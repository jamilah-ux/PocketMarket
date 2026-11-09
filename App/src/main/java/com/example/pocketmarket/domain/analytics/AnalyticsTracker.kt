package com.example.pocketmarket.domain.analytics

interface AnalyticsTracker {
    fun logEvent(name: String, params: Map<String, Any> = emptyMap())
}

class SystemAnalyticsTracker : AnalyticsTracker {
    override fun logEvent(name: String, params: Map<String, Any>) {
        // Analytics abstraction hook for event tracking
        println("Analytics Event Logged: $name -> $params")
    }
}