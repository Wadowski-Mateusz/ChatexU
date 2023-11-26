package com.example.server.repository

import com.example.server.model.FriendRequest
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface FriendRequestRepository: MongoRepository<FriendRequest, String> {

    fun findAllByRecipientId(recipientId: ObjectId): Set<FriendRequest>
    fun findAllBySenderId(senderId: ObjectId): Set<FriendRequest>

    @Query(
        value = "{\$or: [ { 'senderId': ?0, 'recipientId': ?1 }, { 'senderId': ?1, 'recipientId': ?0 } ]}",
        exists = true
    )
    fun existsByUserIds(senderId: ObjectId, recipientId: ObjectId): Boolean

}