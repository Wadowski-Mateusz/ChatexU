package com.example.chatexu.presentation.user_options

import com.example.chatexu.common.Constants
import com.example.chatexu.domain.model.User

data class UserOptionsState(
    val currentUserId: String = Constants.ID_DEFAULT,
    val isLoading: Boolean = false,
    val error: String = "",
    val jwt: String = "",

    val currentUser: User = User.emptyUser(),
    val nicknameUpdateFailure: Boolean = false,

)


/**
 * set profile picture
 * set nickname
 */
