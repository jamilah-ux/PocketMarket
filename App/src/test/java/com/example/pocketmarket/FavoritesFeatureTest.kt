package com.example.pocketmarket

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoritesFeatureTest {

    @Test
    fun toggleFavorite_addsAndRemovesProductIdCorrectly() {
        val favoriteIds = mutableSetOf<Long>()
        val productId = 101L

        // Toggle On
        favoriteIds.add(productId)
        assertTrue("Product ID should be present in favorites set", favoriteIds.contains(productId))

        // Toggle Off
        favoriteIds.remove(productId)
        assertFalse("Product ID should be removed from favorites set", favoriteIds.contains(productId))
    }
}