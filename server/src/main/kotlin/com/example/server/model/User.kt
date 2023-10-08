package com.example.server.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.Instant

@Document("users")
data class User(

    @Id
    @Field("id")
    val userId: ObjectId = ObjectId(),
    // to use with custom getter for fast ObjectId.toHexString()
//    private val _userId: ObjectId = ObjectId(),

    @Indexed
    val nickname: String,

    @Indexed
    val email: String,
    val login: String,
    val password: String, // TODO change to hash

//    val lastPassword: String = "",
//    val passwordResetTime: Instant = Instant.MIN

    val profilePictureUri: String,

    val listOfBlockedUsers: List<String> = emptyList(),
    val lastTimeOnline: Instant = Instant.MIN,


//    val listOfFriends: List<String> = emptyList(),
//    val status: ,    // sealed class, enum?
//    val tokens // sessions , security
//    val role(s) //
//    val settings // languages

) {

//    val userId: String
//        get() = _userId.toHexString()
}
