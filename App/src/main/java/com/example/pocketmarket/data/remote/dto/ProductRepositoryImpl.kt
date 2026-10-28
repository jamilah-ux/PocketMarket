package com.example.pocketmarket.data.remote.dto

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.pocketmarket.data.remote.DummyJsonApi
import com.example.pocketmarket.data.remote.ProductPagingSource
import com.example.pocketmarket.data.remote.retryWithBackoff
import com.example.pocketmarket.di.IoDispatcher
import com.example.pocketmarket.domain.model.AppError
import com.example.pocketmarket.domain.model.Product
import com.example.pocketmarket.domain.repository.ProductRepository
import com.example.pocketmarket.mapper.toDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val api: DummyJsonApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ProductRepository {

    override fun getProducts(): Flow<Result<List<Product>>> = flow {
        val result: Result<List<Product>> = try {
            val products = retryWithBackoff(maxRetries = 3, initialDelayMs = 1000L) {
                val response = api.getProducts()
                response.products.toDomain()
            }
            Result.success(products)
        } catch (e: Throwable) {
            val appError = mapToAppError(e)
            Result.failure(appError)
        }
        emit(result)
    }.flowOn(ioDispatcher)

    override suspend fun getProductById(id: Long): Result<Product> = withContext(ioDispatcher) {
        try {
            val product = retryWithBackoff(maxRetries = 2, initialDelayMs = 500L) {
                val dto = api.getProductDetail(id)
                dto.toDomain()
            }
            Result.success(product)
        } catch (e: Throwable) {
            Result.failure(mapToAppError(e))
        }
    }

    override fun getPagedProducts(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = ProductPagingSource.PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = ProductPagingSource.PAGE_SIZE
            ),
            pagingSourceFactory = { ProductPagingSource(api) }
        ).flow.flowOn(ioDispatcher)
    }

    /**
     * تحويل الأخطاء الفنية إلى كائنات AppError مخصصة (4xx, 5xx, Timeouts, No Internet)
     */
    private fun mapToAppError(throwable: Throwable): AppError {
        return when (throwable) {
            is AppError -> throwable
            is UnknownHostException -> AppError.NoInternet
            is SocketTimeoutException -> AppError.Timeout
            is HttpException -> {
                val code = throwable.code()
                when (code) {
                    in 400..499 -> AppError.ClientError(code, throwable.message())
                    in 500..599 -> AppError.ServerError(code, throwable.message())
                    else -> AppError.Unknown(throwable.message())
                }
            }
            is IOException -> AppError.NoInternet
            else -> AppError.Unknown(throwable.localizedMessage)
        }
    }
}