package com.example.server.model

import com.example.server.dto.ChatViewDto
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.Instant

@Document("messages")
data class Message(
    @Id
    @Field("id")
    val messageId: ObjectId = ObjectId(),

    val senderId: ObjectId,
    val timestamp: Instant,
    val messageContent: Content,

    ) {
    data class Builder(
        var messageId: ObjectId? = null,
        var senderId: ObjectId? = null,
        var timestamp: Instant? = null,
        var messageContent: Content? = null,
    ) {

        fun messageId(messageId: ObjectId) = apply { this.messageId = messageId }
        fun senderId(senderId: ObjectId) = apply { this.senderId = senderId }
        fun timestamp(timestamp: Instant) = apply { this.timestamp = timestamp }
        fun messageContent(messageContent: Content) = apply { this.messageContent = messageContent }
        fun build() = Message(messageId!!, senderId!!, timestamp!!, messageContent!!)
        fun fastBuild() =
                messageId(ObjectId())
                .senderId(ObjectId())
                .timestamp(Instant.now())
                .messageContent(TextContent("Fast Build"))
                .build()
    }
}


sealed class Content

data class TextContent(val text: String) : Content()
data class AttachmentContent(val attachmentUri: String) : Content()
data class MixedContent(val components: List<Content>) : Content() // txt + video/img; multiple images/videos; images+videos/gifs
