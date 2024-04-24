package io.github.windedge.table

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import io.github.windedge.table.components.Divider
import kotlinx.coroutines.launch


@Composable
fun <T> BasicPaginatedDataTable(
    columns: ColumnBuilder.() -> Unit,
    paginationState: PaginationState,
    onPageChanged: suspend (PaginationState) -> List<T>,
    modifier: Modifier = Modifier,
    cellPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 5.dp),
    divider: @Composable ((rowIndex: Int) -> Unit)? = @Composable { Divider() },
    footer: @Composable (BoxScope.() -> Unit)? = null,
    eachRow: PaginationRowBuilder.(T) -> Unit
) {
    val recordList = remember { mutableStateListOf<T>() }
    LaunchedEffect(paginationState.totalCount, paginationState.pageSize, paginationState.pageIndex) {
        launch {
            onPageChanged(paginationState).let {
                recordList.clear()
                recordList.addAll(it)
            }
        }
    }

    DataTable(columns, modifier, cellPadding, divider, footer) {
        recordList.forEachIndexed { index, record ->
            PaginationRowBuilderImpl(index, this).apply { eachRow(record) }
        }
    }
}

@TableDslBuilder
interface PaginationRowBuilder {
    val rowIndex: Int
    fun row(modifier: Modifier = Modifier, content: RowBuilder.() -> Unit)
}

@Suppress("unused")
class PaginationRowBuilderImpl(override val rowIndex: Int, private val parent: RowsBuilder) : PaginationRowBuilder {
    override fun row(modifier: Modifier, content: RowBuilder.() -> Unit) {
        parent.row(modifier) { content() }
    }
}

@Suppress("MemberVisibilityCanBePrivate", "unused")
class PaginationState(totalCount: Int, pageIndex: Int, pageSize: Int) {
    var totalCount by mutableStateOf(totalCount)
        private set
    var pageSize by mutableStateOf(pageSize)
        private set
    var pageIndex by mutableStateOf(pageIndex)
        private set

    val pageCount: Int get() = (totalCount + pageSize - 1) / pageSize

    fun ensurePageCountPositive(): Int = if (pageCount < 1) 1 else pageCount

    fun next() {
        pageIndex = (pageIndex + 1).coerceIn(1, ensurePageCountPositive())
    }

    fun previous() {
        pageIndex = (pageIndex - 1).coerceIn(1, ensurePageCountPositive())
    }

    fun goto(index: Int) {
        pageIndex = index.coerceIn(1, ensurePageCountPositive())
    }

    fun changeTotalCount(count: Int) {
        totalCount = count
    }

    fun changePageSize(size: Int) {
        pageSize = size
    }

    companion object {
        val Saver: Saver<PaginationState, *> = listSaver(
            save = { listOf(it.totalCount, it.pageIndex, it.pageSize) },
            restore = {
                PaginationState(it[0], it[1], it[2])
            }
        )
    }
}

@Composable
fun rememberPaginationState(
    initialTotalCount: Int,
    initialPageIndex: Int = 1,
    pageSize: Int = 10,
): PaginationState {
    var pageIndex by remember { mutableStateOf(initialPageIndex) }
    val state = rememberSaveable(initialTotalCount, initialPageIndex, pageSize, saver = PaginationState.Saver) {
        PaginationState(initialTotalCount, pageIndex, pageSize).apply { goto(this.pageIndex) }
    }
    LaunchedEffect(state.pageIndex) { pageIndex = state.pageIndex }
    return state
}
