package com.chelsea.alp_vp_frontend_mentalapp.data.model

import com.chelsea.alp_vp_frontend_mentalapp.ui.focus.MusicCategory

data class MusicItem(
    val title: String,
    val category: MusicCategory,
    val resId: Int
)