package com.example.pocketmarket.domain.util


import com.example.pocketmarket.domain.model.Category
import com.example.pocketmarket.domain.model.Product

fun List<Product>.filterByCategory(category: Category): List<Product> =
    filter { it.category == category }

fun List<Product>.filterInStockOnly(): List<Product> =
    filter { it.isInStock }

fun List<Product>.sortByPrice(ascending: Boolean = true): List<Product> =
    if (ascending) sortedBy { it.discountedPrice } else sortedByDescending { it.discountedPrice }

fun List<Product>.groupByCategory(): Map<Category, List<Product>> =
    groupBy { it.category }

fun List<Product>.calculateAverageRating(): Double =
    if (isEmpty()) 0.0 else map { it.rating }.average()