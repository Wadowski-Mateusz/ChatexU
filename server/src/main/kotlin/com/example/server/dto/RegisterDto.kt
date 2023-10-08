package com.example.server.dto

data class RegisterDto(
    val nickname: String,
    val email: String,
    val login: String,
    val password: String,
)
