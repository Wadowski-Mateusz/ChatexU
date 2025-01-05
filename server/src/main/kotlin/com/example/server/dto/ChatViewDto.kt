package com.example.server.dto

import com.example.server.model.MessageType
import java.time.Instant

data class ChatViewDto(
    val chatId: String,
    val chatName: String,
    val lastMessageType: MessageType,
    val lastMessageSender: String,
    val timestamp: Instant,
    val icon: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChatViewDto

        if (chatId != other.chatId) return false
        if (chatName != other.chatName) return false
        if (lastMessageType != other.lastMessageType) return false
        if (lastMessageSender != other.lastMessageSender) return false
        if (timestamp != other.timestamp) return false
        if (!icon.contentEquals(other.icon)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = chatId.hashCode()
        result = 31 * result + chatName.hashCode()
        result = 31 * result + lastMessageType.hashCode()
        result = 31 * result + lastMessageSender.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + icon.contentHashCode()
        return result
    }
}