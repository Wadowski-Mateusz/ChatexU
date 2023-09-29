package com.example.server.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("users")
data class User(

    @Id
    @Field("id")
    val userId: ObjectId = ObjectId(),

    val nickname: String = "",
    val email: String = "",
    val password: String = "",
    val login: String = "",

    val settings: Settings = Settings(),
    // val blockedUsers: List<ObjectId>,
)


data class Settings(
    val notifications: Notifications = Notifications.OFF,
)

enum class Notifications {
    ALL,
    POP_UP,
    VIBRATION,
    SOUND,
    OFF,
    //BLOCK_SCREEN_ONLY_NUMBER_OF_MESSAGES,
}