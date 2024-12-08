package com.example.chatexu.presentation.chat

import com.example.chatexu.domain.model.Chat
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.model.User

data class ChatState (
    var isLoading: Boolean = false,
    var error: String = "",
    var userId: String = "",
    var chatId: String = "",

    var user: User = User.emptyUser(),
    var recipients: List<User> = emptyList(),
    var messages: List<Message> = emptyList(),
    var chat: Chat = Chat.emptyChat()
)