package com.example.server.dto

data class FriendDto(
    val id: String,
    val nickname: String,
    val nicknameFromChat: String,
    val icon: ByteArray,
)

