package com.chelsea.alp_vp_frontend_mentalapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TodoViewModel : ViewModel() {

    // List of tasks
    private val _tasks = MutableStateFlow<List<com.chelsea.alp_vp_frontend_mentalapp.ui.model.Task>>(emptyList())
    val tasks: StateFlow<List<com.chelsea.alp_vp_frontend_mentalapp.ui.model.Task>> = _tasks

    // Add Task Fields
    val taskName = MutableStateFlow("")
    val deadline = MutableStateFlow("")
    val hour = MutableStateFlow("")
    val minute = MutableStateFlow("")

    fun loadDummyData() {
        _tasks.value = listOf(
            com.chelsea.alp_vp_frontend_mentalapp.ui.model.Task(1, "Buy Milk", "13 Jan 2024", 60, false),
            com.chelsea.alp_vp_frontend_mentalapp.ui.model.Task(2, "Finish Homework", "14 Jan 2024", 90, true)
        )
    }

    fun addTask() {
        val h = hour.value.toIntOrNull() ?: 0
        val m = minute.value.toIntOrNull() ?: 0
        val totalMinutes = h * 60 + m

        val newTask = com.chelsea.alp_vp_frontend_mentalapp.ui.model.Task(
            task_id = (_tasks.value.size + 1),
            nama_task = taskName.value,
            deadline = deadline.value,
            duration = totalMinutes,
            done_status = false
        )

        _tasks.value = _tasks.value + newTask

        taskName.value = ""
        deadline.value = ""
        hour.value = ""
        minute.value = ""
    }

    fun toggleDone(taskId: Int, newValue: Boolean) {
        _tasks.value = _tasks.value.map {
            if (it.task_id == taskId) it.copy(done_status = newValue)
            else it
        }
    }
}