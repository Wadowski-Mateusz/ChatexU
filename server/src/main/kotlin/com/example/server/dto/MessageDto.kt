package com.example.server.dto

import com.example.server.model.MessageContent


data class MessageDto(
    val messageId: String,
    val senderId: String,
    val chatId: String,
    val timestamp: String,
    val messageContent: MessageContent,
    val isEdited: Boolean,
    val isDeletedForViewer: Boolean,
    val answerTo: String?,
    ) {

//     Jackson needs either a no-argument constructor (default constructor) or properly annotated properties.
//    constructor() : this("", "", "", Instant.now(), Content.TextContent(""))

}
