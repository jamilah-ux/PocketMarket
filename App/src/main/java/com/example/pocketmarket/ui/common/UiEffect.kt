package com.example.pocketmarket.ui.common

sealed interface UiEffect {
    data class ShowSnackbar(val message: String, val actionLabel: String? = null) : UiEffect
    data class Navigate(val route: String) : UiEffect
}