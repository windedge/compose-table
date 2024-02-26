package io.github.windedge.table.material

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FirstPage
import androidx.compose.material.icons.filled.LastPage
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.windedge.table.PaginationState

@Composable
fun Paginator(
    paginationState: PaginationState,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
) {
    Row(modifier, horizontalArrangement, verticalAlignment) {
        val pageIndex = paginationState.pageIndex
        val pageCount = paginationState.pageCount
        val totalCount = paginationState.totalCount
        val pageSize = paginationState.pageSize

        val start = (pageIndex - 1) * pageSize + 1
        val end = (start + pageSize - 1).coerceAtMost(totalCount)

        Text("$start-$end of $totalCount")
        IconButton(onClick = { paginationState.goto(1) }, enabled = pageIndex > 1) {
            Icon(Icons.Default.FirstPage, "First")
        }
        IconButton(onClick = { paginationState.previous() }, enabled = pageIndex > 1) {
            Icon(Icons.Default.ChevronLeft, "Previous")
        }
        IconButton(onClick = { paginationState.next() }, enabled = pageIndex < pageCount) {
            Icon(Icons.Default.ChevronRight, "Next")
        }
        IconButton(onClick = { paginationState.goto(pageCount) }, enabled = pageIndex < pageCount) {
            Icon(Icons.Default.LastPage, "Last")
        }
    }
}
