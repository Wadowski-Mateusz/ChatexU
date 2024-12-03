package com.example.server.commons

import com.example.server.model.User
import org.bson.types.ObjectId
import java.io.File
import java.util.*

// Returns the same ObjectId every time
fun ObjectId.default(): ObjectId = ObjectId.getSmallestWithDate(Date(0))
fun String.replaceFirsCaseWithUpper(): String = this.replaceFirstChar { it.uppercase() }

