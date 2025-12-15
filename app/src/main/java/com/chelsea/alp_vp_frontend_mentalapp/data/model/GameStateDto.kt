package com.chelsea.alp_vp_frontend_mentalapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GameStateDto(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "user_id") val userId: Int,
    @Json(name = "boardState") val boardState: String,
    @Json(name = "score") val score: Int,
    @Json(name = "nextBlocks") val nextBlocks: String,
    @Json(name = "createdAt") val createdAt: String? = null,
    @Json(name = "updatedAt") val updatedAt: String? = null
)

@JsonClass(generateAdapter = true)
data class CreateGameStateRequest(
    @Json(name = "user_id") val userId: Int,
    @Json(name = "boardState") val boardState: String,
    @Json(name = "score") val score: Int,
    @Json(name = "nextBlocks") val nextBlocks: String
)

@JsonClass(generateAdapter = true)
data class UpdateGameStateRequest(
    @Json(name = "boardState") val boardState: String,
    @Json(name = "score") val score: Int,
    @Json(name = "nextBlocks") val nextBlocks: String
)

@JsonClass(generateAdapter = true)
data class HealthResponse(
    @Json(name = "status") val status: String
)
