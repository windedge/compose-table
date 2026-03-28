package io.github.windedge.table

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun HorizontalTableScrollbar(
    scrollState: ScrollState,
    modifier: Modifier,
) {
    // Horizontal scrollbar is not shown on JS for now
}
