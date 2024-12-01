package com.example.chatexu.domain.model

import android.graphics.Bitmap
import com.example.chatexu.common.Constants

data class User(
    val id: String,
    val nickname: String,
    val username: String,
    val icon: Bitmap? = null,
    val friends: List<String> = emptyList(),
) {
        companion object {
            fun emptyUser() : User {
               return User(
                    id = Constants.ID_DEFAULT,
                    nickname = "",
                    username = "",
                )
            }
        }
}
