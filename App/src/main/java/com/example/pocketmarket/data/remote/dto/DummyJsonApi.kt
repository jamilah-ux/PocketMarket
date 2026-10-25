package com.example.pocketmarket.data.remote

import com.example.pocketmarket.data.remote.dto.ProductDto
import com.example.pocketmarket.data.remote.dto.ProductsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DummyJsonApi {

    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 30
    ): ProductsResponseDto

    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String
    ): ProductsResponseDto

    @GET("products/categories")
    suspend fun getCategories(): List<String>

    @GET("products/{id}")
    suspend fun getProductDetail(
        @Path("id") id: Long
    ): ProductDto
}