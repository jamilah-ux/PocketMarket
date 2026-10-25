package com.example.pocketmarket.mapper

import com.example.pocketmarket.data.remote.dto.ProductDto
import com.example.pocketmarket.domain.model.Category
import com.example.pocketmarket.domain.model.Product

fun ProductDto.toDomain(): Product {
    return Product(
        id = this.id,
        title = this.title,
        description = this.description,
        price = this.price,
        discountPercentage = this.discountPercentage,
        rating = this.rating.coerceIn(0.0, 5.0),
        stock = this.stock.coerceAtLeast(0),
        brand = null,
        category = Category(this.category.ifBlank { "general" }),
        thumbnail = this.thumbnail,
        images = this.images
    )
}

fun List<ProductDto>.toDomain(): List<Product> = map { it.toDomain() }