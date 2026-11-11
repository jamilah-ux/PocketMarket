package com.example.pocketmarket.ui.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pocketmarket.R
import com.example.pocketmarket.domain.model.CartItem
import com.example.pocketmarket.ui.components.PocketAddToCartButton
import com.example.pocketmarket.ui.components.PocketButton
import com.example.pocketmarket.ui.components.PocketEmptyState
import com.example.pocketmarket.ui.components.PocketProductImage
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    items: List<CartItem>,
    currentLanguage: String = "ar", // 👈 استقبال اللغة الحالية
    onBackClick: () -> Unit,
    onQuantityChange: (CartItem, Int) -> Unit,
    onRemoveItem: (CartItem) -> Unit,
    onCheckoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEn = currentLanguage == "en"
    var itemToDelete by remember { mutableStateOf<CartItem?>(null) }
    val totalPrice = items.sumOf { it.product.discountedPrice * it.quantity }
    val formattedTotalPrice = String.format(Locale.US, "%.2f", totalPrice)

    // نافذة تأكيد الحذف المترجمة ديناميكياً
    itemToDelete?.let { item ->
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = { Text(if (isEn) "Delete Product" else "حذف المنتج") },
            text = {
                Text(
                    if (isEn) "Are you sure you want to remove ${item.product.title} from cart?"
                    else "هل أنت متأكد من رغبتك في حذف ${item.product.title} من السلة؟"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRemoveItem(item)
                        itemToDelete = null
                    }
                ) {
                    Text(if (isEn) "Delete" else "حذف", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) {
                    Text(if (isEn) "Cancel" else "إلغاء")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEn) "Shopping Cart" else stringResource(R.string.cart_title)) },
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
        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    PocketEmptyState(
                        title = stringResource(R.string.cart_empty_title),
                        description = stringResource(R.string.cart_empty_desc)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PocketButton(
                        text = if (isEn) "Browse Products" else "تصفح المنتجات",
                        onClick = onBackClick,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = items,
                        key = { it.product.id }
                    ) { item ->
                        CartItemRow(
                            item = item,
                            onQuantityChange = { newQty -> onQuantityChange(item, newQty) },
                            onRemoveRequest = { itemToDelete = item }
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (isEn) "Total Amount" else "المجموع الإجمالي",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "$$formattedTotalPrice",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        PocketButton(
                            text = if (isEn) "Checkout" else stringResource(R.string.cart_checkout),
                            onClick = onCheckoutClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemoveRequest: () -> Unit
) {
    val formattedPrice = String.format(Locale.US, "%.2f", item.product.discountedPrice * item.quantity)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PocketProductImage(
                imageUrl = item.product.thumbnail,
                contentDescription = item.product.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.product.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$$formattedPrice",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))

                PocketAddToCartButton(
                    quantityInCart = item.quantity,
                    onIncrease = { onQuantityChange(item.quantity + 1) },
                    onDecrease = {
                        if (item.quantity > 1) {
                            onQuantityChange(item.quantity - 1)
                        } else {
                            onRemoveRequest()
                        }
                    },
                    onAddToCart = { },
                    modifier = Modifier.width(120.dp)
                )
            }

            IconButton(onClick = onRemoveRequest) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.cart_remove),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}