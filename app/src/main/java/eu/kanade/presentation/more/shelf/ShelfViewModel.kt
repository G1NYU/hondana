package eu.kanade.presentation.more.shelf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShelfViewModel(
    private val repository: ShelfRepository = ShelfRepository(),
    // Replace these with values from your app's preferences/settings
    private val aniListUser: String = "G1NYU",
    private val lastFmApiKey: String = "",
    private val lastFmUser: String = "G1NYU",
    private val bookIsbns: List<String> = listOf(
        "9780811204816", // No Longer Human
        "9781400079273", // Kafka on the Shore
        "9780802128251", // Convenience Store Woman
        "9780811200320", // The Setting Sun
    ),
) : ViewModel() {

    private val _state = MutableStateFlow(ShelfUiState(loading = true))
    val state: StateFlow<ShelfUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = ShelfUiState(loading = true)

            val aniDeferred = async { repository.getAniListShelf(aniListUser) }
            val musicDeferred = async { repository.getLastFmTop(lastFmApiKey, lastFmUser) }
            val booksDeferred = async { repository.getBooks(bookIsbns) }

            val aniState = aniDeferred.await()
            val music = musicDeferred.await()
            val books = booksDeferred.await()

            _state.value = aniState.copy(
                music = music,
                books = books,
                loading = false,
            )
        }
    }
}
