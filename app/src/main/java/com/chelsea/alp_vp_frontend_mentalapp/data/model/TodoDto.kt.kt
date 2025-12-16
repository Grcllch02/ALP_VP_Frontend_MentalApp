package com.chelsea.alp_vp_frontend_mentalapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TodoDto(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "title") val title: String,
    @Json(name = "is_done") val isDone: Boolean
)
