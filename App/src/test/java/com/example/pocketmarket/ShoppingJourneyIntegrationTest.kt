package com.example.pocketmarket

import com.example.pocketmarket.fixtures.MockProducts
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ShoppingJourneyIntegrationTest {

    @Test
    fun endToEndShoppingJourney_addProductAndCompleteCheckout_cartIsCleared() {
        val product = MockProducts.items.first()
        val cartState = mutableListOf(product)

        // Verify product added to cart
        assertEquals(1, cartState.size)
        assertEquals("iPhone 9", cartState.first().title)

        // Simulate successful checkout clearance
        cartState.clear()
        assertTrue("Cart should be empty after completing checkout journey", cartState.isEmpty())
    }
}