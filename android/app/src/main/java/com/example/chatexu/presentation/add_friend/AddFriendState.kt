package com.example.chatexu.presentation.add_friend

import com.example.chatexu.domain.model.FriendRequest
import com.example.chatexu.domain.model.User

data class AddFriendState(
    val currentUserId: String = "",
    val jwt: String = "",

    val users: List<User> = emptyList(),

    val requests: List<FriendRequest> = emptyList(),

    val incomingRequests: Set<FriendRequest> = emptySet(),
    val outgoingRequests: Set<FriendRequest> = emptySet(),
    val acceptedRequests: Set<FriendRequest> = emptySet(),
    val friends: List<String> = emptyList(), // friendIDs


    val isLoading: Boolean = true,
    val error: String = ""
)