package com.example.server.dto

import com.example.server.commons.default
import com.example.server.model.MessageType
import org.bson.types.ObjectId


data class MessageDto(
    val messageId: String,
    val senderId: String,
    val chatId: String,
    val timestamp: String,
    val messageType: MessageType,
    val isEdited: Boolean,
    val isDeletedForViewer: Boolean,
    val answerTo: String = ObjectId().default().toHexString(),
    ) {

//     Jackson needs either a no-argument constructor (default constructor) or properly annotated properties.
//    constructor() : this("", "", "", Instant.now(), Content.TextContent(""))

}
