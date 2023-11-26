package com.example.server.dto

data class UserViewDto (
    val userId: String,
    val nickname: String,
    val icon: ByteArray,
)