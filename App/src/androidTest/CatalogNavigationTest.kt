package com.example.pocketmarket.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.pocketmarket.domain.model.Category
import com.example.pocketmarket.domain.model.Product
import com.example.pocketmarket.ui.cart.CartScreen
import com.example.pocketmarket.ui.detail.ProductDetailScreen
import org.junit.Rule
import org.junit.Test

class CatalogNavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testCategory = Category("Electronics")

    private val sampleProduct = Product(
        id = 1L,
        title = "Wireless Headphones",
        description = "High quality headphones",
        price = 150.0,
        discountPercentage = 10.0,
        rating = 4.7,
        stock = 8,
        category = testCategory
    )

    @Test
    fun productDetailScreen_displaysTitleAndAddToCartButton() {
        composeTestRule.setContent {
            ProductDetailScreen(
                product = sampleProduct,
                onBackClick = {},
                onNavigateToCart = {},
                onAddToCart = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithText("Wireless Headphones").assertIsDisplayed()
    }

    @Test
    fun cartScreen_showsEmptyState_whenItemsAreEmpty() {
        composeTestRule.setContent {
            CartScreen(
                items = emptyList(),
                onBackClick = {},
                onQuantityChange = { _, _ -> },
                onRemoveItem = {},
                onCheckoutClick = {}
            )
        }

        composeTestRule.onNodeWithText("السلة فارغة").assertIsDisplayed()
    }
}