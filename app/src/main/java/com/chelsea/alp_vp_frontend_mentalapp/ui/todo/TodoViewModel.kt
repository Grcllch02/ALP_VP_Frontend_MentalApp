package com.chelsea.alp_vp_frontend_mentalapp.ui.todo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TodoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TodoUiState())
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()

    private var nextId = 100

    init {
        val dummyData = listOf(
            TodoTask(1, "Presentasi Visual Programming", false, "10 Jan 2024", "2 Jam"),
            TodoTask(2, "Siapkan demo booth", true, "11 Jan 2024", "1 Jam"),
            TodoTask(3, "Beli perlengkapan dekorasi", false, "12 Jan 2024", "45 Menit")
        )
        _uiState.value = TodoUiState(todoList = dummyData)
        nextId = 4
    }

    fun addTodo(title: String) {
        if (title.isBlank()) return
        val newTask = TodoTask(
            id = nextId++,
            title = title,
            isDone = false,
            date = "-",
            duration = "-"
        )
        _uiState.update { it.copy(todoList = it.todoList + newTask) }
    }

    fun toggleTodo(taskId: Int) {
        _uiState.update { currentState ->
            val updatedList = currentState.todoList.map { task ->
                if (task.id == taskId) task.copy(isDone = !task.isDone) else task
            }
            currentState.copy(todoList = updatedList)
        }
    }

    fun deleteTodo(taskId: Int) {
        _uiState.update { currentState ->
            currentState.copy(todoList = currentState.todoList.filter { it.id != taskId })
        }
    }

    fun getTaskById(taskId: Int): TodoTask? {
        return _uiState.value.todoList.find { it.id == taskId }
    }

    fun updateTaskDetails(taskId: Int, newTitle: String, newDate: String, newDuration: String) {
        _uiState.update { currentState ->
            val updatedList = currentState.todoList.map { task ->
                if (task.id == taskId) {
                    task.copy(title = newTitle, date = newDate, duration = newDuration)
                } else {
                    task
                }
            }
            currentState.copy(todoList = updatedList)
        }
    }
}