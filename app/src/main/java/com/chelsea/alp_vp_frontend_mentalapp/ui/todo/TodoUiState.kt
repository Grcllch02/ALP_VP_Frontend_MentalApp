package com.chelsea.alp_vp_frontend_mentalapp.ui.todo

data class TodoTask(
    val id: Int,
    val title: String,
    val isDone: Boolean,
    val date: String,
    val duration: String
)

data class TodoUiState(
    val todoList: List<TodoTask> = emptyList()
)