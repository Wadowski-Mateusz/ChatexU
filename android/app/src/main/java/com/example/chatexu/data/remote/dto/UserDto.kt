package com.example.chatexu.data.remote.dto


import com.fasterxml.jackson.annotation.JsonProperty

data class UserDto(
    @JsonProperty("userId")
    val userId: String,
    @JsonProperty("nickname")
    val nickname: String,
    @JsonProperty("username")
    val username: String,
    @JsonProperty("email")
    val email: String,
    @JsonProperty("password")
    val password: String,
    @JsonProperty("friends")
    val friends: List<Any>,
    @JsonProperty("blockedUsers")
    val blockedUsers: List<Any>,
    @JsonProperty("profilePictureUri")
    val profilePictureUri: String,
    @JsonProperty("profilePicture")
    val profilePicture: String,
)