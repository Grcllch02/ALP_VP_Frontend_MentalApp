package com.chelsea.alp_vp_frontend_mentalapp.ui.model

data class Task(
    val task_id: Int? = null,
    val nama_task: String,
    val deadline: String?,
    val duration: Int?,
    val done_status: Boolean,
    val kategori_task_id: Int? = null,
)