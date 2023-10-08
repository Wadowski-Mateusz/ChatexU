package com.example.server.exceptions

/**
 * Standard error messages
 * */

object ErrorMessageCommons {
    fun idNotFound(type: String, id: String): String = "${type.replaceFirstChar { it.uppercase() }} with id=$id not found."
}