package com.example.pocketmarket.ui.navigation

sealed class ScreenRoute(val route: String) {
    object Home : ScreenRoute("home")
    object Catalog : ScreenRoute("catalog")
    object Detail : ScreenRoute("detail/{productId}") {
        fun createRoute(productId: Long) = "detail/$productId"
    }
    object Cart : ScreenRoute("cart")
    object Settings : ScreenRoute("settings")
}