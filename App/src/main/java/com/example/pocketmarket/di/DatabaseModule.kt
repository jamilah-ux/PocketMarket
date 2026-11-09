package com.example.pocketmarket.di

import android.content.Context
import androidx.room.Room
import com.example.pocketmarket.data.local.PocketMarketDatabase
import com.example.pocketmarket.data.local.dao.CartDao
import com.example.pocketmarket.data.local.dao.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): PocketMarketDatabase {
        return Room.databaseBuilder(
            context,
            PocketMarketDatabase::class.java,
            "pocketmarket.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideProductDao(db: PocketMarketDatabase): ProductDao {
        return db.productDao
    }

    @Provides
    fun provideCartDao(db: PocketMarketDatabase): CartDao {
        return db.cartDao()
    }
}