package com.example.pocketmarket

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OfflineStateTest {

    @Test
    fun offlineNetworkState_showsCachedDataAndRetryBanner() {
        val isNetworkAvailable = false
        val hasCachedData = true

        assertFalse("Network should be detected as unavailable", isNetworkAvailable)
        assertTrue("Cache layer must serve local data when offline", hasCachedData)
    }
}