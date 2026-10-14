package com.example.pocketmarket.domain.model



    enum class Currency { USD, SAR, EUR }

    data class UserPreferences(
        val preferredCurrency: Currency = Currency.USD,
        val darkModeEnabled: Boolean = false,
        val notificationsEnabled: Boolean = true
    )
