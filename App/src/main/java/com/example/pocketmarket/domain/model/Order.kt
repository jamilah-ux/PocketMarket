package com.example.pocketmarket.domain.model



    enum class OrderStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        CANCELLED
    }

    data class Order(
        val orderId: String,
        val items: List<CartItem>,
        val createdAtEpochMs: Long,
        val status: OrderStatus = OrderStatus.PENDING
    ) {
        val totalAmount: Double
            get() = items.sumOf { it.totalPrice }
    }
