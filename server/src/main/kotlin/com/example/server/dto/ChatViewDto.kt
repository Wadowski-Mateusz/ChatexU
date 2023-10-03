package com.example.server.dto

import java.time.Instant
import java.util.*


data class ChatViewDto(
    val chatId: UUID,
    val chatName: String,
    val lastMessage: String,
    val timestamp: Instant,
    //val icon: ?,
//    val isMuted: Boolean,
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
        fun build() = ChatViewDto(chatId!!, chatName!!, lastMessage!!, timestamp!!)
        fun fastBuild() =
            chatId(UUID.randomUUID())
            .chatName("fastBuild: chatName")
            .lastMessage("fastBuild: lastMessage")
            .timestamp(Instant.now())
            .build()
    }

}
