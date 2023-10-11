package com.example.server.model

import com.example.server.commons.default
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

@Document("messages")
data class Message(
    @Id
    val messageId: ObjectId = ObjectId(),
    val senderId: ObjectId,
    @Indexed
    val chatId: ObjectId,
    @Indexed
    val timestamp: Instant,
    val messageType: MessageType,
    val isEdited: Boolean = false,
    val deletedBy: List<String>,
    val answerTo: ObjectId = ObjectId().default(),
    )


// why it doesn't work with val?
// if it is val, finding in the database throw error
sealed class MessageType(var type: String) {

    data class Text(val text: String): MessageType(type = TYPE_TEXT)
    data class Resource(val uri: String): MessageType(type = TYPE_RESOURCE)
    class Deleted: MessageType(type = TYPE_DELETED)
    class Initialization: MessageType(type = TYPE_INIT) // first message in the chat, that or null as "last message"


    companion object {
        const val TYPE_TEXT = "text"
        const val TYPE_RESOURCE = "resource"
        const val TYPE_DELETED = "deleted"
        const val TYPE_INIT = "init"

        @JsonCreator
        @JvmStatic
        private fun fromJson(
            @JsonProperty("type") type: String,
            @JsonProperty("text") text: String?,
            @JsonProperty("uri") uri: String?,
        ): MessageType {
            return when (type) {
                TYPE_TEXT -> Text(text!!)
                TYPE_RESOURCE -> Resource(uri!!)
                TYPE_DELETED -> Deleted()
                TYPE_INIT -> Initialization()
                else -> throw IllegalArgumentException("Invalid Content type")
            }
        }
    }

}