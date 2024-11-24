package com.example.server.dto

data class UserViewDto (
    val userId: String,
    val nickname: String,
    val username: String,
    val icon: ByteArray,
)