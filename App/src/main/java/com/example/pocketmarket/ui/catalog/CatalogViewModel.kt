package com.example.pocketmarket.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.pocketmarket.domain.model.AppError
import com.example.pocketmarket.domain.model.Product
import com.example.pocketmarket.domain.repository.ProductRepository
import com.example.pocketmarket.ui.common.UiEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CatalogAction {
    data class UpdateSearch(val query: String) : CatalogAction
    data class SelectCategory(val category: String) : CatalogAction
    data class ChangeSort(val sortOption: SortOption) : CatalogAction
    object ToggleLayout : CatalogAction
    object Refresh : CatalogAction
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState(isLoading = true))
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    private val _effect = Channel<UiEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private var fetchJob: Job? = null

    val pagedProducts: Flow<PagingData<Product>> = _uiState
        .flatMapLatest { state ->
            val searchTerms = getSearchTerms(state.searchQuery)
            repository.getPagedProducts()
                .map { pagingData ->
                    pagingData.filter { product ->
                        val matchesCategory = state.selectedCategory == "All" ||
                                product.category.value.equals(state.selectedCategory, ignoreCase = true)

                        val matchesSearch = matchesProductSearch(product, searchTerms)

                        matchesCategory && matchesSearch
                    }
                }
        }
        .cachedIn(viewModelScope)

    init {
        loadInitialData()
    }

    fun onAction(action: CatalogAction) {
        when (action) {
            is CatalogAction.UpdateSearch -> {
                _uiState.update { it.copy(searchQuery = action.query) }
                applyFilterAndSort()
            }
            is CatalogAction.SelectCategory -> {
                _uiState.update { it.copy(selectedCategory = action.category) }
                applyFilterAndSort()
            }
            is CatalogAction.ChangeSort -> {
                _uiState.update { it.copy(sortOption = action.sortOption) }
                applyFilterAndSort()
            }
            CatalogAction.ToggleLayout -> _uiState.update {
                val nextMode = if (it.layoutMode == ViewLayoutMode.GRID) ViewLayoutMode.LIST else ViewLayoutMode.GRID
                it.copy(layoutMode = nextMode)
            }
            CatalogAction.Refresh -> loadInitialData()
        }
    }

    // دالة لاستخراج مصطلحات البحث باللغة العربية والإنجليزية
    private fun getSearchTerms(rawQuery: String): List<String> {
        val query = rawQuery.trim().lowercase()
        if (query.isBlank()) return emptyList()

        val terms = mutableListOf(query)

        val translations = mapOf(
            "تفاح" to listOf("apple", "appl"),
            "تفاحة" to listOf("apple", "appl"),
            "أبل" to listOf("apple", "appl"),
            "سرير" to listOf("bed"),
            "تخت" to listOf("bed"),
            "كنبة" to listOf("sofa", "couch"),
            "اريكة" to listOf("sofa", "couch"),
            "أريكة" to listOf("sofa", "couch"),
            "صوفا" to listOf("sofa"),
            "كرسي" to listOf("chair"),
            "طاولة" to listOf("table", "desk"),
            "مكتب" to listOf("table", "desk"),
            "عطر" to listOf("perfume", "fragrance"),
            "عطور" to listOf("perfume", "fragrance"),
            "مكياج" to listOf("beauty", "makeup"),
            "تجميل" to listOf("beauty"),
            "طعام" to listOf("grocery", "groceries", "food"),
            "غذاء" to listOf("grocery", "groceries", "food"),
            "اكل" to listOf("grocery", "groceries", "food"),
            "أكل" to listOf("grocery", "groceries", "food"),
            "قطط" to listOf("cat", "pet"),
            "قطة" to listOf("cat", "pet"),
            "حيوانات" to listOf("pet")
        )

        translations.forEach { (key, list) ->
            if (query.contains(key)) {
                terms.addAll(list)
            }
        }

        return terms
    }

    private fun matchesProductSearch(product: Product, searchTerms: List<String>): Boolean {
        if (searchTerms.isEmpty()) return true

        val title = product.title.lowercase()
        val description = product.description.lowercase()
        val brand = product.brand?.lowercase() ?: ""
        val category = product.category.value.lowercase()

        return searchTerms.any { term ->
            title.contains(term) ||
                    description.contains(term) ||
                    brand.contains(term) ||
                    category.contains(term)
        }
    }

    private fun applyFilterAndSort() {
        _uiState.update { currentState ->
            val searchTerms = getSearchTerms(currentState.searchQuery)
            val categoryFilter = currentState.selectedCategory.trim().lowercase()

            val filteredList = currentState.allProducts.filter { product ->
                val matchesCategory = categoryFilter == "all" ||
                        product.category.value.trim().lowercase() == categoryFilter

                val matchesSearch = matchesProductSearch(product, searchTerms)

                matchesCategory && matchesSearch
            }

            val sortedList = when (currentState.sortOption) {
                SortOption.NAME_ASC -> filteredList.sortedBy { it.title.lowercase() }
                SortOption.PRICE_LOW_HIGH -> filteredList.sortedBy { it.price }
                SortOption.PRICE_HIGH_LOW -> filteredList.sortedByDescending { it.price }
            }

            currentState.copy(products = sortedList)
        }
    }

    private fun loadInitialData() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                repository.getProducts().collect { result ->
                    result.fold(
                        onSuccess = { fetchedProducts ->
                            val categories = listOf("All") + fetchedProducts.map { it.category.value }.distinct()
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    allProducts = fetchedProducts,
                                    products = fetchedProducts,
                                    categories = categories,
                                    errorMessage = null
                                )
                            }
                            applyFilterAndSort()
                        },
                        onFailure = { error ->
                            val displayMessage = when (error) {
                                is AppError -> error.toUserFriendlyMessage()
                                else -> error.localizedMessage ?: "حدث خطأ غير متوقع"
                            }

                            _uiState.update { it.copy(isLoading = false, errorMessage = displayMessage) }
                            _effect.send(UiEffect.ShowSnackbar(displayMessage, "إعادة المحاولة"))
                        }
                    )
                }
            } catch (e: Exception) {
                val displayMessage = (e as? AppError)?.toUserFriendlyMessage()
                    ?: "لا يوجد اتصال بالإنترنت. يرجى التحقق من الشبكة وإعادة المحاولة."
                _uiState.update { it.copy(isLoading = false, errorMessage = displayMessage) }
                _effect.send(UiEffect.ShowSnackbar(displayMessage, "إعادة المحاولة"))
            }
        }
    }
}