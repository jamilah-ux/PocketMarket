package com.example.pocketmarket.domain.model

enum class OrderStatus(val label: String) {
    PROCESSING("قيد المعالجة ⏳"),
    SHIPPED("تم الشحن 🚚"),
    DELIVERED("تم التوصيل ✅"),
    CANCELLED("ملغي ❌")
}

data class MarketOrder(
    val id: String,
    val date: String,
    val totalAmount: Double,
    val itemsCount: Int,
    val status: OrderStatus
)

data class UserProfile(
    val id: String = "USR-22413613",
    val name: String = "Jamila Jalal",
    val email: String = "jamila@example.com",
    val phone: String = "+966 50 000 0000",
    val isLoggedIn: Boolean = true,
    val orders: List<MarketOrder> = emptyList()
)