package com.example.chatexu.domain.model

import com.example.chatexu.common.Constants
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class Chat(
    val chatId: String,
    val lastMessage: String,
    val typeOfChat: ChatType,
    val created: Instant,

    val participants: List<String> = emptyList(),
//    val lastViewedBy: Map<String, Instant> = emptyMap(),
//    val mutedBy: Map<String, Instant> = emptyMap(),

    ) {

    companion object {
        fun emptyChat(): Chat = Chat(
            chatId = Constants.ID_DEFAULT,
            lastMessage = Constants.ID_DEFAULT,
            typeOfChat = ChatType.UserToUser(),
            created = Instant.MIN
        )
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