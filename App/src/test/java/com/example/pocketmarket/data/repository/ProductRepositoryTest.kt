package com.example.pocketmarket.data.repository

import app.cash.turbine.test
import com.example.pocketmarket.domain.model.Category
import com.example.pocketmarket.domain.model.Product
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ProductRepositoryTest {

    private lateinit var repository: FakeProductRepository
    private val testCategory = Category("Electronics")

    @Before
    fun setUp() {
        repository = FakeProductRepository()
    }

    @Test
    fun getProducts_emitsSuccessWithProductList() = runTest {
        val testProducts = listOf(
            Product(
                id = 1L,
                title = "Laptop",
                description = "Test Laptop",
                price = 999.99,
                discountPercentage = 10.0,
                rating = 4.5,
                stock = 5,
                category = testCategory
            )
        )
        repository.emitProducts(testProducts)

        repository.getProducts().test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(testProducts, result.getOrNull())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getProducts_emitsFailure_whenNetworkErrorOccurs() = runTest {
        repository.setShouldReturnError(true)

        repository.getProducts().test {
            val result = awaitItem()
            assertTrue(result.isFailure)
            assertEquals("Network Error", result.exceptionOrNull()?.message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun searchProducts_filtersListCorrectly() = runTest {
        val products = listOf(
            Product(
                id = 1L,
                title = "Headphones",
                description = "Audio",
                price = 100.0,
                discountPercentage = 20.0,
                rating = 4.8,
                stock = 10,
                category = testCategory
            ),
            Product(
                id = 2L,
                title = "Mouse",
                description = "Peripherals",
                price = 50.0,
                discountPercentage = 10.0,
                rating = 4.2,
                stock = 15,
                category = testCategory
            )
        )
        repository.emitProducts(products)

        repository.searchProducts("Head").test {
            val filteredList = awaitItem()
            assertEquals(1, filteredList.size)
            assertEquals("Headphones", filteredList.first().title)
            cancelAndIgnoreRemainingEvents()
        }
    }
}