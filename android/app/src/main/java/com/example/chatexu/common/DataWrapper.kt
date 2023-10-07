package com.example.chatexu.common

sealed class DataWrapper<T>(
    val data: T? = null,
    val message: String? = null
) {

    class Success<T>(data: T) : DataWrapper<T>(data)
    class Error<T>(message: String, data: T? = null) : DataWrapper<T>(data, message)
    class Loading<T>(data: T? = null) : DataWrapper<T>(data)

}