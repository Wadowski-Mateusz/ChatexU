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
)

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
