package com.example.server.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.Instant

@Document("friend_requests")
data class FriendRequest(
    @Id
    @Field("_id")
    val requestId: ObjectId = ObjectId(),
    val senderId: ObjectId,
    val recipientId: ObjectId,
    val created: Instant
)
