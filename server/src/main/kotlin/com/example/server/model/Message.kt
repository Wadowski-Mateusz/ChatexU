package com.example.server.model

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

    val sender: ObjectId,
    val timestamp: Instant,
    val messageContent: Content,
)


sealed class Content

data class TextContent(val text: String) : Content()
data class AttachmentContent(val attachmentUri: String) : Content()
data class MixedContent(val components: List<Content>) : Content() // txt + video/img; multiple images/videos; images+videos/gifs
