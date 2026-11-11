package com.example.pocketmarket.ui.catalog

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.pocketmarket.domain.model.Product
import com.example.pocketmarket.fixtures.MockProducts
import com.example.pocketmarket.ui.common.UiEffect
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<UiEffect>()
    val effect: SharedFlow<UiEffect> = _effect.asSharedFlow()

    val pagedProducts: Flow<PagingData<Product>> = flowOf(PagingData.from(MockProducts.items))

    private val allProducts = MockProducts.items

    init {
        loadInitialData()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }

    private fun cleanCategoryString(cat: Any): String {
        val str = cat.toString()
        return str.replace("Category(value=", "").replace(")", "").trim()
    }

    private fun loadInitialData() {
        val rawCategories = allProducts.map { cleanCategoryString(it.category) }.distinct()
        val categoriesList: List<String> = listOf("all") + rawCategories
        _uiState.update {
            it.copy(
                products = allProducts,
                categories = categoriesList,
                isLoading = false
            )
        }
    }

    fun onAction(action: CatalogAction) {
        when (action) {
            is CatalogAction.UpdateSearch -> updateSearch(action.query)
            is CatalogAction.SelectCategory -> selectCategory(action.category)
            is CatalogAction.ChangeSort -> changeSort(action.sortOption)
            CatalogAction.ToggleLayout -> toggleLayout()
            CatalogAction.Refresh -> refreshCatalog()
        }
    }

    private fun updateSearch(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    private fun selectCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
        applyFilters()
    }

    private fun changeSort(sortOption: SortOption) {
        _uiState.update { it.copy(sortOption = sortOption) }
        applyFilters()
    }

    private fun toggleLayout() {
        _uiState.update {
            val nextMode = if (it.layoutMode == ViewLayoutMode.GRID) ViewLayoutMode.LIST else ViewLayoutMode.GRID
            it.copy(layoutMode = nextMode)
        }
    }

    private fun applyFilters() {
        var filtered = allProducts

        val currentCategory = _uiState.value.selectedCategory
        if (currentCategory != "all") {
            filtered = filtered.filter { cleanCategoryString(it.category).equals(currentCategory, ignoreCase = true) }
        }

        val currentQuery = _uiState.value.searchQuery.trim()
        if (currentQuery.isNotEmpty()) {
            filtered = filtered.filter {
                it.title.contains(currentQuery, ignoreCase = true) ||
                        it.description.contains(currentQuery, ignoreCase = true)
            }
        }

        filtered = when (_uiState.value.sortOption) {
            SortOption.NAME_ASC -> filtered.sortedBy { it.title }
            SortOption.PRICE_LOW_HIGH -> filtered.sortedBy { it.price }
            SortOption.PRICE_HIGH_LOW -> filtered.sortedByDescending { it.price }
        }

        _uiState.update { it.copy(products = filtered) }
    }

    fun refreshCatalog(isEnglish: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(500)

            if (isNetworkAvailable()) {
                applyFilters()
            } else {
                val errorMsg = if (isEnglish) {
                    "No internet connection. Please check your network."
                } else {
                    "لا يوجد اتصال بالإينترنت. يرجى التحقق من الشبكة."
                }
                val retryLabel = if (isEnglish) "Retry" else "إعادة المحاولة"
                _effect.emit(UiEffect.ShowSnackbar(message = errorMsg, actionLabel = retryLabel))
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}