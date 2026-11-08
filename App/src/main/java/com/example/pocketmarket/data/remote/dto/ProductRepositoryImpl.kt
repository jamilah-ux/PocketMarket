package com.example.pocketmarket.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.pocketmarket.data.local.dao.ProductDao
import com.example.pocketmarket.data.remote.DummyJsonApi
import com.example.pocketmarket.data.remote.ProductPagingSource
import com.example.pocketmarket.data.remote.retryWithBackoff
import com.example.pocketmarket.di.IoDispatcher
import com.example.pocketmarket.domain.model.AppError
import com.example.pocketmarket.domain.model.Product
import com.example.pocketmarket.domain.repository.ProductRepository
import com.example.pocketmarket.mapper.toDomain
import com.example.pocketmarket.mapper.toDomainFromEntity
import com.example.pocketmarket.mapper.toEntityList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val api: DummyJsonApi,
    private val productDao: ProductDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ProductRepository {

    private var lastSyncTimestamp: Long = 0
    private val cacheTimeoutMillis = TimeUnit.MINUTES.toMillis(5)

    override fun getProducts(): Flow<Result<List<Product>>> = flow {
        // 1. إرسال بيانات Room المحلية أولاً
        val localFlow = productDao.getAllProducts().map { entities ->
            Result.success(entities.toDomainFromEntity())
        }

        // 2. التحديث من الشبكة إذا انتهت صلاحية الكاش
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastSyncTimestamp > cacheTimeoutMillis) {
            try {
                val remoteProducts = retryWithBackoff(maxRetries = 3, initialDelayMs = 1000L) {
                    val response = api.getProducts()
                    response.products.toDomain()
                }
                productDao.insertProducts(remoteProducts.toEntityList())
                lastSyncTimestamp = currentTime
            } catch (e: Throwable) {
                val appError = mapToAppError(e)
                emit(Result.failure(appError))
            }
        }

        // 3. التحديث المستمر من قاعدة البيانات
        emitAll(localFlow)
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