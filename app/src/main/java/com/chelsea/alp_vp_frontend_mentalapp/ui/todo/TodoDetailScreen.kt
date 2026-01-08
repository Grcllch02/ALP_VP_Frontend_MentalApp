package com.chelsea.alp_vp_frontend_mentalapp.ui.todo

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailScreen(
    taskId: Int,
    viewModel: TodoViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val task = viewModel.getTaskById(taskId)

    var title by remember { mutableStateOf(task?.title ?: "") }
    var date by remember { mutableStateOf(task?.date ?: "") }
    var duration by remember { mutableStateOf(task?.duration ?: "") }

    val bgColor = Color(0xFF141625)
    val whiteText = Color.White
    val blueButton = Color(0xFF7C5DFA)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Task", color = whiteText) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = whiteText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (task != null) {
                DetailInput("Task Name", title) { title = it }
                DetailInput("Date / Deadline", date) { date = it }
                DetailInput("Duration", duration) { duration = it }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        viewModel.updateTaskDetails(taskId, title, date, duration)
                        Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show()
                        onBack()
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = blueButton),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text("Save Changes", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Text("Task not found!", color = Color.Red)
            }
        }
    }
}

@Composable
fun DetailInput(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(label, color = Color(0xFFDFE3FA), fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF252945),
                unfocusedContainerColor = Color(0xFF252945),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}