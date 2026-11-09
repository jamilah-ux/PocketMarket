package com.example.pocketmarket.ui.checkout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.pocketmarket.ui.components.OrderProgressRing
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: CheckoutViewModel,
    onCheckoutSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    val fullNameFocus = remember { FocusRequester() }
    val addressFocus = remember { FocusRequester() }
    val phoneFocus = remember { FocusRequester() }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val deliveryOptions = listOf("Standard Delivery", "Express Delivery", "Same Day Pickup")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Checkout (Simulation)") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // مؤشر تقدم محاكاة مراحل الطلب عبر Canvas
            OrderProgressRing(
                progress = if (uiState.isLoading) 0.5f else 1.0f,
                size = 100.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Full Name Input
            OutlinedTextField(
                value = uiState.fullName,
                onValueChange = viewModel::onFullNameChanged,
                label = { Text("Full Name") },
                isError = uiState.fullNameError != null,
                supportingText = { uiState.fullNameError?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { addressFocus.requestFocus() }),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(fullNameFocus)
            )

            // Address Input
            OutlinedTextField(
                value = uiState.address,
                onValueChange = viewModel::onAddressChanged,
                label = { Text("Shipping Address") },
                isError = uiState.addressError != null,
                supportingText = { uiState.addressError?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { phoneFocus.requestFocus() }),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(addressFocus)
            )

            // Phone Number Input
            OutlinedTextField(
                value = uiState.phoneNumber,
                onValueChange = viewModel::onPhoneNumberChanged,
                label = { Text("Phone Number") },
                isError = uiState.phoneNumberError != null,
                supportingText = { uiState.phoneNumberError?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(phoneFocus)
            )

            // Dropdown Menu: Delivery Method
            ExposedDropdownMenuBox(
                expanded = uiState.isDropdownExpanded,
                onExpandedChange = viewModel::onDropdownExpandedChanged
            ) {
                OutlinedTextField(
                    value = uiState.deliveryMethod,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Delivery Option") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.isDropdownExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = uiState.isDropdownExpanded,
                    onDismissRequest = { viewModel.onDropdownExpandedChanged(false) }
                ) {
                    deliveryOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = { viewModel.onDeliveryMethodSelected(option) }
                        )
                    }
                }
            }

            // Date Picker Field
            val formattedDate = uiState.deliveryDateMillis?.let {
                SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()).format(Date(it))
            } ?: ""

            OutlinedTextField(
                value = formattedDate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Preferred Delivery Date") },
                isError = uiState.deliveryDateError != null,
                supportingText = { uiState.deliveryDateError?.let { Text(it) } },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            )

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.onDeliveryDateSelected(datePickerState.selectedDateMillis)
                            showDatePicker = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            // General Error Banner مع أنيميشن ظهوره
            AnimatedVisibility(
                visible = uiState.generalError != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                uiState.generalError?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }

            // Submit Button
            Button(
                onClick = {
                    focusManager.clearFocus()
                    if (!viewModel.validateForm()) {
                        // Advanced UX: Focus on the first field containing error
                        when {
                            uiState.fullNameError != null -> fullNameFocus.requestFocus()
                            uiState.addressError != null -> addressFocus.requestFocus()
                            uiState.phoneNumberError != null -> phoneFocus.requestFocus()
                        }
                    } else {
                        viewModel.submitCheckout(onCheckoutSuccess)
                    }
                },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Place Demo Order")
                }
            }
        }
    }
}