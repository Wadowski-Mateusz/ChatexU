package com.example.server.dto

import com.example.server.model.User

data class UserDto(
    val userId: String,
)
fun User.toDto(): UserDto {
    return UserDto(
        userId = userId.toHexString()
    )
}

