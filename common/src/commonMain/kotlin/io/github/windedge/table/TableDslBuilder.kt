package io.github.windedge.table

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@DslMarker
annotation class TableDslBuilder

@TableDslBuilder
interface ColumnBuilder {
    fun column(
        modifier: Modifier = Modifier,
        contentAlignment: Alignment = Alignment.CenterStart,
        composable: @Composable() (BoxScope.() -> Unit)
    )

    fun headerBackground(composable: @Composable () -> Unit)
}

@TableDslBuilder
interface RowsBuilder {
    fun row(modifier: Modifier = Modifier, content: RowBuilderImpl.() -> Unit)
}

@TableDslBuilder
interface RowBuilder {
    fun cell(
        modifier: Modifier = Modifier,
        contentAlignment: Alignment = Alignment.CenterStart,
        content: @Composable() (BoxScope.() -> Unit)
    )
}


