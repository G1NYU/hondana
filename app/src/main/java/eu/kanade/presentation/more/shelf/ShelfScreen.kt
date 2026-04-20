package eu.kanade.presentation.more.shelf

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShelfScreen(
    state: ShelfUiState,
    onRefresh: () -> Unit = {},
) {
    var selectedItem by remember { mutableStateOf<ShelfItem?>(null) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val backdropUrl = selectedItem?.bannerUrl?.takeIf { it.isNotBlank() }
        ?: selectedItem?.imageUrl?.takeIf { it.isNotBlank() }
        ?: state.featured?.bannerUrl?.takeIf { it.isNotBlank() }
        ?: state.featured?.imageUrl

    Box(modifier = Modifier.fillMaxSize()) {

        // Cinematic blurred backdrop
        AnimatedVisibility(
            visible = !backdropUrl.isNullOrBlank(),
            enter = fadeIn(tween(800)),
            exit = fadeOut(tween(800)),
        ) {
            AsyncImage(
                model = backdropUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(32.dp),
            )
        }

        // Dark scrim
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.75f),
                            Color.Black.copy(alpha = 0.90f),
                        ),
                    ),
                ),
        )

        if (state.loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp),
            ) {
                item { ShelfHero(item = state.featured, onRefresh = onRefresh) }
                if (state.manhwa.isNotEmpty()) item { ShelfRow(title = "Manhwa", items = state.manhwa, onItemClick = { selectedItem = it }) }
                if (state.manga.isNotEmpty()) item { ShelfRow(title = "Manga", items = state.manga, onItemClick = { selectedItem = it }) }
                if (state.music.isNotEmpty()) item { ShelfRow(title = "Music", items = state.music, onItemClick = { selectedItem = it }) }
                if (state.books.isNotEmpty()) item { ShelfRow(title = "Books", items = state.books, onItemClick = { selectedItem = it }) }
            }
        }

        if (selectedItem != null) {
            ModalBottomSheet(
                onDismissRequest = { selectedItem = null },
                sheetState = bottomSheetState,
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
            ) {
                ShelfSpotlightSheet(item = selectedItem!!)
            }
        }
    }
}

@Composable
private fun ShelfHero(item: ShelfItem?, onRefresh: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(16.dp),
        contentAlignment = Alignment.BottomStart,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "NEO-OTAKU ARCHIVE",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                letterSpacing = TextUnit(3f, TextUnitType.Sp),
            )
            Text(
                text = "G1NYU Shelf",
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Black),
                color = Color.White,
            )
            if (item != null) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.65f),
                )
            }
        }
        IconButton(
            onClick = onRefresh,
            modifier = Modifier.align(Alignment.TopEnd),
        ) {
            Icon(
                imageVector = Icons.Outlined.Refresh,
                contentDescription = "Refresh shelf",
                tint = Color.White.copy(alpha = 0.7f),
            )
        }
    }
}

@Composable
private fun ShelfRow(
    title: String,
    items: List<ShelfItem>,
    onItemClick: (ShelfItem) -> Unit,
) {
    Column(
        modifier = Modifier.padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(items) { item ->
                ShelfCard(item = item, onClick = { onItemClick(item) })
            }
        }
    }
}

@Composable
private fun ShelfCard(item: ShelfItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Box {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.72f))),
                        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    ),
            )
        }
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.6f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun ShelfSpotlightSheet(item: ShelfItem) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        )
        if (item.subtitle.isNotBlank()) {
            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (item.description.isNotBlank()) {
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (item.tags.isNotEmpty()) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(item.tags) { tag ->
                    SuggestionChip(
                        onClick = {},
                        label = { Text(tag, style = MaterialTheme.typography.labelSmall) },
                    )
                }
            }
        }
    }
}
