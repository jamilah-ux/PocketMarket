package com.example.pocketmarket.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pocketmarket.data.local.dao.ProductDao
import com.example.pocketmarket.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductDaoTest {

    private lateinit var database: PocketMarketDatabase
    private lateinit var dao: ProductDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PocketMarketDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.productDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndGetAllProducts_returnsInsertedData() = runBlocking {
        val sampleEntity = ProductEntity(
            id = 1L,
            title = "Test Product",
            description = "Test Desc",
            price = 99.99,
            discountPercentage = 10.0,
            rating = 4.5,
            stock = 5,
            category = "electronics",
            thumbnail = "http://test.com/img.jpg",
            images = listOf("http://test.com/img.jpg")
        )

        dao.insertProducts(listOf(sampleEntity))

        val products = dao.getAllProducts().first()
        assertEquals(1, products.size)
        assertEquals("Test Product", products[0].title)
    }
}