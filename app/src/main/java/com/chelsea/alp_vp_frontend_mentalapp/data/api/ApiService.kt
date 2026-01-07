package com.chelsea.alp_vp_frontend_mentalapp.data.api

import com.chelsea.alp_vp_frontend_mentalapp.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // =======================
    // FOCUS
    // =======================

    @POST("focus")
    suspend fun saveFocusSession(
        @Body request: CreateFocusSessionRequest
    ): Response<FocusSessionDto>

    @GET("focus/history")
    suspend fun getFocusHistory(): Response<List<FocusSessionDto>>

    // =======================
    // HEALTH
    // =======================

    @GET("health")
    suspend fun getHealth(): Response<HealthResponse>

    // =======================
    // GAME
    // =======================

    @GET("game-states")
    suspend fun getAllGameStates(): Response<List<GameStateDto>>

    @GET("game-states/{id}")
    suspend fun getGameState(
        @Path("id") id: Int
    ): Response<GameStateDto>

    @GET("game-states/user/{userId}/latest")
    suspend fun getUserLatestGameState(
        @Path("userId") userId: Int
    ): Response<GameStateDto>

    @POST("game-states")
    suspend fun createGameState(
        @Body request: CreateGameStateRequest
    ): Response<GameStateDto>

    @PUT("game-states/{id}")
    suspend fun updateGameState(
        @Path("id") id: Int,
        @Body request: UpdateGameStateRequest
    ): Response<GameStateDto>

    @DELETE("game-states/{id}")
    suspend fun deleteGameState(
        @Path("id") id: Int
    ): Response<Unit>

    // =======================
    // AUTH
    // =======================

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    // =======================
    // TODO LIST (Tambahan Punya Kamu)
    // =======================

    // Ambil semua todo list milik user tertentu
    @GET("todos/user/{userId}")
    suspend fun getTodosByUserId(
        @Path("userId") userId: Int
    ): Response<List<TodoDto>>

    // Bikin todo baru
    @POST("todos")
    suspend fun createTodo(
        @Body request: CreateTodoRequest
    ): Response<TodoDto>

    // Update todo (misal ganti judul atau centang selesai)
    @PUT("todos/{id}")
    suspend fun updateTodo(
        @Path("id") id: Int,
        @Body request: UpdateTodoRequest
    ): Response<TodoDto>

    // Hapus todo
    @DELETE("todos/{id}")
    suspend fun deleteTodo(
        @Path("id") id: Int
    ): Response<Unit>
}
