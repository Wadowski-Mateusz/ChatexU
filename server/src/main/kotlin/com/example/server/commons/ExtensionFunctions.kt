package com.example.server.commons

import org.bson.types.ObjectId
import java.util.*

// Returns the same ObjectId every time
fun ObjectId.default(): ObjectId = ObjectId.getSmallestWithDate(Date(0))
fun String.replaceFirsCaseWithUpper(): String = this.replaceFirstChar { it.uppercase() }
