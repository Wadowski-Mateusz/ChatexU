package com.example.server.dto

import com.example.server.model.MessageType
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