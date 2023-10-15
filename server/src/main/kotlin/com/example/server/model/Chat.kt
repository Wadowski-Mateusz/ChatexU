package com.example.server.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.Instant

@Document("chats")
data class Chat(
    @Id
    val chatId: ObjectId = ObjectId(),
    val lastMessage: ObjectId,
    val typeOfChat: ChatType,
    val created: Instant,

    val participants: List<ObjectId>,
    val lastViewedBy: Map<ObjectId, Instant> = emptyMap(),  // when user has been seeing chat last chat, to show him how many unread messages he has
    val mutedBy: Map<ObjectId, Instant> = emptyMap(),       // how long conversation is muted for user


//    val bannedBy: Set<ObjectId>, // users who banned conversation
//    val files: List<String>, // list of URIs

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
