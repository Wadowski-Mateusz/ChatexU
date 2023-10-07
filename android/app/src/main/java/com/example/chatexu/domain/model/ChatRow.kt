package com.example.chatexu.domain.model


import android.graphics.Bitmap
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant
import java.util.UUID

@JsonClass(generateAdapter = true)
data class ChatRow(
    @field:Json(name = "chatId")
    val chatId: String,
    @field:Json(name = "chatName")
    val chatName: String,
    @field:Json(name = "lastMessage")
    val lastMessage: String,
    @field:Json(name = "timestamp")
    val timestamp: Instant,

    val icon: Bitmap? = null,


) {


    data class Builder(
        var chatId: String? = null,
        var chatName: String? = null,
        var lastMessage: String? = null,
        var timestamp: Instant? = null,
        var icon: Bitmap? = null,
    ) {

        fun chatId(chatId: String) = apply { this.chatId = chatId }
        fun chatName(chatName: String) = apply { this.chatName = chatName }
        fun lastMessage(lastMessage: String) = apply { this.lastMessage = lastMessage }
        fun timestamp(timestamp: Instant) = apply { this.timestamp = timestamp }
        fun icon(icon: Bitmap) = apply { this.icon = icon }
        fun build() = ChatRow(chatId!!, chatName!!, lastMessage!!, timestamp!!, icon)
        fun fastBuild() =
            chatId(UUID.randomUUID().toString())
                .chatName("fastBuild: chatName")
                .lastMessage("fastBuild: lastMessage")
                .timestamp(Instant.now())
                .build()
    }

}
