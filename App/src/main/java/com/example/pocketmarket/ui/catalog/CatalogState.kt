package com.example.pocketmarket.ui.catalog

import com.example.pocketmarket.domain.model.Product

enum class SortOption {
    NAME_ASC,
    PRICE_LOW_HIGH,
    PRICE_HIGH_LOW
}

enum class ViewLayoutMode {
    LIST,
    GRID
}

data class CatalogUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val categories: List<String> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val sortOption: SortOption = SortOption.NAME_ASC,
    val layoutMode: ViewLayoutMode = ViewLayoutMode.GRID,
    val errorMessage: String? = null

)