package com.example.chatexu.data.remote.dto

import com.example.chatexu.domain.model.MessageType
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class MessageDto @JsonCreator constructor(
    @JsonProperty("messageId")
    val messageId: String,
    @JsonProperty("senderId")
    val senderId: String,
    @JsonProperty("chatId")
    val chatId: String,
    @JsonProperty("timestamp")
    val timestamp: String,
    @JsonProperty("messageType")
    val messageType: MessageType,
//    @JsonProperty("isEdited")
//    val isEdited: Boolean,
//    @JsonProperty("isDeletedForViewer")
//    val isDeletedForViewer: Boolean, // TODO unused?
//    @JsonProperty("replyTo")
//    val replyTo: String
) {

//     Jackson needs either a no-argument constructor (default constructor) or properly annotated properties.
//    constructor() : this("", "", "", Instant.now(), Content.TextContent(""))

}
