package eu.kanade.presentation.more.shelf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShelfViewModel(
    private val repository: ShelfRepository = ShelfRepository(),
) : ViewModel() {

    private val _state = MutableStateFlow(ShelfUiState(loading = true))
    val state: StateFlow<ShelfUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = repository.getDemoState()
        }
    }
}
