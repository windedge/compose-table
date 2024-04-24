package io.github.windedge.table

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import io.github.windedge.table.components.Divider
import kotlin.math.max

@Composable
fun DataTable(
    columns: ColumnBuilder.() -> Unit,
    modifier: Modifier = Modifier,
    cellPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 5.dp),
    divider: @Composable ((rowIndex: Int) -> Unit)? = @Composable { Divider() },
    footer: @Composable (BoxScope.() -> Unit)? = null,
    rowsContent: RowsBuilder.() -> Unit
) {
    val columnBuilder = ColumnBuilderImpl().apply { columns() }
    val headers = columnBuilder.columns
    val rows = RowsBuilderImpl().apply { rowsContent() }.rows.map { it.apply { this.build() } }

    val contentComposable = @Composable {
        headers.forEach { (header, modifier, contentAlignment) ->
            Box(modifier = modifier.padding(cellPadding), contentAlignment = contentAlignment) { header() }
        }
        rows.forEach { row ->
            row.cells.forEach { (cell, modifier, contentAlignment) ->
                Box(modifier = modifier.padding(cellPadding), contentAlignment = contentAlignment) { cell() }
            }
        }
    }

    val backgroundComposables = @Composable {
        rows.forEach { Box(modifier = it.modifier.then(Modifier.fillMaxSize())) }
    }

    SubcomposeLayout(modifier = modifier) { constraints ->
        // Combine subcompose calls for headers and cells
        val contentPlaceables = subcompose("content", content = contentComposable)

        // Measure to determine the column widths
        val columnWidths = IntArray(headers.size) { 0 }
        val rowHeights = IntArray(rows.size + 1) { 0 }
        contentPlaceables.chunked(headers.size).forEachIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, placeable ->
                placeable.measure(constraints).also { measured ->
                    columnWidths[columnIndex] = max(columnWidths[columnIndex], measured.width)
                    rowHeights[rowIndex] = max(rowHeights[rowIndex], measured.height)
                }
            }
        }

        // Calculate the scaling factor if needed
        val totalWidth = columnWidths.sum()

        val scale = when {
            constraints.maxWidth == Constraints.Infinity -> 1f           // add this to avoid infinite width in parent scrolling
            totalWidth < constraints.maxWidth -> constraints.maxWidth.toFloat() / totalWidth
            else -> 1f
        }

        // Apply scaling to column widths
        val scaledColumnWidths = columnWidths.map { (it * scale).toInt() }

        // Measure with the scaled column widths
        val scaledContentPlaceables =
            subcompose("scaledContent", content = contentComposable).mapIndexed { index, measurable ->
                val rowHeight = rowHeights[index / headers.size]
                val columnWidth = scaledColumnWidths[index % headers.size]
                val scaledConstraints = constraints.copy(columnWidth, columnWidth, rowHeight, rowHeight)
                measurable.measure(scaledConstraints)
            }

        // Split the measured placeables into headers and rows
        val scaledHeaderPlaceables = scaledContentPlaceables.take(headers.size)
        val scaledCellRowPlaceables = scaledContentPlaceables.drop(headers.size).chunked(headers.size)

        // Calculate the height of each row and the total height
        val headerHeight = scaledHeaderPlaceables.maxOf { it.height }
        val tableWidth = scaledColumnWidths.sum()

        val headerBackground = subcompose("headerDecoration", columnBuilder.headerBackground).firstOrNull()
            ?.measure(constraints.copy(maxWidth = tableWidth, maxHeight = headerHeight))

        val rowBackgrounds = subcompose("rowBackgrounds", backgroundComposables).mapIndexed { index, measurable ->
            measurable.measure(constraints.copy(maxWidth = tableWidth, maxHeight = rowHeights[index + 1]))
        }

        val dividerPlacables = subcompose("dividers") {
            repeat(rows.size + 1) { divider?.invoke(it) } // dividers = header + rows
        }.mapIndexed { rowIndex, mesurable ->
            mesurable.measure(constraints.copy(maxWidth = tableWidth, maxHeight = rowHeights[rowIndex]))
        }
        val dividierHeights = dividerPlacables.map { it.height }

        val footerPlaceable = footer?.let {
            val footerComposable = @Composable { Box(modifier = Modifier.padding(cellPadding)) { it() } }
            subcompose("footer", footerComposable).firstOrNull()
                ?.measure(constraints.copy(minWidth = tableWidth, maxWidth = tableWidth))
        }
        val footerHeight = footerPlaceable?.height ?: 0

        val tableHeight = rowHeights.sum() + dividierHeights.sum() + footerHeight
        // Layout the headers and cells
        layout(tableWidth, tableHeight) {
            // header decoration
            headerBackground?.place(0, 0)

            // Place headers
            var xPosition = 0
            var yPosition = 0
            scaledHeaderPlaceables.forEach { placeable ->
                placeable.place(x = xPosition, y = yPosition)
                xPosition += placeable.width
            }

            // header divider
            yPosition += headerHeight
            if (headerBackground == null) {
                dividerPlacables[0].place(0, yPosition)
                yPosition += dividierHeights[0]
            }

            // Place cells
            scaledCellRowPlaceables.forEachIndexed { index, row ->
                val background = rowBackgrounds[index]
                background.place(0, yPosition)

                xPosition = 0
                row.forEach { placeable ->
                    placeable.place(x = xPosition, y = yPosition)
                    xPosition += placeable.width
                }
                yPosition += rowHeights[index + 1]

                // Place divider
                dividerPlacables[index + 1].place(0, yPosition)
                yPosition += dividierHeights[index + 1]

            }

            footerPlaceable?.place(0, yPosition)
        }
    }
}

data class TableCell(
    val composable: @Composable BoxScope.() -> Unit,
    val modifier: Modifier = Modifier,
    val contentAlignment: Alignment = Alignment.CenterStart,
)

class ColumnBuilderImpl : ColumnBuilder {
    val columns = mutableListOf<TableCell>()
    var headerBackground: @Composable (() -> Unit) = @Composable {}

    override fun column(
        modifier: Modifier,
        contentAlignment: Alignment,
        composable: @Composable BoxScope.() -> Unit
    ) {
        columns.add(TableCell(composable, modifier, contentAlignment))
    }

    override fun headerBackground(composable: @Composable () -> Unit) {
        headerBackground = @Composable { RowLayout(composable) }
    }
}

@Composable
fun RowLayout(composable: @Composable () -> Unit) {
    Layout(composable, modifier = Modifier.fillMaxSize()) { measurables, constraints ->
        val placeable = measurables.map { it.measure(constraints) }.first()
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeable.place(0, 0)
        }
    }
}


class RowBuilderImpl(val build: RowBuilderImpl.() -> Unit, val modifier: Modifier = Modifier) : RowBuilder {
    val cells = mutableListOf<TableCell>()

    override fun cell(
        modifier: Modifier,
        contentAlignment: Alignment,
        content: @Composable BoxScope.() -> Unit
    ) {
        cells.add(TableCell(content, modifier, contentAlignment))
    }
}


class RowsBuilderImpl : RowsBuilder {
    val rows = mutableListOf<RowBuilderImpl>()

    override fun row(modifier: Modifier, content: RowBuilderImpl.() -> Unit) {
        rows.add(RowBuilderImpl(content, modifier))
    }
}


