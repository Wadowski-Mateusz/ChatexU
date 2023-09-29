package com.example.server.model

import org.bson.types.ObjectId
import org.jetbrains.annotations.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("users")
data class User(

    @Id
    @Field("id")
    val userId: ObjectId = ObjectId(),
    @NotNull
    val nickname: String = ""

)
