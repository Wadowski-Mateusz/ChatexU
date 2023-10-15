package com.example.chatexu.data.remote.dto

import com.example.chatexu.domain.model.MessageType
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ChatRowDto @JsonCreator constructor(
    @JsonProperty("chatId")
        val chatId: String,
    @JsonProperty("chatName")
        val chatName: String,
    @JsonProperty("lastMessageType")
        val lastMessageType: MessageType,
    @JsonProperty("lastMessageSender")
        val lastMessageSenderId: String,
    @JsonProperty("timestamp")
        val timestamp: String,
    @JsonProperty("icon")
        val icon: String,
)
