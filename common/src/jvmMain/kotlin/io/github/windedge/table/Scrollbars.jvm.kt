package io.github.windedge.table

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun HorizontalTableScrollbar(
    scrollState: ScrollState,
    modifier: Modifier,
) {
    val adapter = rememberScrollbarAdapter(scrollState)
    HorizontalScrollbar(
        adapter = adapter,
        modifier = modifier,
    )
}
