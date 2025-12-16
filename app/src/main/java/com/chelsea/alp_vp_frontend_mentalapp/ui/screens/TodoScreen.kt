package com.chelsea.alp_vp_frontend_mentalapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chelsea.alp_vp_frontend_mentalapp.ui.viewmodel.TodoViewModel

@Composable
fun TodoScreen(viewModel: TodoViewModel = viewModel()) {

    LaunchedEffect(Unit) {
        viewModel.loadTodos()
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(viewModel.todos.value) { todo ->
            Text(text = "• ${todo.title}")
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
