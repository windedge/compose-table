package io.github.windedge.table.m3

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.windedge.table.BasicPaginatedDataTable
import io.github.windedge.table.ColumnBuilder
import io.github.windedge.table.PaginationRowBuilder
import io.github.windedge.table.PaginationState
import io.github.windedge.table.components.Divider
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Composable
fun <T> PaginatedDataTable(
    columns: ColumnBuilder.() -> Unit,
    paginationState: PaginationState,
    onPageChanged: suspend (PaginationState) -> List<T>,
    context: CoroutineContext = Dispatchers.Default,
    modifier: Modifier = Modifier,
    cellPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 5.dp),
    divider: @Composable ((rowIndex: Int) -> Unit)? = @Composable { Divider() },
    footer: @Composable BoxScope.() -> Unit = {
        Paginator(paginationState, modifier = Modifier.align(Alignment.CenterEnd))
    },
    eachRow: PaginationRowBuilder.(T) -> Unit
) {
    BasicPaginatedDataTable(columns, paginationState, onPageChanged, context, modifier, cellPadding, divider, footer, eachRow)
}
