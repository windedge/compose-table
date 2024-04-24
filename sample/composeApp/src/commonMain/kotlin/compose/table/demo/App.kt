package compose.table.demo

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.windedge.table.m3.PaginatedDataTable
import io.github.windedge.table.rememberPaginationState

data class User(val id: Int, val name: String, val email: String, val age: Int?)

fun randomUser(id: Int): User {
    val name = arrayOf("Alice", "Bob", "Charlie").random()
    val email = arrayOf("charlie@example.com", "bob@example.com", "alice@example.com").random()
    val age = arrayOf(28, 30, null).random()

    return User(id, name, email, age)
}

@Composable
fun App() {
    val vScrollState = rememberScrollState()
    Box(modifier = Modifier.padding(10.dp).verticalScroll(vScrollState)) {
        Column(modifier = Modifier) {
            val users = remember {
                mutableStateListOf<User>().apply {
                    repeat(18) { add(randomUser(it)) }
                }
            }

            val color = colorScheme.onPrimary
            val textStyle = MaterialTheme.typography.titleMedium
            val selectedIds = remember { mutableStateListOf<Int>() }
            val paginationState = rememberPaginationState(users.size, pageSize = 5)


            Button(onClick = { users.add(randomUser(users.size)) }) {
                Text("Add User")
            }

            PaginatedDataTable(
                columns = {
                    headerBackground {
                        Box(Modifier.clip(shapes.extraSmall).background(colorScheme.primary))
                    }

                    column {
                        val range = paginationState.run {
                            (pageIndex - 1) * pageSize until pageIndex * pageSize
                        }
                        val checked = range.all { selectedIds.contains(it) }
                        Checkbox(
                            checked,
                            colors = CheckboxDefaults.colors(colorScheme.inversePrimary, colorScheme.onPrimary),
                            onCheckedChange = {
                                if (it) range.forEach { selectedIds.add(it) } else range.forEach { selectedIds.remove(it) }
                            }
                        )
                    }

                    column(modifier = Modifier.height(50.dp)) {
                        Text(
                            "Name",
                            color = color,
                            style = textStyle
                        )
                    }
                    column { Text("Email", color = color, style = textStyle) }
                    column { Text("Age", color = color, style = textStyle) }
                    column { }
                },
                paginationState = paginationState,
                onPageChanged = {
                    selectedIds.clear()
                    users.chunked(it.pageSize)[(it.pageIndex - 1)]
                },
            ) { user ->
                val checked = selectedIds.contains(user.id)
                row(modifier = Modifier.toggleable(checked, onValueChange = {
                    if (it) selectedIds.add(user.id) else selectedIds.remove(user.id)
                })) {
                    cell {
                        Checkbox(checked, onCheckedChange = {
                            if (it) selectedIds.add(user.id) else selectedIds.remove(user.id)
                        })
                    }
                    cell { Text(user.name) }
                    cell { Text(user.email) }
                    cell { Text(user.age?.toString() ?: "N/A") }
                    cell {
                        IconButton({
                            println("delete ${user.name}")
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }

        }

//        val scrollbarAdapter = rememberScrollbarAdapter(scrollState)
//        VerticalScrollbar(scrollbarAdapter, modifier = Modifier.align(Alignment.CenterEnd))
    }

}


internal expect fun openUrl(url: String?)