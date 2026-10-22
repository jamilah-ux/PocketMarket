package com.example.pocketmarket.ui.navigation

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.pocketmarket.domain.model.CartItem
import com.example.pocketmarket.domain.model.Product
import com.example.pocketmarket.ui.cart.CartScreen
import com.example.pocketmarket.ui.catalog.CatalogScreen
import com.example.pocketmarket.ui.catalog.CatalogUiState
import com.example.pocketmarket.ui.catalog.SortOption
import com.example.pocketmarket.ui.catalog.ViewLayoutMode
import com.example.pocketmarket.ui.components.PocketEmptyState
import com.example.pocketmarket.ui.detail.ProductDetailScreen
import com.example.pocketmarket.ui.settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph(
    navController: NavHostController,
    products: List<Product>,
    cartItems: SnapshotStateList<CartItem>,
    catalogState: CatalogUiState,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onSortChange: (SortOption) -> Unit,
    onToggleLayout: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.Catalog.route,
        modifier = modifier
    ) {
        // شاشة الكتالوج
        composable(route = ScreenRoute.Catalog.route) {
            CatalogScreen(
                state = catalogState,
                onSearchChange = onSearchChange,
                onCategorySelect = onCategorySelect,
                onSortOptionChange = onSortChange,
                onToggleLayoutMode = onToggleLayout,
                onAddToCart = { product ->
                    navController.navigate(ScreenRoute.Detail.createRoute(product.id))
                }
            )
        }

        // شاشة تفاصيل المنتج مع دعم الـ Deep Link
        composable(
            route = ScreenRoute.Detail.route,
            arguments = listOf(
                navArgument("productId") { type = NavType.LongType }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "pocketmarket://product/{productId}"
                    action = Intent.ACTION_VIEW
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getLong("productId")
            val product = products.find { it.id == productId }

            if (product != null) {
                ProductDetailScreen(
                    product = product,
                    onBackClick = { navController.popBackStack() },
                    onNavigateToCart = { navController.navigate(ScreenRoute.Cart.route) },
                    onAddToCart = { prod, qty ->
                        val index = cartItems.indexOfFirst { it.product.id == prod.id }
                        if (index != -1) {
                            val existing = cartItems[index]
                            cartItems[index] = existing.copy(
                                quantity = (existing.quantity + qty).coerceAtMost(prod.stock)
                            )
                        } else {
                            cartItems.add(CartItem(prod, qty))
                        }
                        navController.navigate(ScreenRoute.Cart.route)
                    }
                )
            } else {
                // Fallback عند إدخال معرف غير صالح أو منتج مفقود
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("خطأ في المنتج") },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                                }
                            }
                        )
                    }
                ) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        PocketEmptyState(
                            title = "المنتج غير موجود",
                            description = "لم نتمكن من العثور على المنتج المطلوب برقم المعرف: $productId"
                        )
                    }
                }
            }
        }

        // شاشة السلة
        composable(route = ScreenRoute.Cart.route) {
            CartScreen(
                items = cartItems,
                onBackClick = { navController.popBackStack() },
                onQuantityChange = { item, qty ->
                    val index = cartItems.indexOf(item)
                    if (index != -1) {
                        cartItems[index] = item.copy(quantity = qty)
                    }
                },
                onRemoveItem = { item -> cartItems.remove(item) },
                onCheckoutClick = { }
            )
        }

        // شاشة الإعدادات
        composable(route = ScreenRoute.Settings.route) {
            SettingsScreen(onBackClick = { navController.popBackStack() })
        }
    }
}