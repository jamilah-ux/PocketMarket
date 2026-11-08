package com.example.pocketmarket.data.local

import com.example.pocketmarket.data.local.UserPreferences
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UserPreferencesTest {

    @Test
    fun defaultPreferences_hasCorrectDefaultValues() {
        val defaultPrefs = UserPreferences()

        assertFalse(defaultPrefs.isDarkMode)
        assertTrue(defaultPrefs.isGridView)
        assertEquals("all", defaultPrefs.preferredCategory)
        assertFalse(defaultPrefs.isOnboardingCompleted)
    }

    @Test
    fun updatedPreferences_returnsModifiedValues() {
        val updatedPrefs = UserPreferences(
            isDarkMode = true,
            isGridView = false,
            preferredCategory = "electronics",
            isOnboardingCompleted = true
        )

        assertTrue(updatedPrefs.isDarkMode)
        assertFalse(updatedPrefs.isGridView)
        assertEquals("electronics", updatedPrefs.preferredCategory)
        assertTrue(updatedPrefs.isOnboardingCompleted)
    }
}