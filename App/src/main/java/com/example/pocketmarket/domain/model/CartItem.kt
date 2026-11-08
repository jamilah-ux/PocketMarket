package com.example.pocketmarket.domain.model

data class CartItem(
    val product: Product,
    val quantity: Int
) {
    val totalPrice: Double
        get() = (product.price * (1 - product.discountPercentage / 100.0)) * quantity
}