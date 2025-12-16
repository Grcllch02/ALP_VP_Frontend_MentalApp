package com.chelsea.alp_vp_frontend_mentalapp.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelsea.alp_vp_frontend_mentalapp.data.api.RetrofitClient
import com.chelsea.alp_vp_frontend_mentalapp.data.model.TodoDto
import kotlinx.coroutines.launch

class TodoViewModel : ViewModel() {

    val todos = mutableStateOf<List<TodoDto>>(emptyList())

    fun loadTodos() {
        viewModelScope.launch {
            val response = RetrofitClient.apiService.getTodos()
            if (response.isSuccessful) {
                todos.value = response.body() ?: emptyList()
            }
        }
    }
}
