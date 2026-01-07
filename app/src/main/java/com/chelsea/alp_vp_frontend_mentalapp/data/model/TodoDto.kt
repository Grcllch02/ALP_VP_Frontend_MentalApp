package com.chelsea.alp_vp_frontend_mentalapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// 1. Ini bentuk data Todo yang diterima dari Backend (Database)
@JsonClass(generateAdapter = true)
data class TodoDto(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "is_completed") val isCompleted: Boolean,
    @Json(name = "user_id") val userId: Int? = null // Opsional, buat jaga-jaga
)

// 2. Ini data yang dikirim saat mau bikin Todo baru (Create)
@JsonClass(generateAdapter = true)
data class CreateTodoRequest(
    @Json(name = "title") val title: String,
    @Json(name = "is_completed") val isCompleted: Boolean = false
)

// 3. Ini data yang dikirim saat mau update (misal: coret/selesai)
@JsonClass(generateAdapter = true)
data class UpdateTodoRequest(
    @Json(name = "title") val title: String? = null,
    @Json(name = "is_completed") val isCompleted: Boolean? = null
)