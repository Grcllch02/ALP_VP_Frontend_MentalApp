package com.chelsea.alp_vp_frontend_mentalapp.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chelsea.alp_vp_frontend_mentalapp.ui.viewmodel.TodoViewModel

@Composable
fun AddTaskScreen(
    viewModel: TodoViewModel,
    onSaveSuccess: () -> Unit
) {
    val taskName by viewModel.taskName.collectAsState()
    val deadline by viewModel.deadline.collectAsState()
    val hour by viewModel.hour.collectAsState()
    val minute by viewModel.minute.collectAsState()

    Scaffold { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Text("Create New Task", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = taskName,
                onValueChange = { viewModel.taskName.value = it },
                label = { Text("Task Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = deadline,
                onValueChange = { viewModel.deadline.value = it },
                label = { Text("Deadline (ex: 12 Jan 2024)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedTextField(
                    value = hour,
                    onValueChange = { viewModel.hour.value = it },
                    label = { Text("Hours") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                OutlinedTextField(
                    value = minute,
                    onValueChange = { viewModel.minute.value = it },
                    label = { Text("Minutes") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.addTask()
                    onSaveSuccess()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Task")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddTaskScreenPreview() {
    val viewModel = TodoViewModel()

    AddTaskScreen(
        viewModel = viewModel,
        onSaveSuccess = {}
    )
}
