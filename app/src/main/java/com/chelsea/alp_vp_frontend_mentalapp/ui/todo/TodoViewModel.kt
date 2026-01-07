package com.chelsea.alp_vp_frontend_mentalapp.ui.todo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TodoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TodoUiState())
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()

    // ID counter sederhana buat mock data
    private var nextId = 100

    init {
        // --- MOCK DATA BUAT DEMO ---
        // Biar pas dibuka ga kosong melompong
        val dummyData = listOf(
            TodoTask(1, "Selesaikan Presentasi ALP", false),
            TodoTask(2, "Belanja Kebutuhan Bulanan", true),
            TodoTask(3, "Olahraga 30 Menit", false),
            TodoTask(4, "Revisi Laporan", false)
        )
        _uiState.value = TodoUiState(todoList = dummyData)
    }

    // Fungsi Tambah Tugas
    fun addTodo(title: String) {
        if (title.isBlank()) return

        val newTask = TodoTask(
            id = nextId++,
            title = title,
            isDone = false
        )

        _uiState.update { currentState ->
            currentState.copy(
                todoList = currentState.todoList + newTask
            )
        }
    }

    // Fungsi Coret/Uncoret Tugas
    fun toggleTodo(taskId: Int) {
        _uiState.update { currentState ->
            val updatedList = currentState.todoList.map { task ->
                if (task.id == taskId) {
                    task.copy(isDone = !task.isDone)
                } else {
                    task
                }
            }
            currentState.copy(todoList = updatedList)
        }
    }

    // Fungsi Hapus Tugas
    fun deleteTodo(taskId: Int) {
        _uiState.update { currentState ->
            val filteredList = currentState.todoList.filter { it.id != taskId }
            currentState.copy(todoList = filteredList)
        }
    }
}