package com.example.server.exceptions

class DataAlreadyInTheDatabaseException(
    val inDatabaseFlag: Int,
    message: String? = null,
    cause: Throwable? = null
): Exception(message, cause) {


    constructor() : this(inDatabaseFlag = 0)
    constructor(message: String) : this(inDatabaseFlag = 0, message = message)
    constructor(message: String, inDatabaseFlag: Int) : this(inDatabaseFlag, message)
    constructor(message: String, cause: Throwable) : this(inDatabaseFlag = 0, message = message, cause = cause)
    constructor(cause: Throwable) : this(inDatabaseFlag = 0, cause = cause)
}