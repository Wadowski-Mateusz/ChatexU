package com.example.server.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("users")
data class User(

    @Id
    @Field("_id")
    val userId: ObjectId = ObjectId(),

    // visible by other users
    @Indexed(unique = true)
    val nickname: String,

    // Authentication only, not visible by other users
    @Indexed(unique = true)
    val username: String,

    @Indexed(unique = true)
    val email: String,
    val password: String, // TODO change to hash
    val profilePictureUri: String,

    val friends: Set<String>,
    val blockedUsers: Set<String>,

//    val lastTimeOnline: Instant = Instant.MIN, // when to update it?
//    val lastPassword: String = "",
//    val passwordResetTime: Instant = Instant.MIN
//    val status: ,    // sealed class, enum?
//    val tokens // sessions , security
//    val role(s) //
//    val settings // languages

)
