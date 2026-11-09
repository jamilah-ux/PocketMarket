package com.example.pocketmarket.ui.checkout

import com.example.pocketmarket.domain.model.Order

data class CheckoutFormState(
    val fullName: String = "",
    val fullNameError: String? = null,
    val address: String = "",
    val addressError: String? = null,
    val phoneNumber: String = "",
    val phoneNumberError: String? = null,
    val deliveryMethod: String = "Standard Delivery",
    val deliveryDateMillis: Long? = null,
    val deliveryDateError: String? = null,
    val isDropdownExpanded: Boolean = false,
    val isLoading: Boolean = false,
    val checkoutResult: Order? = null,
    val generalError: String? = null
) {
    val isValid: Boolean
        get() = fullNameError == null &&
                addressError == null &&
                phoneNumberError == null &&
                deliveryDateError == null &&
                fullName.isNotBlank() &&
                address.isNotBlank() &&
                phoneNumber.isNotBlank() &&
                deliveryDateMillis != null
}