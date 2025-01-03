package com.example.chatexu.domain.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.chatexu.R
import com.example.chatexu.common.Constants

data class User(
    val id: String,
    val nickname: String,
    val username: String,
    val icon: Bitmap? = null,
    val friends: List<String> = emptyList(),
    val role: String = ""
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
