package com.example.server.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
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
    @Indexed
    val chatId: ObjectId,
    @Indexed
    val timestamp: Instant,
    val messageContent: MessageContent,
    val isEdited: Boolean = false,
    val deletedBy: List<String>,
    val answerTo: String? = null,
    )



sealed class MessageContent(val type: String) {

    data class Text(val text: String): MessageContent(type = TYPE_TEXT)
    data class Resource(val uri: String): MessageContent(type = TYPE_RESOURCE)
    class Deleted: MessageContent(type = TYPE_DELETED)

    private companion object {
        const val TYPE_TEXT = "text"
        const val TYPE_RESOURCE = "resource"
        const val TYPE_DELETED = "resource"

        @JsonCreator
        @JvmStatic
        private fun fromJson(
            @JsonProperty("type") type: String,
            @JsonProperty("text") text: String?,
            @JsonProperty("uri") uri: String?,
        ): MessageContent {
            return when (type) {
                TYPE_RESOURCE -> Resource(uri!!)
                TYPE_TEXT -> Text(text!!)
                TYPE_DELETED -> Deleted()
                else -> throw IllegalArgumentException("Invalid Content type")
            }
        }
    }


}
