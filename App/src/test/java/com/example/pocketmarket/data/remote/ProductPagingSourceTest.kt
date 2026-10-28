package com.example.pocketmarket.data.remote

import androidx.paging.PagingSource
import com.example.pocketmarket.data.remote.dto.ProductDto
import com.example.pocketmarket.data.remote.dto.ProductsResponseDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class ProductPagingSourceTest {

    // 1. Fake API لمحاكاة DummyJsonApi
    private class FakeDummyJsonApi(
        private val shouldFail: Boolean = false,
        private val totalItems: Int = 40
    ) : DummyJsonApi {
        override suspend fun getProducts(limit: Int, skip: Int): ProductsResponseDto {
            if (shouldFail) {
                throw IOException("Network error")
            }

            val fakeList = (skip until (skip + limit).coerceAtMost(totalItems)).map { id ->
                ProductDto(
                    id = id.toLong(),
                    title = "Product $id",
                    description = "Description $id",
                    price = 10.0,
                    discountPercentage = 0.0,
                    rating = 4.5,
                    stock = 10,
                    category = "Category",
                    thumbnail = "url",
                    images = emptyList()
                )
            }

            return ProductsResponseDto(
                products = fakeList,
                total = totalItems,
                skip = skip,
                limit = limit
            )
        }

        override suspend fun getProductDetail(id: Long): ProductDto {
            throw NotImplementedError()
        }

        override suspend fun searchProducts(query: String, limit: Int, skip: Int): ProductsResponseDto {
            throw NotImplementedError()
        }
    }

    @Test
    fun load_initialPage_returnsSuccessAndNextKey() = runTest {
        // Arrange
        val api = FakeDummyJsonApi(totalItems = 50)
        val pagingSource = ProductPagingSource(api)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        // Assert
        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(20, page.data.size)
        assertEquals(null, page.prevKey)
        assertEquals(20, page.nextKey)
    }

    @Test
    fun load_lastPage_returnsNullNextKey() = runTest {
        // Arrange (مجموع المنتجات 20 والطلب بدأ من 0 وبحجم 20)
        val api = FakeDummyJsonApi(totalItems = 20)
        val pagingSource = ProductPagingSource(api)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Append(
                key = 0,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        // Assert: يجب أن يكون nextKey فارغاً لأنه لا توجد عناصر إضافية
        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(null, page.nextKey)
    }

    @Test
    fun load_networkError_returnsLoadResultError() = runTest {
        // Arrange (محاكاة انقطاع الإنترنت)
        val api = FakeDummyJsonApi(shouldFail = true)
        val pagingSource = ProductPagingSource(api)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        // Assert
        assertTrue(result is PagingSource.LoadResult.Error)
        val errorResult = result as PagingSource.LoadResult.Error
        assertTrue(errorResult.throwable is IOException)
    }
}