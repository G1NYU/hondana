package eu.kanade.tachiyomi.ui.shelf

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import eu.kanade.presentation.more.shelf.ShelfScreen

object ShelfTab : Tab {

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
        ShelfScreen()
    }
}
