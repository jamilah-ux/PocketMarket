package com.example.pocketmarket.domain

import com.example.pocketmarket.domain.model.Category
import com.example.pocketmarket.domain.model.Product
import com.example.pocketmarket.domain.util.calculateAverageRating
import com.example.pocketmarket.domain.util.filterByCategory
import com.example.pocketmarket.domain.util.filterInStockOnly
import com.example.pocketmarket.domain.util.sortByPrice
import com.example.pocketmarket.fixtures.MockProducts
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductDomainTest {

    @Test
    fun discountedPrice_computesCorrectValue() {
        val product = Product(
            id = 100,
            title = "Test Item",
            description = "Desc",
            price = 100.0,
            discountPercentage = 20.0,
            rating = 4.5,
            stock = 10,
            category = Category("test"),
            thumbnail = ""
        )
        assertEquals(80.0, product.discountedPrice, 0.001)
    }

    @Test(expected = IllegalArgumentException::class)
    fun negativePrice_throwsException() {
        Product(
            id = 101,
            title = "Invalid Item",
            description = "Desc",
            price = -5.0,
            rating = 3.0,
            stock = 5,
            category = Category("test"),
            thumbnail = "",
            discountPercentage = 0.0,
            images = emptyList()
        )
    }

    @Test
    fun filterInStockOnly_excludesOutOfStockItems() {
        val inStockList = MockProducts.items.filterInStockOnly()
        assertTrue(inStockList.all { it.stock > 0 })
        assertFalse(inStockList.any { it.stock == 0 })
    }

    @Test
    fun sortByPrice_sortsAscendingAndDescendingCorrectly() {
        val ascending = MockProducts.items.sortByPrice(ascending = true)
        val descending = MockProducts.items.sortByPrice(ascending = false)

        assertTrue(ascending.first().discountedPrice <= ascending.last().discountedPrice)
        assertTrue(descending.first().discountedPrice >= descending.last().discountedPrice)
    }

    @Test
    fun emptyList_handlesOperationsGracefully() {
        val empty = emptyList<Product>()
        assertEquals(emptyList<Product>(), empty.filterByCategory(Category("unknown")))
        assertEquals(emptyList<Product>(), empty.sortByPrice())
        assertEquals(0.0, empty.calculateAverageRating(), 0.0)
    }
}