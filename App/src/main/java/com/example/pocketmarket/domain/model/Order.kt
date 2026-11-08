package com.example.pocketmarket.domain.model

data class Order(
    val id: String,
    val items: List<CartItem>,
    val totalAmount: Double,
    val timestamp: Long,
    val status: String = "DEMO_COMPLETED"
)