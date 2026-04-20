package eu.kanade.presentation.more.shelf

class ShelfRepository {
    suspend fun getDemoState(): ShelfUiState {
        val items = listOf(
            ShelfItem(title = "Omniscient Reader", subtitle = "Manhwa", tags = listOf("apocalypse", "meta")),
            ShelfItem(title = "Goodnight Punpun", subtitle = "Manga", tags = listOf("seinen", "psychological")),
            ShelfItem(title = "Nujabes", subtitle = "Music", tags = listOf("lofi", "jazz-hop")),
            ShelfItem(title = "Kafka on the Shore", subtitle = "Book", tags = listOf("surreal", "literary")),
        )

        return ShelfUiState(
            featured = items.first(),
            manga = items.filter { it.subtitle == "Manga" },
            manhwa = items.filter { it.subtitle == "Manhwa" },
            music = items.filter { it.subtitle == "Music" },
            books = items.filter { it.subtitle == "Book" },
            loading = false,
        )
    }
}
