package io.github.windedge.table.material

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.windedge.table.BasePaginatedDataTable
import io.github.windedge.table.ColumnBuilder
import io.github.windedge.table.PaginationRowBuilder
import io.github.windedge.table.PaginationState
import io.github.windedge.table.components.Divider

@Composable
fun <T> PaginatedDataTable(
    columns: ColumnBuilder.() -> Unit,
    paginationState: PaginationState,
    onPageChanged: suspend (PaginationState) -> List<T>,
    modifier: Modifier = Modifier,
    cellPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 5.dp),
    divider: @Composable ((rowIndex: Int) -> Unit)? = @Composable { Divider() },
    footer: @Composable BoxScope.() -> Unit = {
        Paginator(paginationState, modifier = Modifier.align(Alignment.CenterEnd))
    },
    eachRow: PaginationRowBuilder.(T) -> Unit
) {
    BasePaginatedDataTable(columns, paginationState, onPageChanged, modifier, cellPadding, divider, footer, eachRow)
}