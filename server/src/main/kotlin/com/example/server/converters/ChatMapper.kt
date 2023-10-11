package com.example.server.converters

import com.example.server.dto.ChatDto
import com.example.server.model.Chat

object ChatMapper {
    fun convertToDto(chat: Chat): ChatDto {
        return ChatDto(
            chatId = chat.chatId.toHexString(),
            lastMessage = chat.lastMessage.toHexString(),
            typeOfChat = chat.typeOfChat,
            participants = chat.participants.map{ it.toHexString() }.toSet(),
            lastViewedBy = chat.lastViewedBy.entries.associate { it.key.toHexString() to it.value.toString() },
            mutedBy = chat.mutedBy.entries.associate { it.key.toHexString() to it.value.toString() },
        )
    }
}