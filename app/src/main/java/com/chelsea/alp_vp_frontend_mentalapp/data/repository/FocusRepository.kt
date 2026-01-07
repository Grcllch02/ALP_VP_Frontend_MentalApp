package com.chelsea.alp_vp_frontend_mentalapp.data.repository

import com.chelsea.alp_vp_frontend_mentalapp.data.api.ApiService
import com.chelsea.alp_vp_frontend_mentalapp.data.model.CreateFocusSessionRequest
import com.chelsea.alp_vp_frontend_mentalapp.data.model.FocusSessionDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FocusRepository(
    private val apiService: ApiService
) {

    suspend fun saveSession(duration: Int): Result<FocusSessionDto> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.saveFocusSession(
                    CreateFocusSessionRequest(duration)
                )

                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.Success(it)
                    } ?: Result.Error("Empty response body")
                } else {
                    Result.Error("Failed to save session: ${response.code()}")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error")
            }
        }

    suspend fun getHistory(): Result<List<FocusSessionDto>> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getFocusHistory()

                if (response.isSuccessful) {
                    Result.Success(response.body() ?: emptyList())
                } else {
                    Result.Error("Failed to load history: ${response.code()}")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Network error")
            }
        }
}
