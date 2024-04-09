package io.github.windedge.table.material3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import compose_table.table.generated.resources.*
import io.github.windedge.table.PaginationState
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import kotlin.math.min

@OptIn(ExperimentalResourceApi::class)
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

        val start = min((pageIndex - 1) * pageSize + 1, totalCount)
        val end = (start + pageSize - 1).coerceAtMost(totalCount)

        Text("$start-$end of $totalCount")
        IconButton(onClick = { paginationState.goto(1) }, enabled = pageIndex > 1) {
            Icon(painterResource(Res.drawable.skip_left), "First")
        }
        IconButton(onClick = { paginationState.previous() }, enabled = pageIndex > 1) {
            Icon(painterResource(Res.drawable.arrow_left), "Previous")
        }
        IconButton(onClick = { paginationState.next() }, enabled = pageIndex < pageCount) {
            Icon(painterResource(Res.drawable.arrow_right), "Next")
        }
        IconButton(onClick = { paginationState.goto(pageCount) }, enabled = pageIndex < pageCount) {
            Icon(painterResource(Res.drawable.skip_right), "Last")
        }
    }
}