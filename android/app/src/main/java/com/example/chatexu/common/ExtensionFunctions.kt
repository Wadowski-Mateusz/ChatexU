package com.example.chatexu.common

import org.mongodb.kbson.ObjectId

// Returns the same ObjectId every time
fun ObjectId.default(): ObjectId = ObjectId("000000000000000000000000")
