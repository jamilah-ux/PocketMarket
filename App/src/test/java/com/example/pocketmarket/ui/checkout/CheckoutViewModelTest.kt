package com.example.pocketmarket.ui.checkout

import com.example.pocketmarket.data.repository.CartRepositoryImpl
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class CheckoutViewModelTest {

    private lateinit var mockRepository: CartRepositoryImpl
    private lateinit var viewModel: CheckoutViewModel

    @Before
    fun setup() {
        mockRepository = mock(CartRepositoryImpl::class.java)
        viewModel = CheckoutViewModel(mockRepository)
    }

    @Suppress("FunctionName")
    @Test
    fun validateForm_emptyFields_returnsFalseAndSetsFieldErrors() {
        val isValid = viewModel.validateForm()

        assertFalse(isValid)
        assertNotNull(viewModel.uiState.value.fullNameError)
        assertNotNull(viewModel.uiState.value.addressError)
        assertNotNull(viewModel.uiState.value.phoneNumberError)
        assertNotNull(viewModel.uiState.value.deliveryDateError)
    }

    @Suppress("FunctionName")
    @Test
    fun validateForm_validFields_returnsTrueAndClearsErrors() {
        viewModel.onFullNameChanged("Jamila Jalal")
        viewModel.onAddressChanged("Al-Rawdah, Riyadh")
        viewModel.onPhoneNumberChanged("0512345678")
        viewModel.onDeliveryDateSelected(System.currentTimeMillis())

        val isValid = viewModel.validateForm()

        assertTrue(isValid)
        assertNull(viewModel.uiState.value.fullNameError)
        assertNull(viewModel.uiState.value.addressError)
        assertNull(viewModel.uiState.value.phoneNumberError)
        assertNull(viewModel.uiState.value.deliveryDateError)
    }
}