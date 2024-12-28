package com.example.server.dto

import com.example.server.model.ChatType

data class ChatDto(
    val chatId: String,
    val lastMessage: String,
    val typeOfChat: ChatType,

    val participants: Set<String>,
//    val lastViewedBy: Map<String, String> = emptyMap(),  // when user has been seeing chat last chat, to show him how many unread messages he has
//    val mutedBy: Map<String, String> = emptyMap(),       // how long conversation is muted for user
)