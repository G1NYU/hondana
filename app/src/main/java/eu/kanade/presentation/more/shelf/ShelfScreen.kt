package eu.kanade.presentation.more.shelf

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val demoItems = listOf(
    "Omniscient Reader",
    "The Horizon",
    "Goodnight Punpun",
    "NANA OST energy",
    "Kafka on the Shore",
)

@Composable
fun ShelfScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = "Shelf",
            style = MaterialTheme.typography.headlineMedium,
        )

        Text(
            text = "A cinematic archive for manga, manhwa, books, and music.",
            style = MaterialTheme.typography.bodyMedium,
        )

        ShelfRow(title = "Favorites", items = demoItems)
        ShelfRow(title = "Manhwa", items = demoItems)
        ShelfRow(title = "Music", items = demoItems)
        ShelfRow(title = "Books", items = demoItems)
    }
}

@Composable
private fun ShelfRow(title: String, items: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(items) { item ->
                Text(
                    text = item,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
