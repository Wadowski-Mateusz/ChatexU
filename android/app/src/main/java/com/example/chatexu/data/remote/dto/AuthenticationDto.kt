package com.example.chatexu.data.remote.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class AuthenticationDto @JsonCreator constructor(
    @JsonProperty("token")
    val token: String,
    @JsonProperty("userId")
    val userId: String
)