package com.example.pocketmarket.ui.checkout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pocketmarket.domain.model.CartItem
import com.example.pocketmarket.ui.components.PocketButton
import com.example.pocketmarket.ui.components.PocketTextField
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartItems: List<CartItem>,
    onBackClick: () -> Unit,
    onConfirmOrder: () -> Unit,
    modifier: Modifier = Modifier
) {
    var address by remember { mutableStateOf("الرياض - حي الروضة") }
    var selectedPaymentMethod by remember { mutableStateOf("Mada / Credit Card") }

    val subtotal = cartItems.sumOf { it.product.discountedPrice * it.quantity }
    val shipping = if (subtotal > 0) 15.0 else 0.0
    val total = subtotal + shipping

    val formattedSubtotal = String.format(Locale.US, "%.2f", subtotal)
    val formattedShipping = String.format(Locale.US, "%.2f", shipping)
    val formattedTotal = String.format(Locale.US, "%.2f", total)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("إتمام الطلب") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 1. عنوان التوصيل
            Text(
                text = "عنوان التوصيل",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            PocketTextField(
                value = address,
                onValueChange = { address = it },
                label = "عنوان الشحن",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 2. طريقة الدفع
            Text(
                text = "طريقة الدفع",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedPaymentMethod = "Mada / Credit Card" },
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedPaymentMethod == "Mada / Credit Card",
                        onClick = { selectedPaymentMethod = "Mada / Credit Card" }
                    )
                    Icon(Icons.Default.CreditCard, contentDescription = null)
                    Text(
                        text = "بطاقة مدى / ائتمانية",
                        modifier = Modifier.padding(start = 12.dp),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedPaymentMethod = "Cash on Delivery" },
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedPaymentMethod == "Cash on Delivery",
                        onClick = { selectedPaymentMethod = "Cash on Delivery" }
                    )
                    Icon(Icons.Default.LocalShipping, contentDescription = null)
                    Text(
                        text = "الدفع عند الاستلام",
                        modifier = Modifier.padding(start = 12.dp),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. ملخص الحساب
            Text(
                text = "ملخص التكلفة",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                    ) {
                        Text("مجموع المنتجات:")
                        Text("$$formattedSubtotal")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                    ) {
                        Text("رسوم التوصيل:")
                        Text("$$formattedShipping")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                    ) {
                        Text("المجموع النهائي:", fontWeight = FontWeight.Bold)
                        Text(
                            "$$formattedTotal",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 4. زر التأكيد
            PocketButton(
                text = "تأكيد وإرسال الطلب",
                onClick = onConfirmOrder,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
        }
    }
}