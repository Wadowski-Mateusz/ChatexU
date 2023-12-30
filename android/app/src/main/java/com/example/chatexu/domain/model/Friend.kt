package com.example.chatexu.domain.model

import android.graphics.Bitmap

data class Friend(
    val id: String,
    val nickname: String,
    val nicknameFromChat: String,
    val icon: Bitmap? = null,
)
