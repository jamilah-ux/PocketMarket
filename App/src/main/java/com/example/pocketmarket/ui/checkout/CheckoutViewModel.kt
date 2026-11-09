package com.example.pocketmarket.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketmarket.data.repository.CartRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepository: CartRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutFormState())
    val uiState: StateFlow<CheckoutFormState> = _uiState.asStateFlow()

    fun onFullNameChanged(name: String) {
        _uiState.update {
            it.copy(
                fullName = name,
                fullNameError = if (name.trim().length < 3) "Name must be at least 3 characters" else null
            )
        }
    }

    fun onAddressChanged(address: String) {
        _uiState.update {
            it.copy(
                address = address,
                addressError = if (address.trim().isBlank()) "Address cannot be empty" else null
            )
        }
    }

    fun onPhoneNumberChanged(phone: String) {
        val phoneRegex = Regex("^[0-9]{9,12}$")
        _uiState.update {
            it.copy(
                phoneNumber = phone,
                phoneNumberError = if (!phone.matches(phoneRegex)) "Enter a valid phone number (9-12 digits)" else null
            )
        }
    }

    fun onDeliveryMethodSelected(method: String) {
        _uiState.update { it.copy(deliveryMethod = method, isDropdownExpanded = false) }
    }

    fun onDropdownExpandedChanged(expanded: Boolean) {
        _uiState.update { it.copy(isDropdownExpanded = expanded) }
    }

    fun onDeliveryDateSelected(dateMillis: Long?) {
        _uiState.update {
            it.copy(
                deliveryDateMillis = dateMillis,
                deliveryDateError = if (dateMillis == null) "Please select a delivery date" else null
            )
        }
    }

    fun validateForm(): Boolean {
        val state = _uiState.value
        val nameError = if (state.fullName.trim().length < 3) "Name must be at least 3 characters" else null
        val addressError = if (state.address.trim().isBlank()) "Address cannot be empty" else null
        val phoneRegex = Regex("^[0-9]{9,12}$")
        val phoneError = if (!state.phoneNumber.matches(phoneRegex)) "Enter a valid phone number (9-12 digits)" else null
        val dateError = if (state.deliveryDateMillis == null) "Please select a delivery date" else null

        _uiState.update {
            it.copy(
                fullNameError = nameError,
                addressError = addressError,
                phoneNumberError = phoneError,
                deliveryDateError = dateError
            )
        }

        return nameError == null && addressError == null && phoneError == null && dateError == null
    }

    fun submitCheckout(onSuccess: () -> Unit) {
        if (!validateForm()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }
            val result = cartRepository.simulateCheckout()
            result.onSuccess { order ->
                _uiState.update { it.copy(isLoading = false, checkoutResult = order) }
                onSuccess()
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, generalError = error.message) }
            }
        }
    }
}