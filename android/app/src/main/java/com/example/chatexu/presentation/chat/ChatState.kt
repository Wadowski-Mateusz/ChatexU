package com.example.chatexu.presentation.chat

import com.example.chatexu.domain.model.Message

data class ChatState (
    var isLoading: Boolean = false,
    var messages: List<Message> = emptyList(),
    var chatId: String = "",
    var error:String = ""
)