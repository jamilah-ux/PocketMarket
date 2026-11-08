package com.example.pocketmarket.data.repository

import com.example.pocketmarket.domain.repository.CatalogRepository
import com.example.pocketmarket.domain.util.AppResult
import com.example.pocketmarket.domain.util.CoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()

    private val testDispatchers = object : CoroutineDispatchers {
        override val main: CoroutineDispatcher = testDispatcher
        override val io: CoroutineDispatcher = testDispatcher
        override val default: CoroutineDispatcher = testDispatcher
    }

    private lateinit var repository: CatalogRepository

    @Before
    fun setup() {
        repository = CatalogRepository(testDispatchers)
    }

    @Test
    fun fetchCatalogSequentially_takesDoubleTheTime() = runTest(testDispatcher) {
        val delayPerCall = 1000L
        val startTime = currentTime

        val result = repository.fetchCatalogSequentially(delayPerCallMs = delayPerCall)

        val totalTime = currentTime - startTime

        Assert.assertTrue(result is AppResult.Success)
        Assert.assertEquals(2000L, totalTime) // 1000ms + 1000ms = 2000ms
    }

    @Test
    fun fetchCatalogInParallel_executesConcurrentlyInSingleDelayTime() = runTest(testDispatcher) {
        val delayPerCall = 1000L
        val startTime = currentTime

        val result = repository.fetchCatalogInParallel(delayPerCallMs = delayPerCall)

        val totalTime = currentTime - startTime

        Assert.assertTrue(result is AppResult.Success)
        Assert.assertEquals(1000L, totalTime) // Parallel: executed at the same time
    }

    @Test
    fun fetchCatalogInParallel_whenOneTaskFails_returnsErrorResult() = runTest(testDispatcher) {
        val result = repository.fetchCatalogInParallel(
            delayPerCallMs = 500L,
            simulateProductFailure = true
        )

        Assert.assertTrue(result is AppResult.Error)
        Assert.assertEquals(
            "Failed to load products from server.",
            (result as AppResult.Error).message
        )
    }

    @Test
    fun cancellation_stopsExecutionImmediately() = runTest(testDispatcher) {
        var completed = false
        val job = launch {
            repository.fetchProducts(delayMs = 5000L)
            completed = true
        }

        testScheduler.advanceTimeBy(1000L) // تقدم الوقت جزئياً
        job.cancelAndJoin() // إلغاء العملية أثناء الانتظار

        Assert.assertTrue(job.isCancelled)
        Assert.assertTrue(!completed) // التأكد أن الكود توقف ولم يكمل
    }
}