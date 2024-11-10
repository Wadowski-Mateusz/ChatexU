package com.example.chatexu.data.remote.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class UserViewDto @JsonCreator constructor(
    @JsonProperty("userId")
    val id: String,
    @JsonProperty("nickname")
    val nickname: String,
    @JsonProperty("username")
    val username: String,
    @JsonProperty("icon")
    val icon: String,
)