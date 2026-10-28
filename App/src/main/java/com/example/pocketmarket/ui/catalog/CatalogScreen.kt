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
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
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
    onSearchChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onSortOptionChange: (SortOption) -> Unit,
    onToggleLayoutMode: () -> Unit,
    onAddToCart: (Product) -> Unit,
    onRefresh: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val lazyPagingItems = pagedProducts.collectAsLazyPagingItems()
    val isRefreshing = lazyPagingItems.loadState.refresh is LoadState.Loading

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

            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {
                    onRefresh()
                    lazyPagingItems.refresh()
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when {
                    // التحميل الأولي الكامل
                    lazyPagingItems.loadState.refresh is LoadState.Loading && lazyPagingItems.itemCount == 0 -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            PocketLoadingState()
                        }
                    }

                    // خطأ التحميل الأولي
                    lazyPagingItems.loadState.refresh is LoadState.Error && lazyPagingItems.itemCount == 0 -> {
                        val error = (lazyPagingItems.loadState.refresh as LoadState.Error).error
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = error.localizedMessage ?: "حدث خطأ أثناء تحميل البيانات",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Button(onClick = { lazyPagingItems.retry() }) {
                                    Text("إعادة المحاولة")
                                }
                            }
                        }
                    }

                    // قائمة فارغة بعد انتهاء التحميل
                    lazyPagingItems.loadState.refresh is LoadState.NotLoading && lazyPagingItems.itemCount == 0 -> {
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

                    // وضع الشبكة (Grid Mode)
                    state.layoutMode == ViewLayoutMode.GRID -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                count = lazyPagingItems.itemCount,
                                key = lazyPagingItems.itemKey { it.id }
                            ) { index ->
                                val product = lazyPagingItems[index]
                                if (product != null) {
                                    PocketGridProductCard(
                                        product = product,
                                        onAddToCartClick = { onAddToCart(product) }
                                    )
                                }
                            }

                            // مؤشر تحميل أو خطأ عند الوصول لأسفل القائمة (Append)
                            when (val appendState = lazyPagingItems.loadState.append) {
                                is LoadState.Loading -> {
                                    item(span = { GridItemSpan(maxLineSpan) }) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                }
                                is LoadState.Error -> {
                                    item(span = { GridItemSpan(maxLineSpan) }) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "فشل تحميل المزيد من المنتجات",
                                                color = MaterialTheme.colorScheme.error
                                            )
                                            Button(onClick = { lazyPagingItems.retry() }) {
                                                Text("إعادة المحاولة")
                                            }
                                        }
                                    }
                                }
                                else -> Unit
                            }
                        }
                    }

                    // وضع القائمة (List Mode)
                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                count = lazyPagingItems.itemCount,
                                key = lazyPagingItems.itemKey { it.id }
                            ) { index ->
                                val product = lazyPagingItems[index]
                                if (product != null) {
                                    PocketProductCard(
                                        product = product,
                                        onAddToCartClick = { onAddToCart(product) }
                                    )
                                }
                            }

                            // مؤشر تحميل أو خطأ عند الوصول لأسفل القائمة (Append)
                            when (val appendState = lazyPagingItems.loadState.append) {
                                is LoadState.Loading -> {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                }
                                is LoadState.Error -> {
                                    item {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "فشل تحميل المزيد من المنتجات",
                                                color = MaterialTheme.colorScheme.error
                                            )
                                            Button(onClick = { lazyPagingItems.retry() }) {
                                                Text("إعادة المحاولة")
                                            }
                                        }
                                    }
                                }
                                else -> Unit
                            }
                        }
                    }
                }
            }
        }
    }
}