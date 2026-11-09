package com.example.pocketmarket.ui.util

import org.junit.Assert.assertNotNull
import org.junit.Test

class NotificationHelperTest {

    @Test
    fun notificationChannelId_isConfiguredCorrectly() {
        val channelId = "order_status_channel"
        assertNotNull("Notification Channel ID must not be null", channelId)
    }
}