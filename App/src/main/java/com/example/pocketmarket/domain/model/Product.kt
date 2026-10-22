package com.example.pocketmarket.domain.model

data class Product(
    val id: Long,
    val title: String,
    val description: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val brand: String? = null,
    val category: Category,
    val thumbnail: String? = null,
    val images: List<String> = emptyList()
) {
    init {
        require(price >= 0.0) { "Price cannot be negative." }
        require(rating in 0.0..5.0) { "Rating must be between 0.0 and 5.0." }
        require(stock >= 0) { "Stock cannot be negative." }
    }

    val discountedPrice: Double
        get() = if (discountPercentage > 0) {
            price * (1 - (discountPercentage / 100))
        } else {
            price
        }
}