package com.chelsea.alp_vp_frontend_mentalapp.ui.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TodoScreen(
    viewModel: TodoViewModel = viewModel()
) {
    // Collect State (Sama kayak FocusScreen)
    val state by viewModel.uiState.collectAsState()

    // State lokal untuk input text
    var newTodoTitle by remember { mutableStateOf("") }

    // Warna Tema (Samain dengan FocusScreen)
    val bgColor = Color(0xFF1A1A2E)
    val cardColor = Color(0xFF16213E)
    val buttonColor = Color(0xFF0F4C75)
    val textColor = Color.White
    val grayColor = Color(0xFF9CA3AF)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // --- HEADER SECTION ---
            Text(
                modifier = Modifier.padding(top = 25.dp, bottom = 8.dp),
                text = "My Tasks",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Text(
                text = "What do you want to achieve today?",
                color = grayColor,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // --- INPUT SECTION ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newTodoTitle,
                    onValueChange = { newTodoTitle = it },
                    placeholder = { Text("Add new task...", color = grayColor) },
                    modifier = Modifier
                        .weight(1f)
                        .background(cardColor, RoundedCornerShape(12.dp)),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        focusedContainerColor = cardColor,
                        unfocusedContainerColor = cardColor,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
                        viewModel.addTodo(newTodoTitle)
                        newTodoTitle = "" // Reset input
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    modifier = Modifier.height(56.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                }
            }

            // --- LIST SECTION ---
            // Pakai LazyColumn biar bisa scroll kalau listnya panjang
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(state.todoList) { task ->
                    TodoItemCard(
                        task = task,
                        cardColor = cardColor,
                        textColor = textColor,
                        onToggle = { viewModel.toggleTodo(task.id) },
                        onDelete = { viewModel.deleteTodo(task.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun TodoItemCard(
    task: TodoTask,
    cardColor: Color,
    textColor: Color,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(cardColor, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Bagian Kiri (Checkbox + Text)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .clickable { onToggle() } // Klik area text juga nge-check
        ) {
            Checkbox(
                checked = task.isDone,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF0F4C75),
                    uncheckedColor = Color(0xFF9CA3AF),
                    checkmarkColor = Color.White
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = task.title,
                fontSize = 16.sp,
                color = if (task.isDone) Color(0xFF9CA3AF) else textColor,
                textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None
            )
        }

        // Bagian Kanan (Tombol Delete)
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color(0xFFEF5350) // Merah soft
            )
        }
    }
}