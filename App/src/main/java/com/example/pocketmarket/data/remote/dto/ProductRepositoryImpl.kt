package com.example.pocketmarket.data.remote.dto

import com.example.pocketmarket.data.remote.DummyJsonApi
import com.example.pocketmarket.di.IoDispatcher
import com.example.pocketmarket.domain.model.Product
import com.example.pocketmarket.domain.repository.ProductRepository
import com.example.pocketmarket.mapper.toDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val api: DummyJsonApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ProductRepository {

    override fun getProducts(): Flow<Result<List<Product>>> = flow {
        val result = runCatching {
            val response = api.getProducts()
            response.products.toDomain()
        }.recoverCatching { throwable ->
            when (throwable) {
                is IOException -> throw Exception("Network error: Please check your internet connection.", throwable)
                else -> throw throwable
            }
        }
        emit(result)
    }.flowOn(ioDispatcher)

    override suspend fun getProductById(id: Long): Result<Product> = withContext(ioDispatcher) {
        runCatching {
            val dto = api.getProductDetail(id)
            dto.toDomain()
        }.recoverCatching { throwable ->
            when (throwable) {
                is IOException -> throw Exception("Network error: Unable to load product details.", throwable)
                else -> throw throwable
            }
        }
    }
}