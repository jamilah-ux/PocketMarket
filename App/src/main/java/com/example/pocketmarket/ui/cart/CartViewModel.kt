package com.example.pocketmarket.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketmarket.domain.model.CartItem
import com.example.pocketmarket.domain.model.Product
import com.example.pocketmarket.ui.common.UiEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface CartAction {
    data class AddProduct(val product: Product, val quantity: Int = 1) : CartAction
    data class UpdateQuantity(val item: CartItem, val quantity: Int) : CartAction
    data class RemoveItem(val item: CartItem) : CartAction
    data object ClearCart : CartAction
    data object Checkout : CartAction
}

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val totalAmount: Double = 0.0
)

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private val _effect = Channel<UiEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onAction(action: CartAction) {
        when (action) {
            is CartAction.AddProduct -> addProduct(action.product, action.quantity)
            is CartAction.UpdateQuantity -> updateQuantity(action.item, action.quantity)
            is CartAction.RemoveItem -> removeItem(action.item)
            is CartAction.ClearCart -> clearCart()
            is CartAction.Checkout -> checkout()
        }
    }

    private fun addProduct(product: Product, quantity: Int) {
        _uiState.update { currentState ->
            val existingItemIndex = currentState.items.indexOfFirst { it.product.id == product.id }
            val updatedItems = if (existingItemIndex != -1) {
                currentState.items.toMutableList().apply {
                    val currentItem = this[existingItemIndex]
                    this[existingItemIndex] = currentItem.copy(quantity = currentItem.quantity + quantity)
                }
            } else {
                currentState.items + CartItem(product = product, quantity = quantity)
            }
            currentState.copy(
                items = updatedItems,
                totalAmount = calculateTotal(updatedItems)
            )
        }
        viewModelScope.launch {
            _effect.send(UiEffect.ShowSnackbar("تمت إضافة المنتج إلى السلة"))
        }
    }

    private fun updateQuantity(item: CartItem, quantity: Int) {
        if (quantity <= 0) {
            removeItem(item)
            return
        }
        _uiState.update { currentState ->
            val updatedItems = currentState.items.map {
                if (it.product.id == item.product.id) it.copy(quantity = quantity) else it
            }
            currentState.copy(
                items = updatedItems,
                totalAmount = calculateTotal(updatedItems)
            )
        }
    }

    private fun removeItem(item: CartItem) {
        _uiState.update { currentState ->
            val updatedItems = currentState.items.filterNot { it.product.id == item.product.id }
            currentState.copy(
                items = updatedItems,
                totalAmount = calculateTotal(updatedItems)
            )
        }
    }

    private fun clearCart() {
        _uiState.update { it.copy(items = emptyList(), totalAmount = 0.0) }
    }

    private fun checkout() {
        if (_uiState.value.items.isEmpty()) return
        clearCart()
        viewModelScope.launch {
            _effect.send(UiEffect.ShowSnackbar("تم إتمام الطلب بنجاح! شكرًا لتسوقك."))
        }
    }

    private fun calculateTotal(items: List<CartItem>): Double {
        return items.sumOf { it.product.price * it.quantity }
    }
}