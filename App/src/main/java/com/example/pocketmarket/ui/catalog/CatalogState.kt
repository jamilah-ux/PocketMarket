package com.example.pocketmarket.ui.catalog

import com.example.pocketmarket.domain.model.Product

enum class ViewLayoutMode {
    GRID, LIST
}

enum class SortOption {
    NAME_ASC, PRICE_LOW_HIGH, PRICE_HIGH_LOW
}

data class CatalogUiState(
    val products: List<Product> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String = "all",
    val searchQuery: String = "",
    val sortOption: SortOption = SortOption.NAME_ASC,
    val layoutMode: ViewLayoutMode = ViewLayoutMode.GRID,
    val isLoading: Boolean = false
)

sealed interface CatalogAction {
    data class UpdateSearch(val query: String) : CatalogAction
    data class SelectCategory(val category: String) : CatalogAction
    data class ChangeSort(val sortOption: SortOption) : CatalogAction
    data object ToggleLayout : CatalogAction
    data object Refresh : CatalogAction
}