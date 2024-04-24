import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.windedge.table.DataTable
import io.github.windedge.table.m2.PaginatedDataTable
import io.github.windedge.table.rememberPaginationState

@Preview
@Composable
fun DataTablePreview() {
    val data = listOf(
        mapOf("Name" to "John Doe", "Age" to "30", "Email" to "john.doe@example.com"),
        mapOf("Name" to "Jane Doe", "Age" to "25", "Email" to "jane.doe@example.com"),
    )

    DataTable(
        columns = {
            headerBackground {
                Box(modifier = Modifier.background(color = Color.LightGray))
            }
            column { Text("Name") }
            column { Text("Age") }
            column { Text("Email") }
        },
    ) {
        data.forEach { row ->
            row(modifier = Modifier) {
                cell { Text(row["Name"] ?: "") }
                cell { Text(row["Age"] ?: "") }
                cell { Text(row["Email"] ?: "") }
            }
        }
    }

}

@Preview
@Composable
fun PaginatedDataTablePreview() {
    val data = List(50) {
        mapOf("Column 1" to "Item $it", "Column 2" to "Item $it", "Column 3" to "Item $it")
    }
    val paginationState = rememberPaginationState(data.size, pageSize = 5)

    PaginatedDataTable(
        columns = {
            headerBackground {
                Box(Modifier.background(Color.LightGray))
            }

            column { Text("Column 1") }
            column { Text("Column 2") }
            column { Text("Column 3") }
        },
        paginationState = paginationState,
        onPageChanged = {
            data.chunked(it.pageSize)[it.pageIndex]
        }
    ) { item: Map<String, String> ->
        row(modifier = Modifier) {
            cell { Text(item["Column 1"] ?: "") }
            cell { Text(item["Column 2"] ?: "") }
            cell { Text(item["Column 3"] ?: "") }
        }
    }
}