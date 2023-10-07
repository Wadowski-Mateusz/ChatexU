package com.example.server.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.Instant

@Document("messages")
data class Message(
    @Id
    @Field("id")
    val messageId: ObjectId = ObjectId(),
    val senderId: ObjectId,
    val chatId: ObjectId,
    @Indexed
    val timestamp: Instant,
    val messageContent: MessageContent,

    ) {
    data class Builder(
        var messageId: ObjectId? = null,
        var senderId: ObjectId? = null,
        var chatId: ObjectId? = null,
        var timestamp: Instant? = null,
        var messageContent: MessageContent? = null,
    ) {
        fun messageId(messageId: ObjectId) = apply { this.messageId = messageId }
        fun senderId(senderId: ObjectId) = apply { this.senderId = senderId }
        fun chatId(chatId: ObjectId) = apply { this.chatId = chatId }
        fun timestamp(timestamp: Instant) = apply { this.timestamp = timestamp }
        fun messageContent(messageContent: MessageContent) = apply { this.messageContent = messageContent }
        fun build() = Message(messageId!!, senderId!!, chatId!!, timestamp!!, messageContent!!)
        fun fastBuild() =
                messageId(ObjectId())
                .senderId(ObjectId())
                .chatId(ObjectId())
                .timestamp(Instant.now())
                .messageContent(MessageContent.Text("Fast Build"))
                .build()
    }
}



sealed class MessageContent(val type: String) {

    data class Text(val text: String) : MessageContent(type = TYPE_TEXT)
    data class Resource(val uri: String) : MessageContent(type = TYPE_RESOURCE)

    private companion object {
        const val TYPE_TEXT = "text"
        const val TYPE_RESOURCE = "resource"

        @JsonCreator
        @JvmStatic
        private fun fromJson(
            @JsonProperty("type") type: String,
            @JsonProperty("text") text: String?,
            @JsonProperty("uri") uri: String?,
        ): MessageContent {
            return when (type) {
                TYPE_TEXT -> Text(text!!)
                TYPE_RESOURCE -> Resource(uri!!)
                else -> throw IllegalArgumentException("Invalid Content type")
            }
        }
    }


}
