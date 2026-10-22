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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.pocketmarket.domain.model.CartItem
import com.example.pocketmarket.fixtures.MockProducts
import com.example.pocketmarket.fixtures.generateLargeProductList
import com.example.pocketmarket.ui.catalog.CatalogUiState
import com.example.pocketmarket.ui.catalog.ViewLayoutMode
import com.example.pocketmarket.ui.navigation.AppNavGraph
import com.example.pocketmarket.ui.theme.PocketMarketTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketMarketTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val cartItems = remember { mutableStateListOf<CartItem>() }

                    val allProducts = remember { generateLargeProductList(1000) }
                    val categories = remember {
                        listOf("All") + MockProducts.items.map { it.category.value }.distinct()
                    }

                    var catalogState by remember {
                        mutableStateOf(
                            CatalogUiState(
                                products = allProducts,
                                categories = categories
                            )
                        )
                    }

                    AppNavGraph(
                        navController = navController,
                        products = allProducts,
                        cartItems = cartItems,
                        catalogState = catalogState,
                        onSearchChange = { catalogState = catalogState.copy(searchQuery = it) },
                        onCategorySelect = { catalogState = catalogState.copy(selectedCategory = it) },
                        onSortChange = { catalogState = catalogState.copy(sortOption = it) },
                        onToggleLayout = {
                            val next = if (catalogState.layoutMode == ViewLayoutMode.GRID) {
                                ViewLayoutMode.LIST
                            } else {
                                ViewLayoutMode.GRID
                            }
                            catalogState = catalogState.copy(layoutMode = next)
                        }
                    )
                }
            }
        }
    }
}