package com.example.server.dto

import org.bson.types.ObjectId
import java.time.Instant
import java.util.*


data class ChatViewDto(
    val chatId: String,
    val chatName: String,
    val lastMessage: String,
    val timestamp: Instant,
    //val icon: ?,
//    val isMuted: Boolean,
    ) {


    data class Builder(
        var chatId: String? = null,
        var chatName: String? = null,
        var lastMessage: String? = null,
        var timestamp: Instant? = null,
        ) {

        fun chatId(chatId: String) = apply { this.chatId = chatId }
        fun chatName(chatName: String) = apply { this.chatName = chatName }
        fun lastMessage(lastMessage: String) = apply { this.lastMessage = lastMessage }
        fun timestamp(timestamp: Instant) = apply { this.timestamp = timestamp }
        fun build() = ChatViewDto(chatId!!, chatName!!, lastMessage!!, timestamp!!)
        fun fastBuild() =
            chatId(ObjectId().toHexString())
            .chatName("fastBuild: chatName")
            .lastMessage("fastBuild: lastMessage")
            .timestamp(Instant.now())
            .build()
    }

}
