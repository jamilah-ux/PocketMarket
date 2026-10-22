package com.example.pocketmarket.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketmarket.domain.model.Product
import com.example.pocketmarket.domain.repository.ProductRepository
import com.example.pocketmarket.domain.repository.ProductRepositoryImpl
import com.example.pocketmarket.fixtures.MockProducts
import com.example.pocketmarket.ui.common.UiEffect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// 1. الإجراءات القادمة من المستخدم (User Actions)
sealed interface CatalogAction {
    data class UpdateSearch(val query: String) : CatalogAction
    data class SelectCategory(val category: String) : CatalogAction
    data class ChangeSort(val sortOption: SortOption) : CatalogAction
    object ToggleLayout : CatalogAction
    object Refresh : CatalogAction
}

// 2. حالة الشاشة غير القابلة للتعديل من الخارج (Immutable UiState)
data class CatalogState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val categories: List<String> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val sortOption: SortOption = SortOption.NAME_ASC,
    val layoutMode: ViewLayoutMode = ViewLayoutMode.GRID,
    val errorMessage: String? = null
)

class CatalogViewModel(
    private val repository: ProductRepository = ProductRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogState(isLoading = true))
    val uiState: StateFlow<CatalogState> = _uiState.asStateFlow()

    // قناة الأحداث لضمان عدم تكرارها عند الـ Recomposition (Task 10)
    private val _effect = Channel<UiEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        loadInitialData()
    }

    fun onAction(action: CatalogAction) {
        when (action) {
            is CatalogAction.UpdateSearch -> _uiState.update { it.copy(searchQuery = action.query) }
            is CatalogAction.SelectCategory -> _uiState.update { it.copy(selectedCategory = action.category) }
            is CatalogAction.ChangeSort -> _uiState.update { it.copy(sortOption = action.sortOption) }
            CatalogAction.ToggleLayout -> _uiState.update {
                val nextMode = if (it.layoutMode == ViewLayoutMode.GRID) ViewLayoutMode.LIST else ViewLayoutMode.GRID
                it.copy(layoutMode = nextMode)
            }
            CatalogAction.Refresh -> loadInitialData()
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.getProducts().collect { result ->
                result.fold(
                    onSuccess = { allProducts ->
                        val categories = listOf("All") + MockProducts.items.map { it.category.value }.distinct()
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                products = allProducts,
                                categories = categories
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = error.localizedMessage ?: "حدث خطأ غير متوقع"
                            )
                        }
                        _effect.send(UiEffect.ShowSnackbar("تعذر تحديث المنتجات", "إعادة المحاولة"))
                    }
                )
            }
        }
    }
}