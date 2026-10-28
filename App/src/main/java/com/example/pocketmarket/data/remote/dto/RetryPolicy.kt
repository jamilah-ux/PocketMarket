package com.example.pocketmarket.data.remote

import kotlinx.coroutines.delay
import java.io.IOException
import java.net.SocketTimeoutException

suspend fun <T> retryWithBackoff(
    maxRetries: Int = 3,
    initialDelayMs: Long = 1000L,
    factor: Double = 2.0,
    block: suspend () -> T
): T {
    var currentDelay = initialDelayMs
    repeat(maxRetries - 1) { attempt ->
        try {
            return block()
        } catch (e: Exception) {
            // لا نكرر المحاولة إذا كان الخطأ من طرف العميل (4xx) أو خطأ منطقي
            if (e !is IOException && e !is SocketTimeoutException) {
                throw e
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong()
        }
    }
    // المحاولة الأخيرة
    return block()
}