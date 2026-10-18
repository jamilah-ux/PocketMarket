package com.example.pocketmarket.ui.home

import android.content.res.Configuration
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pocketmarket.R
import com.example.pocketmarket.domain.model.Product
import com.example.pocketmarket.fixtures.MockProducts
import com.example.pocketmarket.ui.components.FeaturedBanner
import com.example.pocketmarket.ui.components.PocketChip
import com.example.pocketmarket.ui.components.PocketEmptyState
import com.example.pocketmarket.ui.components.PocketLoadingState
import com.example.pocketmarket.ui.components.PocketProductCard
import com.example.pocketmarket.ui.components.PocketTextField
import com.example.pocketmarket.ui.theme.PocketMarketTheme

sealed interface HomeUiState {
    object Loading : HomeUiState
    object Empty : HomeUiState
    data class Content(
        val products: List<Product>,
        val categories: List<String>,
        val selectedCategory: String
    ) : HomeUiState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onCategorySelected: (String) -> Unit = {},
    onProductClick: (Product) -> Unit = {},
    onAddToCartClick: (Product) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.home_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { /* Navigate to cart in future tasks */ }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Cart",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is HomeUiState.Loading -> PocketLoadingState()
                is HomeUiState.Empty -> PocketEmptyState()
                is HomeUiState.Content -> {
                    var searchQuery by remember { mutableStateOf("") }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // 1. Search Bar
                        item {
                            PocketTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                label = stringResource(R.string.home_search_hint)
                            )
                        }

                        // 2. Featured Promotional Banner
                        item {
                            FeaturedBanner()
                        }

                        // 3. Categories Row
                        item {
                            Column {
                                Text(
                                    text = stringResource(R.string.home_categories_title),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(uiState.categories) { category ->
                                        PocketChip(
                                            selected = category == uiState.selectedCategory,
                                            onClick = { onCategorySelected(category) },
                                            label = category
                                        )
                                    }
                                }
                            }
                        }

                        // 4. Featured Products Section Title
                        item {
                            Text(
                                text = stringResource(R.string.home_featured_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // 5. Products List
                        val filteredProducts = if (searchQuery.isBlank()) {
                            uiState.products
                        } else {
                            uiState.products.filter {
                                it.title.contains(searchQuery, ignoreCase = true)
                            }
                        }

                        items(filteredProducts, key = { it.id }) { product ->
                            PocketProductCard(
                                product = product,
                                onAddToCartClick = { onAddToCartClick(product) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- Previews (Meeting Acceptance Criteria: Content, Loading, Empty & Dark Mode) ---

private val sampleCategories = listOf("All", "Smartphones", "Laptops", "Fragrances", "Skincare")

@Preview(name = "Home Screen - Light Mode", showBackground = true)
@Preview(name = "Home Screen - Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewHomeScreenContent() {
    PocketMarketTheme {
        Surface {
            HomeScreen(
                uiState = HomeUiState.Content(
                    products = MockProducts.items.take(5),
                    categories = sampleCategories,
                    selectedCategory = "All"
                )
            )
        }
    }
}

@Preview(name = "Home Screen - Loading", showBackground = true)
@Composable
private fun PreviewHomeScreenLoading() {
    PocketMarketTheme {
        Surface {
            HomeScreen(uiState = HomeUiState.Loading)
        }
    }
}

@Preview(name = "Home Screen - Empty", showBackground = true)
@Composable
private fun PreviewHomeScreenEmpty() {
    PocketMarketTheme {
        Surface {
            HomeScreen(uiState = HomeUiState.Empty)
        }
    }
}