package com.example.chatexu.data.remote.dto

import com.example.chatexu.common.Constants
import com.example.chatexu.domain.model.MessageType

data class MessageDto(
    val messageId: String,
    val senderId: String,
    val chatId: String,
    val timestamp: String,
    val messageType: MessageType,
    val isEdited: Boolean,
    val isDeletedForViewer: Boolean, // TODO unused?
    val answerTo: String = Constants.ID_DEFAULT,
) {

//     Jackson needs either a no-argument constructor (default constructor) or properly annotated properties.
//    constructor() : this("", "", "", Instant.now(), Content.TextContent(""))

}
