package com.example.pocketmarket.domain.model



    data class CartItem(
        val product: Product,
        val quantity: Int
    ) {
        init {
            require(quantity > 0) { "Cart quantity must be greater than zero." }
        }

        val totalPrice: Double
            get() = product.discountedPrice * quantity
    }
