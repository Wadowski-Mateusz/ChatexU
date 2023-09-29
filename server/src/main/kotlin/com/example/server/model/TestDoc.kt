package com.example.server.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*

@Document("test_docs")
data class TestDoc(

    @Id
    @Field("id")
    val testDocId: ObjectId  = ObjectId(),

    val testMsg: String = "test",

//    @Id
//    val testDocId: UUID = UUID.randomUUID()
)