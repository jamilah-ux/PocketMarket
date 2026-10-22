package com.example.pocketmarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.pocketmarket.fixtures.MockProducts
import com.example.pocketmarket.fixtures.generateLargeProductList
import com.example.pocketmarket.ui.catalog.CatalogScreen
import com.example.pocketmarket.ui.catalog.CatalogUiState
import com.example.pocketmarket.ui.catalog.ViewLayoutMode
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
                    val largeList = remember { generateLargeProductList(1000) }
                    val categories = remember {
                        listOf("All") + MockProducts.items.map { it.category.value }.distinct()
                    }

                    var uiState by remember {
                        mutableStateOf(
                            CatalogUiState(
                                products = largeList,
                                categories = categories
                            )
                        )
                    }

                    CatalogScreen(
                        state = uiState,
                        onSearchChange = { query ->
                            uiState = uiState.copy(searchQuery = query)
                        },
                        onCategorySelect = { category ->
                            uiState = uiState.copy(selectedCategory = category)
                        },
                        onSortOptionChange = { sort ->
                            uiState = uiState.copy(sortOption = sort)
                        },
                        onToggleLayoutMode = {
                            val newMode = if (uiState.layoutMode == ViewLayoutMode.GRID) {
                                ViewLayoutMode.LIST
                            } else {
                                ViewLayoutMode.GRID
                            }
                            uiState = uiState.copy(layoutMode = newMode)
                        },
                        onAddToCart = { product ->
                            // سيتم ربط الإضافة للسلة لاحقاً
                        }
                    )
                }
            }
        }
    }
}