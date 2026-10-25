package com.example.pocketmarket.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("price") val price: Double,
    @SerialName("discountPercentage") val discountPercentage: Double = 0.0,
    @SerialName("rating") val rating: Double = 0.0,
    @SerialName("stock") val stock: Int = 0,
    @SerialName("category") val category: String,
    @SerialName("thumbnail") val thumbnail: String,
    @SerialName("images") val images: List<String> = emptyList()
)