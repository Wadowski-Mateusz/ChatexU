package com.example.chatexu.domain.model

import com.example.chatexu.common.default
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.mongodb.kbson.ObjectId
import java.time.Instant



data class Message(
    val messageId: ObjectId = ObjectId(),
    val senderId: ObjectId,
    val chatId: ObjectId,
    val timestamp: Instant,
    val messageType: MessageType,
    val isEdited: Boolean = false,
    val deletedBy: List<String> = emptyList(),
    val answerTo: ObjectId = ObjectId().default(),
)


// why it doesn't work with val?
// if it is val, finding in the database throw error
sealed class MessageType(var type: String) {

    data class Text(val text: String): MessageType(type = TYPE_TEXT)
    data class Resource(val uri: String): MessageType(type = TYPE_RESOURCE)
    class Deleted: MessageType(type = TYPE_DELETED)
    class Initialization: MessageType(type = TYPE_INIT) // first message in the chat, that or null as "last message"


    private companion object {
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