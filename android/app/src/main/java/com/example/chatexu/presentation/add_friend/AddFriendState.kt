package com.example.chatexu.presentation.add_friend

import com.example.chatexu.domain.model.FriendRequest
import com.example.chatexu.domain.model.User

data class AddFriendState(
    val userId: String = "",

    val users: List<User> = emptyList(),
    val matchingUsers: List<User> = emptyList(),
    val addedUsers: List<String> = emptyList(),
    val requests: List<FriendRequest> = emptyList(),

    val incomingRequests: Map<User, FriendRequest> = emptyMap(),
    val outgoingRequests: Map<User, FriendRequest> = emptyMap(),
    val friends: List<User> = emptyList(),
    val strangers: List<User> = emptyList(),

    val matchingIncomingRequests: Map<User, FriendRequest> = emptyMap(),
    val matchingOutgoingRequests: Map<User, FriendRequest> = emptyMap(),
    val matchingFriends: List<User> = emptyList(),
    val matchingStrangers: List<User> = emptyList(),

    val isLoading: Boolean = true,
    val error: String = ""
)