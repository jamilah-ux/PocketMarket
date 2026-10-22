package com.example.pocketmarket.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketmarket.domain.model.CartItem
import com.example.pocketmarket.domain.model.Product
import com.example.pocketmarket.ui.common.UiEffect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface CartAction {
    data class AddProduct(val product: Product, val quantity: Int) : CartAction
    data class UpdateQuantity(val item: CartItem, val newQuantity: Int) : CartAction
    data class RemoveItem(val item: CartItem) : CartAction
    object Checkout : CartAction
}

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val isCheckingOut: Boolean = false
) {
    val totalAmount: Double
        get() = items.sumOf { it.totalPrice }
}

class CartViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private val _effect = Channel<UiEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onAction(action: CartAction) {
        when (action) {
            is CartAction.AddProduct -> {
                _uiState.update { state ->
                    val existing = state.items.find { it.product.id == action.product.id }
                    val updatedList = if (existing != null) {
                        state.items.map {
                            if (it.product.id == action.product.id) {
                                it.copy(quantity = (it.quantity + action.quantity).coerceAtMost(action.product.stock))
                            } else it
                        }
                    } else {
                        state.items + CartItem(action.product, action.quantity)
                    }
                    state.copy(items = updatedList)
                }
                viewModelScope.launch {
                    _effect.send(UiEffect.ShowSnackbar("تمت إضافة المنتج إلى السلة"))
                }
            }
            is CartAction.UpdateQuantity -> {
                _uiState.update { state ->
                    val updated = state.items.map {
                        if (it.product.id == action.item.product.id) it.copy(quantity = action.newQuantity) else it
                    }
                    state.copy(items = updated)
                }
            }
            is CartAction.RemoveItem -> {
                _uiState.update { state ->
                    state.copy(items = state.items.filterNot { it.product.id == action.item.product.id })
                }
                viewModelScope.launch {
                    _effect.send(UiEffect.ShowSnackbar("تم حذف المنتج من السلة"))
                }
            }
            CartAction.Checkout -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isCheckingOut = true) }
                    // محاكاة إتمام الشراء
                    kotlinx.coroutines.delay(800)
                    _uiState.update { it.copy(items = emptyList(), isCheckingOut = false) }
                    _effect.send(UiEffect.ShowSnackbar("تم إتمام الطلب بنجاح! شكرًا لتسوقك."))
                }
            }
        }
    }
}