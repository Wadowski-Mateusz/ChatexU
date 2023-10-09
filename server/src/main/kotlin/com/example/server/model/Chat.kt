package com.example.server.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("chats")
data class Chat(
    @Id
    @Field("id")
    val chatId: ObjectId = ObjectId(),
    val participants: List<ObjectId>,
    val lastMessage: ObjectId,
//    val bannedBy: List<ObjectId>,
//    val files: List<String>, // list of URIs
//    val chatIcon: ?,
    val typeOfChat: ChatType

)

sealed class ChatType(val type: String) {
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


