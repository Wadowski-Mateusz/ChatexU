package com.example.chatexu.presentation.create_chat

import com.example.chatexu.domain.model.Friend

data class CreateChatState(
    val userId: String = "",

    val friends: List<Friend> = emptyList(),
    val matchingFriends: List<Friend> = friends,

    val isLoading: Boolean = false,
    val error: String = ""
)