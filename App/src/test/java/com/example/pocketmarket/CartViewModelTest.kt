package com.example.pocketmarket

import com.example.pocketmarket.fixtures.MockProducts
import com.example.pocketmarket.ui.cart.CartAction
import com.example.pocketmarket.ui.cart.CartViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CartViewModelTest {

    private lateinit var viewModel: CartViewModel

    // استخدام منتج جاهز ومطابق لتصميم Product في المشروع
    private val sampleProduct = MockProducts.items.first()

    @Before
    fun setUp() {
        viewModel = CartViewModel()
    }

    @Test
    fun addProduct_updatesCartItemsCorrectly() {
        viewModel.onAction(CartAction.AddProduct(sampleProduct, 2))

        val currentItems = viewModel.uiState.value.items
        assertEquals(1, currentItems.size)
        assertEquals(2, currentItems.first().quantity)
        assertEquals(sampleProduct.id, currentItems.first().product.id)
    }

    @Test
    fun updateQuantity_modifiesItemQuantity() {
        viewModel.onAction(CartAction.AddProduct(sampleProduct, 1))
        val currentItem = viewModel.uiState.value.items.first()

        viewModel.onAction(CartAction.UpdateQuantity(currentItem, 3))

        val updatedQuantity = viewModel.uiState.value.items.first().quantity
        assertEquals(3, updatedQuantity)
    }

    @Test
    fun removeItem_removesItemFromCart() {
        viewModel.onAction(CartAction.AddProduct(sampleProduct, 1))
        val currentItem = viewModel.uiState.value.items.first()

        viewModel.onAction(CartAction.RemoveItem(currentItem))

        assertEquals(0, viewModel.uiState.value.items.size)
    }
}