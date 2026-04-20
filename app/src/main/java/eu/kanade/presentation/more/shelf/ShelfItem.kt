package eu.kanade.presentation.more.shelf

import androidx.compose.runtime.Immutable

@Immutable
data class ShelfItem(
    val title: String,
    val subtitle: String = "",
    val imageUrl: String = "",
    val bannerUrl: String = "",
    val description: String = "",
    val tags: List<String> = emptyList(),
    val sourceUrl: String = "",
)

@Immutable
data class ShelfUiState(
    val featured: ShelfItem? = null,
    val manga: List<ShelfItem> = emptyList(),
    val manhwa: List<ShelfItem> = emptyList(),
    val music: List<ShelfItem> = emptyList(),
    val books: List<ShelfItem> = emptyList(),
    val loading: Boolean = false,
)
