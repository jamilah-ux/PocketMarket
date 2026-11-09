package com.example.pocketmarket.ui.cart

import com.example.pocketmarket.domain.model.CartItem
import com.example.pocketmarket.domain.model.Category
import com.example.pocketmarket.domain.model.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CartViewModelTest {

    private val testCategory = Category("Electronics")

    @Test
    fun calculateCartTotal_returnsCorrectSum() {
        val p1 = Product(
            id = 1L,
            title = "Phone",
            description = "",
            price = 500.0,
            discountPercentage = 10.0,
            rating = 4.5,
            stock = 10,
            category = testCategory
        )
        val p2 = Product(
            id = 2L,
            title = "Case",
            description = "",
            price = 30.0,
            discountPercentage = 33.33,
            rating = 4.0,
            stock = 20,
            category = testCategory
        )

        val items = listOf(
            CartItem(product = p1, quantity = 2),
            CartItem(product = p2, quantity = 1)
        )

        val total = items.sumOf { it.totalPrice }
        assertTrue(total > 0)
    }

    @Test
    fun emptyCart_returnsZeroTotal() {
        val items = emptyList<CartItem>()
        val total = items.sumOf { it.totalPrice }
        assertEquals(0.0, total, 0.001)
    }
}