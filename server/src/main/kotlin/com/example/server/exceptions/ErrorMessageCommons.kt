package com.example.server.exceptions

/**
 * Standard error messages
 * */

enum class ClassName(val className: String) {
    CHAT("`Chat`"),
    FRIEND_REQUEST("`FriendRequest`"),
    MESSAGE("`Message`"),
    USER("`User`"),
}

enum class Field(val field: String) {
    EMAIL("`email`"),
    ID("`ID`"),
    LOGIN("`Login`"),
    NICKNAME("`Nickname`"),
    USER_ID("`UserId`"),
    USERNAME("`Username`"),
    PASSWORD("`Password`"),
}

object ErrorMessageCommons {
    fun notFound(className: ClassName, field: Field, value: String, functionName: String = "") =
        if (functionName.isBlank())
            "${className.className} has not been found by `${field.field}` with value `${value}`."
        else
            "${className.className} has not been found by `${field.field}` with value `${value}`. Thrown by `${functionName}`"

    fun isBlank(field: Field, functionName: String = "") =
        if (functionName.isBlank())
            "${field.field} is blank."
        else
            "${field.field} is blank. Thrown by `${functionName}`"

    fun objectIdIsNotValid(objectIdValue: String, className: ClassName, functionName: String = ""): String =
        if (functionName.isBlank())
            "`${objectIdValue}` of class `${className.className}` is not valid."
        else
            "`${objectIdValue}` of class `${className.className}` is not valid. Thrown by `${functionName}`"


}