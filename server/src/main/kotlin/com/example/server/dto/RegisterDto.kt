package com.example.server.dto

data class RegisterDto(
    val nickname: String,
    val username: String,
    val email: String,
    val password: String,
)
