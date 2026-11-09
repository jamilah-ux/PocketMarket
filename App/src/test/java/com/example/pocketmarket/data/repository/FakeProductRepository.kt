package com.example.pocketmarket.data.repository

import com.example.pocketmarket.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FakeProductRepository {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    private var shouldReturnError = false

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    fun emitProducts(products: List<Product>) {
        _products.value = products
    }

    fun getProducts(): Flow<Result<List<Product>>> = flow {
        if (shouldReturnError) {
            emit(Result.failure(RuntimeException("Network Error")))
        } else {
            _products.collect { list ->
                emit(Result.success(list))
            }
        }
    }

    fun searchProducts(query: String): Flow<List<Product>> {
        return _products.map { list ->
            list.filter { it.title.contains(query, ignoreCase = true) }
        }
    }
}