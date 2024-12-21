package com.example.chatexu.presentation.auth

import com.example.chatexu.common.Constants
import com.example.chatexu.domain.model.User

data class AuthState(
    var userId: String = Constants.ID_DEFAULT,
    var users: List<User> = emptyList(),
    var isLoading: Boolean = false,
    var error: String = "",

    var loginPage: Boolean = true,
    var registerPage: Boolean = false,

    var badLoginData: Boolean = false,

    var badRegisterEmail: Boolean = false,
    var badRegisterNickname: Boolean = false,
    var badRegisterPasswords: Boolean = false,

    var registerStatus: Boolean = false,
    var loginStatus: Boolean = false,
)
