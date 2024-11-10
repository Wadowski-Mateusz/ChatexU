package com.example.chatexu.domain.model

import android.graphics.Bitmap

data class User(
    val id: String,
    val nickname: String,
    val username: String,
    val icon: Bitmap? = null,
    val friends: List<String> = emptyList(),
)
