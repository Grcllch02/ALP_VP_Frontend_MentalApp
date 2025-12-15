package com.chelsea.alp_vp_frontend_mentalapp.ui.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelsea.alp_vp_frontend_mentalapp.data.api.RetrofitClient
import com.chelsea.alp_vp_frontend_mentalapp.data.model.LoginRequest
import com.chelsea.alp_vp_frontend_mentalapp.data.model.RegisterRequest
import com.chelsea.alp_vp_frontend_mentalapp.data.model.UserDto
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    val currentUser = mutableStateOf<UserDto?>(null)
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)
    
    fun isLoggedIn(): Boolean = currentUser.value != null
    
    fun login(email: String, password: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            
            try {
                val response = RetrofitClient.apiService.login(
                    LoginRequest(email, password)
                )
                if (response.isSuccessful && response.body() != null) {
                    currentUser.value = response.body()!!.user
                    onSuccess()
                } else {
                    errorMessage.value = "Login failed: Invalid credentials"
                }
            } catch (e: Exception) {
                errorMessage.value = "Login error: ${e.message}"
            }
            isLoading.value = false
        }
    }
    
    fun register(username: String, email: String, password: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            
            try {
                val response = RetrofitClient.apiService.register(
                    RegisterRequest(username, email, password)
                )
                if (response.isSuccessful && response.body() != null) {
                    currentUser.value = response.body()!!.user
                    onSuccess()
                } else {
                    errorMessage.value = "Registration failed: ${response.message()}"
                }
            } catch (e: Exception) {
                errorMessage.value = "Registration error: ${e.message}"
            }
            isLoading.value = false
        }
    }
    
    fun logout() {
        currentUser.value = null
    }
    
    fun clearError() {
        errorMessage.value = null
    }
}
