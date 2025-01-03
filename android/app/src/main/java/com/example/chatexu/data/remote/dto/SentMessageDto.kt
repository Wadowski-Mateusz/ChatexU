package com.example.chatexu.data.remote.dto

import com.example.chatexu.domain.model.MessageType
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class SentMessageDto @JsonCreator constructor(
    @JsonProperty("messageType")
    val messageType: MessageType,
    @JsonProperty("senderId")
    val senderId: String,
    @JsonProperty("chatId")
    val chatId: String,
//    @JsonProperty("replyTo")
//    val replyTo: String,
)