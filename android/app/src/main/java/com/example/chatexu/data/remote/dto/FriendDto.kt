package com.example.chatexu.data.remote.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class FriendDto @JsonCreator constructor(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("nickname")
    val nickname: String,
    @JsonProperty("nicknameFromChat")
    val nicknameFromChat: String,
    @JsonProperty("icon")
    val icon: String,
)
