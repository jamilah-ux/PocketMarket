package com.example.pocketmarket.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketmarket.domain.model.AppError
import com.example.pocketmarket.domain.repository.ProductRepository
import com.example.pocketmarket.ui.common.UiEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState(isLoading = true))
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    private val _effect = Channel<UiEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private var fetchJob: Job? = null

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
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }
            try {
                repository.getProducts().collect { result ->
                    result.fold(
                        onSuccess = { allProducts ->
                            val categories = listOf("All") + allProducts.map { it.category.value }.distinct()
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    products = allProducts,
                                    categories = categories,
                                    errorMessage = null
                                )
                            }
                        },
                        onFailure = { error ->
                            val displayMessage = when (error) {
                                is AppError -> error.toUserFriendlyMessage()
                                else -> error.localizedMessage ?: "حدث خطأ غير متوقع"
                            }

                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = displayMessage
                                )
                            }

                            _effect.send(UiEffect.ShowSnackbar(displayMessage, "إعادة المحاولة"))
                        }
                    )
                }
            } catch (e: Exception) {
                val displayMessage = (e as? AppError)?.toUserFriendlyMessage()
                    ?: "لا يوجد اتصال بالإنترنت. يرجى التحقق من الشبكة وإعادة المحاولة."
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = displayMessage
                    )
                }
                _effect.send(UiEffect.ShowSnackbar(displayMessage, "إعادة المحاولة"))
            }
        }
    }
}