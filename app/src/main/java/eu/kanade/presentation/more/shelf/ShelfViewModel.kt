package eu.kanade.presentation.more.shelf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShelfViewModel(
    private val repository: ShelfRepository = ShelfRepository(),
    private val aniListUser: String = "G1NYU",
    private val lastFmApiKey: String = "",
    private val lastFmUser: String = "G1NYU",
) : ViewModel() {

    private val _state = MutableStateFlow(ShelfUiState(loading = true))
    val state: StateFlow<ShelfUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = ShelfUiState(loading = true)

            val aniState = repository.getAniListShelf(aniListUser)
            val music = repository.getLastFmTop(lastFmApiKey, lastFmUser)

            _state.value = aniState.copy(
                music = music,
                loading = false,
            )
        }
    }
}
