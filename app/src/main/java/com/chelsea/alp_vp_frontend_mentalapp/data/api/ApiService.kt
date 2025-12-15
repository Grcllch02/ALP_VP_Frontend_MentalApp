package com.chelsea.alp_vp_frontend_mentalapp.data.api

import com.chelsea.alp_vp_frontend_mentalapp.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    @GET("health")
    suspend fun getHealth(): Response<HealthResponse>
    
    @GET("game-states")
    suspend fun getAllGameStates(): Response<List<GameStateDto>>
    
    @GET("game-states/{id}")
    suspend fun getGameState(@Path("id") id: Int): Response<GameStateDto>
    
    @GET("game-states/user/{userId}/latest")
    suspend fun getUserLatestGameState(@Path("userId") userId: Int): Response<GameStateDto>
    
    @POST("game-states")
    suspend fun createGameState(@Body request: CreateGameStateRequest): Response<GameStateDto>
    
    @PUT("game-states/{id}")
    suspend fun updateGameState(
        @Path("id") id: Int,
        @Body request: UpdateGameStateRequest
    ): Response<GameStateDto>
    
    @DELETE("game-states/{id}")
    suspend fun deleteGameState(@Path("id") id: Int): Response<Unit>
    
    // User Authentication (placeholder - will be implemented in backend)
    @POST("auth/login")
    suspend fun login(@Body request: com.chelsea.alp_vp_frontend_mentalapp.data.model.LoginRequest): Response<com.chelsea.alp_vp_frontend_mentalapp.data.model.AuthResponse>
    
    @POST("auth/register")
    suspend fun register(@Body request: com.chelsea.alp_vp_frontend_mentalapp.data.model.RegisterRequest): Response<com.chelsea.alp_vp_frontend_mentalapp.data.model.AuthResponse>
}
