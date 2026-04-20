package eu.kanade.presentation.more.shelf

import androidx.compose.runtime.Immutable

@Immutable
data class ShelfUiState(
    val featured: ShelfItem? = null,
    val manga: List<ShelfItem> = emptyList(),
    val manhwa: List<ShelfItem> = emptyList(),
    val music: List<ShelfItem> = emptyList(),
    val books: List<ShelfItem> = emptyList(),
    val loading: Boolean = false,
)
