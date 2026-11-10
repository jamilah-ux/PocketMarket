package com.example.pocketmarket.ui.navigation

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.pocketmarket.fixtures.MockProducts
import com.example.pocketmarket.ui.cart.CartAction
import com.example.pocketmarket.ui.cart.CartScreen
import com.example.pocketmarket.ui.cart.CartViewModel
import com.example.pocketmarket.ui.catalog.CatalogAction
import com.example.pocketmarket.ui.catalog.CatalogScreen
import com.example.pocketmarket.ui.catalog.CatalogViewModel
import com.example.pocketmarket.ui.checkout.CheckoutScreen
import com.example.pocketmarket.ui.checkout.OrderSuccessScreen
import com.example.pocketmarket.ui.common.UiEffect
import com.example.pocketmarket.ui.components.PocketEmptyState
import com.example.pocketmarket.ui.detail.ProductDetailScreen
import com.example.pocketmarket.ui.profile.ProfileScreen
import com.example.pocketmarket.ui.settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph(
    navController: NavHostController,
    catalogViewModel: CatalogViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val catalogState by catalogViewModel.uiState.collectAsStateWithLifecycle()
    val cartState by cartViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val totalCartCount = cartState.items.sumOf { it.quantity }
    val cartItemsMap = cartState.items.associate { it.product.id to it.quantity }

    LaunchedEffect(Unit) {
        cartViewModel.effect.collect { effect ->
            when (effect) {
                is UiEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        actionLabel = effect.actionLabel
                    )
                }
                is UiEffect.Navigate -> navController.navigate(effect.route)
            }
        }
    }

    LaunchedEffect(Unit) {
        catalogViewModel.effect.collect { effect ->
            when (effect) {
                is UiEffect.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = effect.message,
                        actionLabel = effect.actionLabel
                    )
                    if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                        catalogViewModel.onAction(CatalogAction.Refresh)
                    }
                }
                is UiEffect.Navigate -> navController.navigate(effect.route)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ScreenRoute.Catalog.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. شاشة الكتالوج
            composable(route = ScreenRoute.Catalog.route) {
                CatalogScreen(
                    state = catalogState,
                    pagedProducts = catalogViewModel.pagedProducts,
                    cartItemsMap = cartItemsMap,
                    cartItemCount = totalCartCount,
                    onSearchChange = { catalogViewModel.onAction(CatalogAction.UpdateSearch(it)) },
                    onCategorySelect = { catalogViewModel.onAction(CatalogAction.SelectCategory(it)) },
                    onSortOptionChange = { catalogViewModel.onAction(CatalogAction.ChangeSort(it)) },
                    onToggleLayoutMode = { catalogViewModel.onAction(CatalogAction.ToggleLayout) },
                    onProductClick = { product ->
                        navController.navigate(ScreenRoute.Detail.createRoute(product.id))
                    },
                    onAddToCart = { product ->
                        cartViewModel.onAction(CartAction.AddProduct(product, 1))
                    },
                    onIncreaseQuantity = { product ->
                        val currentItem = cartState.items.find { it.product.id == product.id }
                        if (currentItem != null) {
                            cartViewModel.onAction(CartAction.UpdateQuantity(currentItem, currentItem.quantity + 1))
                        } else {
                            cartViewModel.onAction(CartAction.AddProduct(product, 1))
                        }
                    },
                    onDecreaseQuantity = { product ->
                        val currentItem = cartState.items.find { it.product.id == product.id }
                        if (currentItem != null) {
                            if (currentItem.quantity > 1) {
                                cartViewModel.onAction(CartAction.UpdateQuantity(currentItem, currentItem.quantity - 1))
                            } else {
                                cartViewModel.onAction(CartAction.RemoveItem(currentItem))
                            }
                        }
                    },
                    onCartClick = {
                        navController.navigate(ScreenRoute.Cart.route)
                    },
                    onProfileClick = {
                        navController.navigate("profile")
                    },
                    onRefresh = { catalogViewModel.onAction(CatalogAction.Refresh) }
                )
            }

            // 2. شاشة تفاصيل المنتج
            composable(
                route = ScreenRoute.Detail.route,
                arguments = listOf(navArgument("productId") { type = NavType.LongType }),
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "pocketmarket://product/{productId}"
                        action = Intent.ACTION_VIEW
                    }
                )
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getLong("productId")
                val product = catalogState.products.find { it.id == productId }
                    ?: MockProducts.items.find { it.id == productId }

                if (product != null) {
                    ProductDetailScreen(
                        product = product,
                        onBackClick = { navController.popBackStack() },
                        onNavigateToCart = { navController.navigate(ScreenRoute.Cart.route) },
                        onAddToCart = { prod, qty ->
                            cartViewModel.onAction(CartAction.AddProduct(prod, qty))
                            navController.navigate(ScreenRoute.Cart.route)
                        }
                    )
                } else {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("خطأ في المنتج") },
                                navigationIcon = {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Back"
                                        )
                                    }
                                }
                            )
                        }
                    ) { p ->
                        Box(modifier = Modifier.padding(p)) {
                            PocketEmptyState(
                                title = "المنتج غير موجود",
                                description = "لم يتم العثور على المنتج برقم: $productId"
                            )
                        }
                    }
                }
            }

            // 3. شاشة سلة التسوق
            composable(route = ScreenRoute.Cart.route) {
                CartScreen(
                    items = cartState.items,
                    onBackClick = { navController.popBackStack() },
                    onQuantityChange = { item, qty ->
                        cartViewModel.onAction(CartAction.UpdateQuantity(item, qty))
                    },
                    onRemoveItem = { item ->
                        cartViewModel.onAction(CartAction.RemoveItem(item))
                    },
                    onCheckoutClick = {
                        navController.navigate("checkout")
                    }
                )
            }

            // 4. شاشة إتمام الطلب (Checkout)
            composable(route = "checkout") {
                CheckoutScreen(
                    cartItems = cartState.items,
                    onBackClick = { navController.popBackStack() },
                    onConfirmOrder = {
                        cartViewModel.onAction(CartAction.Checkout)
                        navController.navigate("order_success") {
                            popUpTo(ScreenRoute.Catalog.route) { inclusive = false }
                        }
                    }
                )
            }

            // 5. شاشة نجاح الطلب (Order Success)
            composable(route = "order_success") {
                OrderSuccessScreen(
                    onContinueShopping = {
                        navController.popBackStack(ScreenRoute.Catalog.route, false)
                    }
                )
            }

            // 6. شاشة البروفايل والطلبيات (Profile & Orders)
            composable(route = "profile") {
                ProfileScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 7. شاشة الإعدادات
            composable(route = ScreenRoute.Settings.route) {
                SettingsScreen(onBackClick = { navController.popBackStack() })
            }
        }
    }
}