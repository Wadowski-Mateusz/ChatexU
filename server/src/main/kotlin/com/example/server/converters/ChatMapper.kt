package com.example.server.converters

import com.example.server.dto.ChatDto
import com.example.server.model.Chat

object ChatMapper {
    fun toDto(chat: Chat): ChatDto {
        return ChatDto(
            chatId = chat.chatId.toHexString(),
            lastMessage = chat.lastMessageId.toHexString(),
            typeOfChat = chat.typeOfChat,
            participants = chat.participants.map{ it.toHexString() }.toSet()
        )
    }
}