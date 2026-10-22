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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.pocketmarket.R
import com.example.pocketmarket.domain.model.Product
import com.example.pocketmarket.ui.components.PocketChip
import com.example.pocketmarket.ui.components.PocketEmptyState
import com.example.pocketmarket.ui.components.PocketGridProductCard
import com.example.pocketmarket.ui.components.PocketLoadingState
import com.example.pocketmarket.ui.components.PocketProductCard
import com.example.pocketmarket.ui.components.PocketTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    state: CatalogUiState,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onSortOptionChange: (SortOption) -> Unit,
    onToggleLayoutMode: () -> Unit,
    onAddToCart: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredAndSortedProducts by remember(state) {
        derivedStateOf {
            state.products
                .filter { product ->
                    val matchesCategory = state.selectedCategory == "All" ||
                            product.category.value.contains(state.selectedCategory, ignoreCase = true)
                    val matchesSearch = state.searchQuery.isBlank() ||
                            product.title.contains(state.searchQuery, ignoreCase = true) ||
                            product.description.contains(state.searchQuery, ignoreCase = true)
                    matchesCategory && matchesSearch
                }
                .sortedWith { p1, p2 ->
                    when (state.sortOption) {
                        SortOption.NAME_ASC -> p1.title.compareTo(p2.title, ignoreCase = true)
                        SortOption.PRICE_LOW_HIGH -> p1.discountedPrice.compareTo(p2.discountedPrice)
                        SortOption.PRICE_HIGH_LOW -> p2.discountedPrice.compareTo(p1.discountedPrice)
                    }
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.catalog_title)) },
                actions = {
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
                    PocketChip(
                        selected = category == state.selectedCategory,
                        onClick = { onCategorySelect(category) },
                        label = category
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
                    label = "A-Z"
                )
                PocketChip(
                    selected = state.sortOption == SortOption.PRICE_LOW_HIGH,
                    onClick = { onSortOptionChange(SortOption.PRICE_LOW_HIGH) },
                    label = "Price: Min"
                )
                PocketChip(
                    selected = state.sortOption == SortOption.PRICE_HIGH_LOW,
                    onClick = { onSortOptionChange(SortOption.PRICE_HIGH_LOW) },
                    label = "Price: Max"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            when {
                state.isLoading -> {
                    PocketLoadingState()
                }
                filteredAndSortedProducts.isEmpty() -> {
                    PocketEmptyState(
                        title = stringResource(R.string.catalog_no_results_title),
                        description = stringResource(R.string.catalog_no_results_desc)
                    )
                }
                state.layoutMode == ViewLayoutMode.GRID -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredAndSortedProducts, key = { it.id }) { product ->
                            PocketGridProductCard(
                                product = product,
                                onAddToCartClick = { onAddToCart(product) }
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredAndSortedProducts, key = { it.id }) { product ->
                            PocketProductCard(
                                product = product,
                                onAddToCartClick = { onAddToCart(product) }
                            )
                        }
                    }
                }
            }
        }
    }
}