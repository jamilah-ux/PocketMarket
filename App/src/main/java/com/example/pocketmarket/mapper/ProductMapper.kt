package com.example.pocketmarket.mapper

import com.example.pocketmarket.data.local.entity.ProductEntity
import com.example.pocketmarket.data.remote.dto.ProductDto
import com.example.pocketmarket.domain.model.Category
import com.example.pocketmarket.domain.model.Product

fun ProductDto.toDomain(): Product {
    return Product(
        id = this.id.toLong(),
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

// ----- Room Entity Mappers -----

fun ProductEntity.toDomain(): Product {
    return Product(
        id = this.id,
        title = this.title,
        description = this.description,
        price = this.price,
        discountPercentage = this.discountPercentage,
        rating = this.rating,
        stock = this.stock,
        brand = null,
        category = Category(this.category),
        thumbnail = this.thumbnail,
        images = this.images
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        price = this.price,
        discountPercentage = this.discountPercentage,
        rating = this.rating,
        stock = this.stock,
        category = this.category.value,
        thumbnail = this.thumbnail.orEmpty(),
        images = this.images
    )
}

fun List<ProductEntity>.toDomainFromEntity(): List<Product> = map { it.toDomain() }
fun List<Product>.toEntityList(): List<ProductEntity> = map { it.toEntity() }