package com.chelsea.alp_vp_frontend_mentalapp.data.repository

import com.chelsea.alp_vp_frontend_mentalapp.data.api.ApiService
import com.chelsea.alp_vp_frontend_mentalapp.data.model.CreateTodoRequest
import com.chelsea.alp_vp_frontend_mentalapp.data.model.TodoDto
import com.chelsea.alp_vp_frontend_mentalapp.data.model.UpdateTodoRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TodoRepository(
    private val apiService: ApiService
) {

    // 1. Ambil semua Todo berdasarkan User ID
    suspend fun getTodos(userId: Int): Result<List<TodoDto>> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getTodosByUserId(userId)

                if (response.isSuccessful) {
                    Result.Success(response.body() ?: emptyList())
                } else {
                    Result.Error("Failed to load todos: ${response.code()}")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error")
            }
        }

    // 2. Tambah Todo Baru
    suspend fun createTodo(title: String): Result<TodoDto> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.createTodo(
                    CreateTodoRequest(title = title, isCompleted = false)
                )

                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.Success(it)
                    } ?: Result.Error("Empty response body")
                } else {
                    Result.Error("Failed to create todo: ${response.code()}")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error")
            }
        }

    // 3. Update Todo (Coret/Selesai atau Ganti Judul)
    suspend fun updateTodo(id: Int, title: String? = null, isCompleted: Boolean? = null): Result<TodoDto> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.updateTodo(
                    id = id,
                    request = UpdateTodoRequest(title = title, isCompleted = isCompleted)
                )

                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.Success(it)
                    } ?: Result.Error("Empty response body")
                } else {
                    Result.Error("Failed to update todo: ${response.code()}")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error")
            }
        }

    // 4. Hapus Todo
    suspend fun deleteTodo(id: Int): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.deleteTodo(id)

                if (response.isSuccessful) {
                    Result.Success(Unit)
                } else {
                    Result.Error("Failed to delete todo: ${response.code()}")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error")
            }
        }
}