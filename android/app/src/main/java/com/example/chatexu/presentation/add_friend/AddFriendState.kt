package com.example.chatexu.presentation.add_friend

import com.example.chatexu.domain.model.FriendRequest
import com.example.chatexu.domain.model.User

data class AddFriendState(
    val currentUserId: String = "",
//    val currentUser: User,

    val users: List<User> = emptyList(),
    val matchingUsers: List<User> = emptyList(),
    val requests: List<FriendRequest> = emptyList(),

    val incomingRequests: Map<String, FriendRequest> = emptyMap(), // KEY is userID who sent request
    val outgoingRequests: Map<String, FriendRequest> = emptyMap(), // KEY is userID who sent request
    val acceptedRequests: Set<FriendRequest> = emptySet(),
    val friends: List<String> = emptyList(), // friendIDs
//    val strangers: List<User> = emptyList(),


//    val matchingIncomingRequests: Map<User, FriendRequest> = emptyMap(),
//    val matchingOutgoingRequests: Map<User, FriendRequest> = emptyMap(),
//    val matchingFriends: List<User> = emptyList(),
//    val matchingStrangers: List<User> = emptyList(),

    val isLoading: Boolean = true,
    val error: String = ""
)