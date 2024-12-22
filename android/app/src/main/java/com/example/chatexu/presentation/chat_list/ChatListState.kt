package com.example.chatexu.presentation.chat_list

import com.example.chatexu.domain.model.ChatRow

data class ChatListState(
    val userId: String = "",
    val jwt: String = "",
    val isLoading: Boolean = false,
    val error: String = "",

    val chatRows: List<ChatRow> = emptyList(),
)