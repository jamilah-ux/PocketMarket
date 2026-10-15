package com.example.pocketmarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pocketmarket.fixtures.MockProducts
import com.example.pocketmarket.ui.components.PocketButton
import com.example.pocketmarket.ui.components.PocketChip
import com.example.pocketmarket.ui.components.PocketEmptyState
import com.example.pocketmarket.ui.components.PocketErrorState
import com.example.pocketmarket.ui.components.PocketLoadingState
import com.example.pocketmarket.ui.components.PocketProductCard
import com.example.pocketmarket.ui.components.PocketTextField
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
                    DesignSystemShowcase()
                }
            }
        }
    }
}

@Composable
fun DesignSystemShowcase() {
    var textValue by remember { mutableStateOf("") }
    var selectedChipIndex by remember { mutableStateOf(0) }
    val categories = listOf("All", "Smartphones", "Laptops", "Fragrances")
    val sampleProduct = MockProducts.items.first()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "PocketMarket Design System",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            // 1. Text Field
            PocketTextField(
                value = textValue,
                onValueChange = { textValue = it },
                label = "Search products..."
            )

            // 2. Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEachIndexed { index, category ->
                    PocketChip(
                        selected = selectedChipIndex == index,
                        onClick = { selectedChipIndex = index },
                        label = category
                    )
                }
            }

            // 3. Buttons
            PocketButton(
                text = "Primary Active Button",
                onClick = {}
            )

            PocketButton(
                text = "Disabled Button",
                onClick = {},
                enabled = false
            )

            // 4. Product Card
            Text(
                text = "Product Card Component",
                style = MaterialTheme.typography.titleMedium
            )
            PocketProductCard(
                product = sampleProduct,
                onAddToCartClick = {}
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // 5. Empty State Demo
            Text(
                text = "Empty State Component",
                style = MaterialTheme.typography.titleMedium
            )
            PocketEmptyState(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // 6. Error State Demo
            Text(
                text = "Error State Component",
                style = MaterialTheme.typography.titleMedium
            )
            PocketErrorState(
                errorMessage = "Custom network timeout error occurred.",
                onRetry = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // 7. Loading Indicator Demo
            Text(
                text = "Loading Component",
                style = MaterialTheme.typography.titleMedium
            )
            PocketLoadingState(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}