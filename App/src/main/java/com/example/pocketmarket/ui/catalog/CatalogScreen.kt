package com.example.pocketmarket.ui.catalog

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import com.example.pocketmarket.R
import com.example.pocketmarket.domain.model.Product
import com.example.pocketmarket.ui.components.PocketChip
import com.example.pocketmarket.ui.components.PocketEmptyState
import com.example.pocketmarket.ui.components.PocketGridProductCard
import com.example.pocketmarket.ui.components.PocketLoadingState
import com.example.pocketmarket.ui.components.PocketProductCard
import com.example.pocketmarket.ui.components.PocketTextField
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    state: CatalogUiState,
    pagedProducts: Flow<PagingData<Product>>,
    cartItemsMap: Map<Long, Int> = emptyMap(),
    cartItemCount: Int = 0,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onSortOptionChange: (SortOption) -> Unit,
    onToggleLayoutMode: () -> Unit,
    onProductClick: (Product) -> Unit = {},
    onAddToCart: (Product) -> Unit,
    onIncreaseQuantity: (Product) -> Unit = {},
    onDecreaseQuantity: (Product) -> Unit = {},
    onCartClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onRefresh: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState()
    val listState = rememberLazyListState()

    LaunchedEffect(state.sortOption, state.selectedCategory) {
        gridState.scrollToItem(0)
        listState.scrollToItem(0)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.catalog_title)) },
                actions = {
                    // 1. أيقونة البروفايل/الحساب الشخصي
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile"
                        )
                    }
                    // 2. أيقونة السلة
                    IconButton(onClick = onCartClick) {
                        BadgedBox(
                            badge = {
                                if (cartItemCount > 0) {
                                    Badge {
                                        Text(text = cartItemCount.toString())
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = stringResource(R.string.cart_title)
                            )
                        }
                    }
                    // 3. أيقونة التبديل بين Grid و List
                    IconButton(onClick = onToggleLayoutMode) {
                        Icon(
                            imageVector = if (state.layoutMode == ViewLayoutMode.GRID) Icons.AutoMirrored.Filled.List else Icons.Default.Menu,
                            contentDescription = "Toggle Grid or List"
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
        ) {
            PocketTextField(
                value = state.searchQuery,
                onValueChange = onSearchChange,
                label = stringResource(R.string.catalog_search_hint),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.categories) { category ->
                    val displayLabel = when (category.lowercase()) {
                        "all" -> stringResource(R.string.category_all)
                        "beauty" -> "جمال"
                        "fragrances" -> "عطور"
                        "furniture" -> "أثاث"
                        "groceries" -> "منتجات غذائية"
                        else -> category
                    }

                    PocketChip(
                        selected = category == state.selectedCategory,
                        onClick = { onCategorySelect(category) },
                        label = displayLabel
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.catalog_sort_label),
                    style = MaterialTheme.typography.labelMedium
                )
                PocketChip(
                    selected = state.sortOption == SortOption.NAME_ASC,
                    onClick = { onSortOptionChange(SortOption.NAME_ASC) },
                    label = stringResource(R.string.catalog_sort_name_asc)
                )
                PocketChip(
                    selected = state.sortOption == SortOption.PRICE_LOW_HIGH,
                    onClick = { onSortOptionChange(SortOption.PRICE_LOW_HIGH) },
                    label = stringResource(R.string.catalog_sort_price_low)
                )
                PocketChip(
                    selected = state.sortOption == SortOption.PRICE_HIGH_LOW,
                    onClick = { onSortOptionChange(SortOption.PRICE_HIGH_LOW) },
                    label = stringResource(R.string.catalog_sort_price_high)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            PullToRefreshBox(
                isRefreshing = state.isLoading,
                onRefresh = onRefresh,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when {
                    state.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            PocketLoadingState()
                        }
                    }

                    state.products.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            PocketEmptyState(
                                title = stringResource(R.string.catalog_no_results_title),
                                description = stringResource(R.string.catalog_no_results_desc)
                            )
                        }
                    }

                    state.layoutMode == ViewLayoutMode.GRID -> {
                        LazyVerticalGrid(
                            state = gridState,
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = state.products,
                                key = { it.id }
                            ) { product ->
                                val quantityInCart = cartItemsMap[product.id] ?: 0
                                PocketGridProductCard(
                                    product = product,
                                    quantityInCart = quantityInCart,
                                    onProductClick = { onProductClick(product) },
                                    onIncreaseQuantity = { onIncreaseQuantity(product) },
                                    onDecreaseQuantity = { onDecreaseQuantity(product) },
                                    onAddToCartClick = { onAddToCart(product) }
                                )
                            }
                        }
                    }

                    else -> {
                        LazyColumn(
                            state = listState,
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = state.products,
                                key = { it.id }
                            ) { product ->
                                val quantityInCart = cartItemsMap[product.id] ?: 0
                                PocketProductCard(
                                    product = product,
                                    quantityInCart = quantityInCart,
                                    onProductClick = { onProductClick(product) },
                                    onIncreaseQuantity = { onIncreaseQuantity(product) },
                                    onDecreaseQuantity = { onDecreaseQuantity(product) },
                                    onAddToCartClick = { onAddToCart(product) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}