package com.example.pocketmarket.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pocketmarket.domain.model.Product

@Composable
fun AdaptiveHomeScreen(
    windowWidthSizeClass: WindowWidthSizeClass,
    products: List<Product>,
    selectedProduct: Product?,
    onProductSelect: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    when (windowWidthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            ProductGridScreen(
                products = products,
                columnsCount = 2,
                onProductSelect = onProductSelect,
                modifier = modifier
            )
        }
        WindowWidthSizeClass.Medium -> {
            ProductGridScreen(
                products = products,
                columnsCount = 3,
                onProductSelect = onProductSelect,
                modifier = modifier
            )
        }
        WindowWidthSizeClass.Expanded -> {
            Row(modifier = modifier.fillMaxSize()) {
                ProductGridScreen(
                    products = products,
                    columnsCount = 2,
                    onProductSelect = onProductSelect,
                    modifier = Modifier.weight(1f)
                )

                selectedProduct?.let { product ->
                    ProductDetailPane(
                        product = product,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun ProductGridScreen(
    products: List<Product>,
    columnsCount: Int,
    onProductSelect: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnsCount),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(products, key = { it.id }) { product ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProductSelect(product) }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = product.title, style = MaterialTheme.typography.titleMedium)
                    Text(text = "$${product.discountedPrice}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun ProductDetailPane(
    product: Product,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = product.title, style = MaterialTheme.typography.headlineMedium)
        Text(text = "$${product.discountedPrice}", style = MaterialTheme.typography.titleLarge)
        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}