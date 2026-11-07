package com.example.pocketmarket.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pocketmarket.data.local.dao.ProductDao
import com.example.pocketmarket.data.local.entity.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class PocketMarketDatabase : RoomDatabase() {
    abstract val productDao: ProductDao
}