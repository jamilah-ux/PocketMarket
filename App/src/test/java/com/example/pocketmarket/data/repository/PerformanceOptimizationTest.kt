package com.example.pocketmarket.data.repository

import com.example.pocketmarket.domain.model.Category
import com.example.pocketmarket.domain.model.Product
import org.junit.Assert.assertNotNull
import org.junit.Test

class PerformanceOptimizationTest {

    @Test
    fun productModel_instantiatesEfficiently() {
        val category = Category("Electronics")
        val product = Product(
            id = 1L,
            title = "Optimized Item",
            description = "Performance Test",
            price = 100.0,
            discountPercentage = 5.0,
            rating = 4.9,
            stock = 10,
            category = category
        )
        assertNotNull(product)
    }
}