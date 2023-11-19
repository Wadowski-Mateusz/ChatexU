package com.example.chatexu.common

import org.mongodb.kbson.ObjectId
import java.lang.Exception

// Returns the same ObjectId every time
fun ObjectId.default(): ObjectId = ObjectId("000000000000000000000000")

fun ObjectId.isStringValid(value: String): Boolean {
    return try {
        ObjectId(value)
        true
    } catch (e: IllegalArgumentException) {
        false
    } catch (e: Exception) {
        throw e
    }
}
