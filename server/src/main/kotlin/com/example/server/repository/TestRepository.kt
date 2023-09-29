package com.example.server.repository

import com.example.server.model.TestDoc
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository


interface TestRepository : MongoRepository<TestDoc, ObjectId> {

    fun findByTestDocId(restaurantId: String): TestDoc?

}