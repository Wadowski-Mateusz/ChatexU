package com.example.server.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("chats")
data class Chat(
    @Id
    val chatId: ObjectId = ObjectId(),
    val lastMessageId: ObjectId,
    val typeOfChat: ChatType,
    val created: Instant,
    val participants: List<ObjectId>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Chat

        if (chatId != other.chatId) return false
        if (lastMessageId != other.lastMessageId) return false
        if (typeOfChat.javaClass != other.typeOfChat.javaClass) return false
        if (created != other.created) return false
        if (participants != other.participants) return false

        return true
    }

    override fun hashCode(): Int {
        var result = chatId.hashCode()
        result = 31 * result + lastMessageId.hashCode()
        result = 31 * result + typeOfChat.hashCode()
        result = 31 * result + created.hashCode()
        result = 31 * result + participants.hashCode()
        return result
    }
}

sealed class ChatType(var type: String) {
    class UserToUser: ChatType(USER_TO_USER)
    data class Group(val iconUri: String): ChatType(GROUP)

    private companion object {
        const val USER_TO_USER = "user to user"
        const val GROUP = "group"

        @JsonCreator
        @JvmStatic
        private fun fromJson(
            @JsonProperty("type") type: String,
            @JsonProperty("iconUri") iconUri: String?,
        ): ChatType {
            return when (type) {
                USER_TO_USER -> UserToUser()
                GROUP -> Group(iconUri!!)
                else -> throw IllegalArgumentException("Invalid chat type")
            }
        }
    }

}
