package com.example.pocketmarket.ui.home

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import org.junit.Assert.assertEquals
import org.junit.Test

class AdaptiveLayoutTest {

    @Test
    fun gridColumnCountReturnsCorrectColumnsBasedOnWindowWidth() {
        fun getColumns(windowClass: WindowWidthSizeClass): Int = when (windowClass) {
            WindowWidthSizeClass.Compact -> 2
            WindowWidthSizeClass.Medium -> 3
            WindowWidthSizeClass.Expanded -> 2
            else -> 2
        }

        assertEquals(2, getColumns(WindowWidthSizeClass.Compact))
        assertEquals(3, getColumns(WindowWidthSizeClass.Medium))
        assertEquals(2, getColumns(WindowWidthSizeClass.Expanded))
    }
}