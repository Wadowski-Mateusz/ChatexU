package com.example.chatexu.data.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant
import java.util.UUID

@JsonClass(generateAdapter = true)
data class ChatRow(
    @Json(name = "chatId")
    val chatId: UUID,
    @Json(name = "chatName")
    val chatName: String,
    @Json(name = "lastMessage")
    val lastMessage: String,
    @Json(name = "timestamp")
    val timestamp: Instant,
) {


    data class Builder(
        var chatId: UUID? = null,
        var chatName: String? = null,
        var lastMessage: String? = null,
        var timestamp: Instant? = null,
    ) {

        fun chatId(chatId: UUID) = apply { this.chatId = chatId }
        fun chatName(chatName: String) = apply { this.chatName = chatName }
        fun lastMessage(lastMessage: String) = apply { this.lastMessage = lastMessage }
        fun timestamp(timestamp: Instant) = apply { this.timestamp = timestamp }
        fun build() = ChatRow(chatId!!, chatName!!, lastMessage!!, timestamp!!)
        fun fastBuild() =
            chatId(UUID.randomUUID())
                .chatName("fastBuild: chatName")
                .lastMessage("fastBuild: lastMessage")
                .timestamp(Instant.now())
                .build()
    }

}
