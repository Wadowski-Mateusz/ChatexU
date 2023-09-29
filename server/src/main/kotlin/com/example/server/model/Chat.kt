package com.example.server.model

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

)
