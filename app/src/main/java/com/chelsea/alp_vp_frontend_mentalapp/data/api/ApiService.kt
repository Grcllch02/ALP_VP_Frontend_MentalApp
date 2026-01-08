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

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @GET("todos")
    suspend fun getAllTodos(): Response<List<TodoDto>>

    @POST("todos")
    suspend fun createTodo(
        @Body request: CreateTodoRequest
    ): Response<TodoDto>

    @PUT("todos/{id}")
    suspend fun updateTodo(
        @Path("id") todoId: Int,
        @Body request: UpdateTodoRequest
    ): Response<TodoDto>

    @DELETE("todos/{id}")
    suspend fun deleteTodo(
        @Path("id") todoId: Int
    ): Response<Unit>
}