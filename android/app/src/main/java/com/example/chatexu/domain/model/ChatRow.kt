package com.example.chatexu.domain.model

import android.graphics.Bitmap
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant

@JsonClass(generateAdapter = true)
data class ChatRow(
    @field:Json(name = "chatId")
    val chatId: String,
    @field:Json(name = "chatName")
    val chatName: String,
    @field:Json(name = "lastMessage")
    val lastMessage: MessageType,
    @field:Json(name = "timestamp")
    val timestamp: Instant,
    val icon: Bitmap? = null,
)