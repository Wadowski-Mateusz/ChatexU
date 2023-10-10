package com.example.server.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("users")
data class User(

    @Id
    @Field("id")
    val userId: ObjectId = ObjectId(),

    @Indexed
    val nickname: String,

    @Indexed
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
