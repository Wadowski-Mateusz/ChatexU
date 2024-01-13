package com.example.chatexu.data.remote.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class FriendRequestDto @JsonCreator constructor(

    @JsonProperty("requestId")
    val requestId: String,

    @JsonProperty("senderId")
    val senderId: String,

    @JsonProperty("recipientId")
    val recipientId: String,

    @JsonProperty("created")
    val created: String
)