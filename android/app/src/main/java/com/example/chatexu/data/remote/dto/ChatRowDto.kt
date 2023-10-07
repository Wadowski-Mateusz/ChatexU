package com.example.chatexu.data.remote.dto


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.example.chatexu.domain.model.ChatRow
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.lang.Exception
import java.time.Instant

@JsonClass(generateAdapter = true)
data class ChatRowDto(
    @Json(name = "chatId")
    val chatId: String,
    @Json(name = "chatName")
    val chatName: String,
    @Json(name = "lastMessage")
    val lastMessage: String,
    @Json(name = "timestamp")
    val timestamp: String,
    @Json(name = "icon")
    val icon: String,
) {
    fun toChatRow(): ChatRow {
        val dtoAsString: String = icon
        val byteArrayData: ByteArray = Base64.decode(dtoAsString, Base64.DEFAULT)
        val bitmap: Bitmap? = BitmapFactory.decodeByteArray(byteArrayData, 0, byteArrayData.size)
        return ChatRow(chatId, chatName, lastMessage, Instant.parse(timestamp), bitmap)
    }
}

