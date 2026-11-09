package com.example.pocketmarket.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pocketmarket.data.local.dao.CartDao
import com.example.pocketmarket.data.local.dao.ProductDao
import com.example.pocketmarket.data.local.entity.CartEntity
import com.example.pocketmarket.data.local.entity.ProductEntity

@Database(
    entities = [ProductEntity::class, CartEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PocketMarketDatabase : RoomDatabase() {
    abstract val productDao: ProductDao
    abstract fun cartDao(): CartDao
}