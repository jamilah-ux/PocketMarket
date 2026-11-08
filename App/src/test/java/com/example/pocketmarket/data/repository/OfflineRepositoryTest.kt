package com.example.pocketmarket.data.repository

import com.example.pocketmarket.data.local.dao.ProductDao
import com.example.pocketmarket.data.local.entity.ProductEntity
import com.example.pocketmarket.data.remote.DummyJsonApi
import com.example.pocketmarket.data.remote.dto.ProductDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

@OptIn(ExperimentalCoroutinesApi::class)
class OfflineRepositoryTest {

    private lateinit var mockApi: DummyJsonApi
    private lateinit var mockDao: ProductDao
    private lateinit var repository: ProductRepositoryImpl
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockApi = mock(DummyJsonApi::class.java)
        mockDao = mock(ProductDao::class.java)
        repository = ProductRepositoryImpl(mockApi, mockDao, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getProducts_emitsDataFromRoomFirst() = runTest {
        val cachedEntities = listOf(
            ProductEntity(
                id = 1L, title = "Cached Product", description = "Desc",
                price = 10.0, discountPercentage = 0.0, rating = 4.0,
                stock = 2, category = "test", thumbnail = "", images = emptyList()
            )
        )

        `when`(mockDao.getAllProducts()).thenReturn(flowOf(cachedEntities))

        val emissions = repository.getProducts().toList()

        val successEmission = emissions.firstOrNull { it.isSuccess }
        assertTrue(successEmission != null && successEmission.isSuccess)
        val products = successEmission?.getOrNull()
        assertEquals(1, products?.size)
        assertEquals("Cached Product", products?.get(0)?.title)
    }

    @Test
    fun refreshProducts_networkFailure_retainsCachedData() = runTest {
        val cachedEntities = listOf(
            ProductEntity(
                id = 1L, title = "Cached Product", description = "Desc",
                price = 10.0, discountPercentage = 0.0, rating = 4.0,
                stock = 2, category = "test", thumbnail = "", images = emptyList()
            )
        )
        `when`(mockDao.getAllProducts()).thenReturn(flowOf(cachedEntities))
        `when`(mockApi.getProducts()).thenThrow(RuntimeException("Network error"))

        val emissions = repository.getProducts().toList()

        // 1. التثبت من صدور خطأ الشبكة
        val failureEmission = emissions.firstOrNull { it.isFailure }
        assertTrue(failureEmission != null && failureEmission.isFailure)

        // 2. التثبت من استمرار توفر بيانات الكاش المحلي وعدم مسحها
        val successEmission = emissions.firstOrNull { it.isSuccess }
        assertTrue(successEmission != null && successEmission.isSuccess)
        assertEquals("Cached Product", successEmission?.getOrNull()?.get(0)?.title)

        // 3. التأكد من عدم استدعاء مسح البيانات
        verify(mockDao, never()).clearAllProducts()
    }
}