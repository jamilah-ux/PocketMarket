package com.example.pocketmarket.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.pocketmarket.domain.model.Product
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ProductRepository {
    fun getProducts(): Flow<Result<List<Product>>>
    suspend fun getProductById(id: Long): Result<Product>
    fun getPagedProducts(): Flow<PagingData<Product>>
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

    override fun getPagedProducts(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                // Fake PagingSource لدعم الـ Mock الموجود في هذا الكلاس
                FakeProductPagingSource(cachedProducts)
            }
        ).flow
    }

    private fun generateLargeProductList(count: Int): List<Product> {
        // إذا كانت الدالة الأصلية موجودة لديك في نهاية الملف اتركيها كما هي
        return emptyList()
    }
}

// كلاس مساعد بسيط للـ Fake Paging حتى يعمل كلاس الـ Mock بدون أخطاء
class FakeProductPagingSource(
    private val items: List<Product>
) : androidx.paging.PagingSource<Int, Product>() {
    override fun getRefreshKey(state: androidx.paging.PagingState<Int, Product>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: 0
        val pageSize = params.loadSize
        val fromIndex = page * pageSize
        val toIndex = minOf(fromIndex + pageSize, items.size)

        return if (fromIndex <= items.size) {
            LoadResult.Page(
                data = items.subList(fromIndex, toIndex),
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (toIndex >= items.size) null else page + 1
            )
        } else {
            LoadResult.Page(emptyList(), null, null)
        }
    }
}