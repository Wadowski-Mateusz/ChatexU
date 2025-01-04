package com.example.server.dto

import com.example.server.model.ChatType

data class ChatDto(
    val chatId: String,
    val lastMessage: String,
    val typeOfChat: ChatType,
    val participants: Set<String>,
)