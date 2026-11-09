package sa.factory.ilmibootcampsmarte.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import sa.factory.ilmibootcampsmarte.domain.usecase.*
import sa.factory.ilmibootcampsmarte.model.IlmiBootcampItem
import sa.factory.ilmibootcampsmarte.model.IlmiBootcampOrder
import sa.factory.ilmibootcampsmarte.model.IlmiBootcampTelemetry
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class IlmiBootcampUiState(
    val catalog: List<IlmiBootcampItem> = emptyList(),
    val filteredCatalog: List<IlmiBootcampItem> = emptyList(),
    val selectedCategory: String = "الكل",
    val cart: List<IlmiBootcampItem> = emptyList(),
    val telemetry: IlmiBootcampTelemetry? = null,
    val lastOrder: IlmiBootcampOrder? = null,
    val isCheckingOut: Boolean = false,
    val activeTab: String = "home"
)

class IlmiBootcampViewModel(
    private val getCatalogUseCase: GetCatalogUseCase,
    private val manageCartUseCase: ManageCartUseCase,
    private val processCheckoutUseCase: ProcessCheckoutUseCase,
    private val observeHardwareTelemetryUseCase: ObserveHardwareTelemetryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(IlmiBootcampUiState())
    val uiState: StateFlow<IlmiBootcampUiState> = _uiState.asStateFlow()

    init {
        // Observe Catalog
        getCatalogUseCase().onEach { list ->
            _uiState.update { it.copy(catalog = list, filteredCatalog = list) }
        }.launchIn(viewModelScope)

        // Observe Cart
        manageCartUseCase.cart.onEach { cartItems ->
            _uiState.update { it.copy(cart = cartItems) }
        }.launchIn(viewModelScope)

        // Observe Telemetry
        observeHardwareTelemetryUseCase().onEach { telem ->
            _uiState.update { it.copy(telemetry = telem) }
        }.launchIn(viewModelScope)
    }

    fun selectCategory(category: String) {
        _uiState.update { state ->
            val filtered = if (category == "الكل") state.catalog else state.catalog.filter { it.category == category }
            state.copy(selectedCategory = category, filteredCatalog = filtered)
        }
    }

    fun switchTab(tab: String) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun addToCart(item: IlmiBootcampItem) = manageCartUseCase.add(item)
    fun removeFromCart(item: IlmiBootcampItem) = manageCartUseCase.remove(item)

    fun checkout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingOut = true) }
            val order = processCheckoutUseCase()
            _uiState.update { it.copy(isCheckingOut = false, lastOrder = order, activeTab = "checkout") }
        }
    }
}
