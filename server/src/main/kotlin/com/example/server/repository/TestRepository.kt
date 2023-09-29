package com.example.server.repository

import com.example.server.model.TestDoc
import org.springframework.data.mongodb.repository.MongoRepository


interface TestRepository : MongoRepository<TestDoc, String> {

    fun findByTestDocId(restaurantId: String): TestDoc?

}