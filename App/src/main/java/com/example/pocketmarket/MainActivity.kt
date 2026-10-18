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
import com.example.pocketmarket.ui.home.HomeScreen
import com.example.pocketmarket.ui.home.HomeUiState
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
                    val allCategories = remember {
                        listOf("All") + MockProducts.items.map { it.category.toString() }.distinct()
                    }
                    var selectedCategory by remember { mutableStateOf("All") }

                    val displayedProducts = remember(selectedCategory) {
                        if (selectedCategory == "All") {
                            MockProducts.items
                        } else {
                            MockProducts.items.filter { it.category.toString() == selectedCategory }
                        }
                    }

                    HomeScreen(
                        uiState = HomeUiState.Content(
                            products = displayedProducts,
                            categories = allCategories,
                            selectedCategory = selectedCategory
                        ),
                        onCategorySelected = { category ->
                            selectedCategory = category
                        },
                        onAddToCartClick = {
                            // سيتم ربط سلة المشتريات في المهام القادمة
                        }
                    )
                }
            }
        }
    }
}