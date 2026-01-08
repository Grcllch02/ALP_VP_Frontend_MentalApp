package com.chelsea.alp_vp_frontend_mentalapp.ui.todo

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TodoScreen(
    viewModel: TodoViewModel,
    onNavigateToDetail: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var newTodoTitle by remember { mutableStateOf("") }

    val bgColor = Color(0xFF141625)
    val cardColor = Color(0xFF1E2139)
    val inputColor = Color(0xFF252945)
    val blueButton = Color(0xFF7C5DFA)
    val whiteText = Color.White
    val grayText = Color(0xFFDFE3FA)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = "Todo List",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = whiteText,
                modifier = Modifier.padding(vertical = 20.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newTodoTitle,
                    onValueChange = { newTodoTitle = it },
                    placeholder = { Text("Add new task...", color = grayText) },
                    modifier = Modifier
                        .weight(1f)
                        .background(inputColor, RoundedCornerShape(12.dp)),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = whiteText,
                        unfocusedTextColor = whiteText,
                        focusedContainerColor = inputColor,
                        unfocusedContainerColor = inputColor,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = {
                        if (newTodoTitle.isBlank()) {
                            Toast.makeText(context, "Fill the task name!", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.addTodo(newTodoTitle)
                            newTodoTitle = ""
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = blueButton),
                    modifier = Modifier.height(56.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = whiteText)
                }
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(items = state.todoList, key = { it.id }) { task ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(cardColor, RoundedCornerShape(12.dp))
                            .clickable { onNavigateToDetail(task.id) }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = task.isDone,
                            onCheckedChange = { viewModel.toggleTodo(task.id) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = blueButton,
                                uncheckedColor = grayText,
                                checkmarkColor = whiteText
                            )
                        )
                        Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
                            Text(
                                text = task.title,
                                color = if (task.isDone) Color.Gray else whiteText,
                                textDecoration = if (task.isDone) TextDecoration.LineThrough else null,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (task.date.isNotBlank() && task.date != "-") {
                                Text(
                                    text = "${task.date} • ${task.duration}",
                                    color = grayText,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        IconButton(onClick = { viewModel.deleteTodo(task.id) }) {
                            Icon(Icons.Default.Delete, "Delete", tint = Color(0xFFEC5757))
                        }
                    }
                }
            }
        }
    }
}