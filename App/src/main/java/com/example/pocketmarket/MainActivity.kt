package com.example.pocketmarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.pocketmarket.domain.model.CartItem
import com.example.pocketmarket.domain.model.Product
import com.example.pocketmarket.fixtures.MockProducts
import com.example.pocketmarket.fixtures.generateLargeProductList
import com.example.pocketmarket.ui.cart.CartScreen
import com.example.pocketmarket.ui.catalog.CatalogScreen
import com.example.pocketmarket.ui.catalog.CatalogUiState
import com.example.pocketmarket.ui.catalog.ViewLayoutMode
import com.example.pocketmarket.ui.detail.ProductDetailScreen
import com.example.pocketmarket.ui.theme.PocketMarketTheme

enum class Screen {
    CATALOG,
    DETAIL,
    CART
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketMarketTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by rememberSaveable { mutableStateOf(Screen.CATALOG) }
                    var selectedProduct by remember { mutableStateOf<Product?>(null) }
                    val cartItems = remember { mutableStateListOf<CartItem>() }

                    val largeList = remember { generateLargeProductList(1000) }
                    val categories = remember {
                        listOf("All") + MockProducts.items.map { it.category.value }.distinct()
                    }

                    var catalogState by remember {
                        mutableStateOf(
                            CatalogUiState(
                                products = largeList,
                                categories = categories
                            )
                        )
                    }

                    when (currentScreen) {
                        Screen.CATALOG -> {
                            CatalogScreen(
                                state = catalogState,
                                onSearchChange = { catalogState = catalogState.copy(searchQuery = it) },
                                onCategorySelect = { catalogState = catalogState.copy(selectedCategory = it) },
                                onSortOptionChange = { catalogState = catalogState.copy(sortOption = it) },
                                onToggleLayoutMode = {
                                    val next = if (catalogState.layoutMode == ViewLayoutMode.GRID) ViewLayoutMode.LIST else ViewLayoutMode.GRID
                                    catalogState = catalogState.copy(layoutMode = next)
                                },
                                onAddToCart = { product ->
                                    selectedProduct = product
                                    currentScreen = Screen.DETAIL
                                }
                            )
                        }
                        Screen.DETAIL -> {
                            selectedProduct?.let { product ->
                                ProductDetailScreen(
                                    product = product,
                                    onBackClick = { currentScreen = Screen.CATALOG },
                                    onNavigateToCart = { currentScreen = Screen.CART },
                                    onAddToCart = { prod, qty ->
                                        val existingIndex = cartItems.indexOfFirst { it.product.id == prod.id }
                                        if (existingIndex != -1) {
                                            val existing = cartItems[existingIndex]
                                            cartItems[existingIndex] = existing.copy(quantity = (existing.quantity + qty).coerceAtMost(prod.stock))
                                        } else {
                                            cartItems.add(CartItem(product = prod, quantity = qty))
                                        }
                                        currentScreen = Screen.CART
                                    }
                                )
                            }
                        }
                        Screen.CART -> {
                            CartScreen(
                                items = cartItems,
                                onBackClick = { currentScreen = Screen.CATALOG },
                                onQuantityChange = { item, newQty ->
                                    val index = cartItems.indexOf(item)
                                    if (index != -1) {
                                        cartItems[index] = item.copy(quantity = newQty)
                                    }
                                },
                                onRemoveItem = { item ->
                                    cartItems.remove(item)
                                },
                                onCheckoutClick = {
                                    // الخطوة القادمة في إدارة الطلبات
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}