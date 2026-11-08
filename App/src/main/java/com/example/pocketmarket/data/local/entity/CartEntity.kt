package com.example.pocketmarket.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey val productId: Long,
    val title: String,
    val price: Double,
    val thumbnail: String,
    val stock: Int,
    val quantity: Int
)