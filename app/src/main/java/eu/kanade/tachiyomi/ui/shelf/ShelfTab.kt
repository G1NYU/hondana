package eu.kanade.tachiyomi.ui.shelf

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.tab.TabOptions
import eu.kanade.presentation.more.shelf.ShelfRepository
import eu.kanade.presentation.more.shelf.ShelfScreen
import eu.kanade.presentation.more.shelf.ShelfUiState
import eu.kanade.presentation.util.Tab
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tachiyomi.core.common.util.lang.launchIO
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import okhttp3.OkHttpClient

data object ShelfTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Outlined.CollectionsBookmark)
            return remember {
                TabOptions(
                    index = 5u,
                    title = "Shelf",
                    icon = icon,
                )
            }
        }

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { ShelfScreenModel() }
        val state by screenModel.state.collectAsState()
        ShelfScreen(
            state = state,
            onRefresh = screenModel::refresh,
        )
    }
}

class ShelfScreenModel(
    // Reuse Mihon's shared OkHttpClient via Injekt
    private val repository: ShelfRepository = ShelfRepository(
        client = Injekt.get<OkHttpClient>(),
    ),
    // TODO: wire these to a real ShelfPreferences once added to settings
    private val aniListUser: String = "G1NYU",
    private val lastFmApiKey: String = "",
    private val lastFmUser: String = "G1NYU",
    private val bookIsbns: List<String> = listOf(
        "9780811204816",
        "9781400079273",
        "9780802128251",
        "9780811200320",
    ),
) : ScreenModel {

    private val _state = MutableStateFlow(ShelfUiState(loading = true))
    val state: StateFlow<ShelfUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        screenModelScope.launchIO {
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
