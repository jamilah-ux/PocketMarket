package com.example.pocketmarket.domain.repository

import com.example.pocketmarket.domain.model.Product
import com.example.pocketmarket.fixtures.MockProducts
import com.example.pocketmarket.fixtures.generateLargeProductList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ProductRepository {
    fun getProducts(): Flow<Result<List<Product>>>
    suspend fun getProductById(id: Long): Result<Product>
}

class ProductRepositoryImpl : ProductRepository {
    private val cachedProducts: List<Product> by lazy {
        generateLargeProductList(1000)
    }

    override fun getProducts(): Flow<Result<List<Product>>> = flow {
        // محاكاة تحميل بيانات من الشبكة/قاعدة البيانات
        delay(400)
        emit(Result.success(cachedProducts))
    }

    override suspend fun getProductById(id: Long): Result<Product> {
        delay(150)
        val product = cachedProducts.find { it.id == id }
        return if (product != null) {
            Result.success(product)
        } else {
            Result.failure(NoSuchElementException("Product with id $id not found"))
        }
    }
}