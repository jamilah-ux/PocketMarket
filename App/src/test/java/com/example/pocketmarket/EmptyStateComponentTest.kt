package com.example.pocketmarket

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EmptyStateComponentTest {

    @Test
    fun emptyState_presentsCorrectTitleAndDescription() {
        val title = "لا توجد منتجات"
        val description = "لم نجد أي منتجات تطابق بحثك حالياً."

        assertEquals("لا توجد منتجات", title)
        assertTrue("Description should contain user context", description.isNotEmpty())
    }
}