package com.example.chatexu.data.remote.dto

import com.example.chatexu.domain.model.ChatType
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

// TODO not tested
data class ChatDto @JsonCreator constructor(
    @JsonProperty("chatId")
        val chatId: String,
    @JsonProperty("lastMessage")
        val lastMessage: String,
    @JsonProperty("typeOfChat")
        val typeOfChat:ChatType,
    @JsonProperty("participants")
        val participants: Set<String>,
    @JsonProperty("lastViewedBy")
        val lastViewedBy:String,
    @JsonProperty("mutedBy")
        val mutedBy: Map<String, String>,
)

//
//val chatId: String,
//val lastMessage: String,
//val typeOfChat: ChatType,
//
//val participants: Set<String>,
//val lastViewedBy: Map<String, String> = emptyMap(),  // when user has been seeing chat last chat, to show him how many unread messages he has
//val mutedBy: Map<String, String> = emptyMap(),       // how long conversation is muted for user