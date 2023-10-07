package com.example.chatexu.presentation.chat_list

import com.example.chatexu.domain.model.ChatRow

data class ChatListState(
    val isLoading: Boolean = false,
    val chatRows: List<ChatRow> = emptyList(),
    val error: String = ""
)