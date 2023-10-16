package com.example.chatexu.presentation.auth

import com.example.chatexu.domain.model.User

data class AuthState(
    var users: List<User> = emptyList(),
    var isLoading: Boolean = false,
    var error:String = ""
)
