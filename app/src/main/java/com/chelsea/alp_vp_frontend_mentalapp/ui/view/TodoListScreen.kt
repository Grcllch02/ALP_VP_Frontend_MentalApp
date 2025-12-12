package com.chelsea.alp_vp_frontend_mentalapp.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chelsea.alp_vp_frontend_mentalapp.ui.model.Task
import com.chelsea.alp_vp_frontend_mentalapp.ui.viewmodel.TodoViewModel

@Composable
fun TodoListScreen(
    viewModel: TodoViewModel,
    onAddTaskClick: () -> Unit
) {
    val tasks by viewModel.tasks.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDummyData()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTaskClick) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Your Tasks", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(tasks) { task ->
                    TaskCard(
                        task = task,
                        onToggle = { viewModel.toggleDone(task.task_id!!, it) }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskCard(task: Task, onToggle: (Boolean) -> Unit) {
    Card(
        Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(task.nama_task, style = MaterialTheme.typography.bodyLarge)
                if (task.deadline != null) {
                    Text("Deadline: ${task.deadline}", style = MaterialTheme.typography.bodySmall)
                }
            }
            Switch(
                checked = task.done_status,
                onCheckedChange = onToggle
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoListScreenPreview() {
    val viewModel = TodoViewModel()
    viewModel.loadDummyData()

    TodoListScreen(
        viewModel = viewModel,
        onAddTaskClick = {}
    )
}