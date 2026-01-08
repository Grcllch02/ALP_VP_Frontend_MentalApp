package com.chelsea.alp_vp_frontend_mentalapp.data.repository

import com.chelsea.alp_vp_frontend_mentalapp.data.api.RetrofitClient
import com.chelsea.alp_vp_frontend_mentalapp.data.model.CreateTodoRequest
import com.chelsea.alp_vp_frontend_mentalapp.data.model.TodoDto
import com.chelsea.alp_vp_frontend_mentalapp.data.model.UpdateTodoRequest
import retrofit2.Response

class TodoRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun getTodos(): Response<List<TodoDto>> {
        return apiService.getAllTodos()
    }

    suspend fun createTodo(title: String, description: String?): Response<TodoDto> {
        val request = CreateTodoRequest(
            title = title,
            description = description
        )
        return apiService.createTodo(request)
    }

    suspend fun updateTodo(
        id: Int,
        title: String? = null,
        description: String? = null,
        isCompleted: Boolean? = null
    ): Response<TodoDto> {
        val request = UpdateTodoRequest(
            title = title,
            description = description,
            isCompleted = isCompleted
        )
        return apiService.updateTodo(id, request)
    }

    suspend fun deleteTodo(id: Int): Response<Unit> {
        return apiService.deleteTodo(id)
    }
}