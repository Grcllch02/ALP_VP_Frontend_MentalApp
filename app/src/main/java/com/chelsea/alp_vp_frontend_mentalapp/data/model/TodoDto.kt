package com.chelsea.alp_vp_frontend_mentalapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TodoDto(
    @Json(name = "id")
    val id: Int,

    @Json(name = "title")
    val title: String,

    @Json(name = "description")
    val description: String? = null,

    // is_completed di JSON akan jadi isCompleted di Kotlin
    @Json(name = "is_completed")
    val isCompleted: Boolean,

    @Json(name = "created_at")
    val createdAt: String? = null
)

@JsonClass(generateAdapter = true)
data class CreateTodoRequest(
    @Json(name = "title")
    val title: String,

    @Json(name = "description")
    val description: String? = null
)

@JsonClass(generateAdapter = true)
data class UpdateTodoRequest(
    @Json(name = "title")
    val title: String? = null,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "is_completed")
    val isCompleted: Boolean? = null
)