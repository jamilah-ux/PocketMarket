package com.example.pocketmarket.domain.repository

import com.example.pocketmarket.domain.model.Category
import com.example.pocketmarket.domain.model.Product
import com.example.pocketmarket.domain.util.AppResult
import com.example.pocketmarket.domain.util.CoroutineDispatchers
import com.example.pocketmarket.fixtures.MockProducts
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

data class CatalogData(
    val products: List<Product>,
    val categories: List<Category>
)

class CatalogRepository(
    private val dispatchers: CoroutineDispatchers
) {

    suspend fun fetchProducts(delayMs: Long = 1000L, shouldFail: Boolean = false): List<Product> =
        withContext(dispatchers.io) {
            delay(delayMs)
            if (shouldFail) {
                throw IllegalStateException("Failed to load products from server.")
            }
            MockProducts.items
        }

    suspend fun fetchCategories(delayMs: Long = 1000L): List<Category> =
        withContext(dispatchers.io) {
            delay(delayMs)
            MockProducts.items.map { it.category }.distinct()
        }

    // 1. التنفيذ المتتالي (Sequential Execution)
    suspend fun fetchCatalogSequentially(delayPerCallMs: Long = 1000L): AppResult<CatalogData> =
        withContext(dispatchers.io) {
            try {
                val products = fetchProducts(delayMs = delayPerCallMs)
                val categories = fetchCategories(delayMs = delayPerCallMs)
                AppResult.Success(CatalogData(products, categories))
            } catch (e: Exception) {
                AppResult.Error(e)
            }
        }

    // 2. التنفيذ المتوازي (Parallel Execution باستخدام Structured Concurrency)
    suspend fun fetchCatalogInParallel(
        delayPerCallMs: Long = 1000L,
        simulateProductFailure: Boolean = false
    ): AppResult<CatalogData> = withContext(dispatchers.io) {
        try {
            coroutineScope {
                val productsDeferred = async { fetchProducts(delayPerCallMs, simulateProductFailure) }
                val categoriesDeferred = async { fetchCategories(delayPerCallMs) }

                val products = productsDeferred.await()
                val categories = categoriesDeferred.await()

                AppResult.Success(CatalogData(products, categories))
            }
        } catch (e: Exception) {
            AppResult.Error(e)
        }
    }
}