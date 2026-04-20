package eu.kanade.presentation.more.shelf

import androidx.compose.runtime.Immutable

@Immutable
data class ShelfItem(
    val title: String,
    val subtitle: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val tags: List<String> = emptyList(),
    val sourceUrl: String = "",
)
