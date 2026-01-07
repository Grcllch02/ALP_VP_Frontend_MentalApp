package com.chelsea.alp_vp_frontend_mentalapp.ui.todo

// Model data untuk satu tugas
data class TodoTask(
    val id: Int,
    val title: String,
    val isDone: Boolean
)

// State utama untuk layar Todo
data class TodoUiState(
    val todoList: List<TodoTask> = emptyList()
)