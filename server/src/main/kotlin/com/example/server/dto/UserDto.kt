package com.example.server.dto

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Field

data class UserDto(

    val userId: String,
    val nickname: String,
    val username: String,
    val email: String,
    val password: String,
    val profilePictureUri: String,
    val friends: Set<String>,
    val blockedUsers: Set<String>,
    val profilePicture: ByteArray, // TODO null for default
)


