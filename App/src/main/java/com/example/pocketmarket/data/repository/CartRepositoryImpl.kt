package com.example.pocketmarket.data.repository

import com.example.pocketmarket.data.local.dao.CartDao
import com.example.pocketmarket.data.local.entity.CartEntity
import com.example.pocketmarket.domain.model.CartItem
import com.example.pocketmarket.domain.model.Category
import com.example.pocketmarket.domain.model.Order
import com.example.pocketmarket.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) {

    val cartItems: Flow<List<CartItem>> = cartDao.getCartItems().map { entities ->
        entities.map { entity ->
            CartItem(
                product = Product(
                    id = entity.productId,
                    title = entity.title,
                    description = "",
                    price = entity.price,
                    discountPercentage = 0.0,
                    rating = 0.0,
                    stock = entity.stock,
                    category = Category("smartphones"),
                    thumbnail = entity.thumbnail,
                    images = emptyList()
                ),
                quantity = entity.quantity
            )
        }
    }

    @Suppress("unused")
    suspend fun addToCart(product: Product, quantity: Int): Result<Unit> {
        if (quantity <= 0) {
            return Result.failure(IllegalArgumentException("Quantity must be greater than zero"))
        }
        if (quantity > product.stock) {
            return Result.failure(IllegalStateException("Requested quantity exceeds available stock"))
        }

        cartDao.insertOrUpdateCartItem(
            CartEntity(
                productId = product.id,
                title = product.title,
                price = product.price,
                thumbnail = product.thumbnail ?: "",
                stock = product.stock,
                quantity = quantity
            )
        )
        return Result.success(Unit)
    }

    @Suppress("unused")
    suspend fun removeFromCart(productId: Long) {
        cartDao.removeCartItem(productId)
    }

    suspend fun simulateCheckout(): Result<Order> {
        val currentItems = cartItems.first()
        if (currentItems.isEmpty()) {
            return Result.failure(IllegalStateException("Cart is empty"))
        }

        for (item in currentItems) {
            if (item.quantity > item.product.stock) {
                return Result.failure(IllegalStateException("Stock insufficient for ${item.product.title}"))
            }
        }

        val totalAmount = currentItems.sumOf { it.totalPrice }
        val order = Order(
            id = "DEMO-ORD-${UUID.randomUUID().toString().take(8).uppercase()}",
            items = currentItems,
            totalAmount = totalAmount,
            timestamp = System.currentTimeMillis()
        )

        cartDao.clearCart()
        return Result.success(order)
    }
}