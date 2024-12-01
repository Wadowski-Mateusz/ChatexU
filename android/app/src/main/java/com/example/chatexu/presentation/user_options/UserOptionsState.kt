package com.example.chatexu.presentation.user_options

import com.example.chatexu.common.Constants
import com.example.chatexu.domain.model.User

data class UserOptionsState(
    val isLoading: Boolean = false,
    val error: String = "",

    val currentUserId: String = Constants.ID_DEFAULT,
    val currentUser: User = User.emptyUser()

)


/**
 * set profile picture
 * set nickname
 */
