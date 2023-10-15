package com.example.chatexu.converters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.chatexu.data.remote.dto.ChatRowDto
import com.example.chatexu.domain.model.ChatRow
import java.time.Instant

object ChatMapper {
    fun toChatRow(chatRowDto: ChatRowDto): ChatRow {
        val dtoAsString: String = chatRowDto.icon
        val byteArrayData: ByteArray = Base64.decode(dtoAsString, Base64.DEFAULT)
        val bitmap: Bitmap? = BitmapFactory.decodeByteArray(byteArrayData, 0, byteArrayData.size)
        return ChatRow(
            chatId = chatRowDto.chatId,
            chatName = chatRowDto.chatName,
            lastMessage = chatRowDto.lastMessageType,
            timestamp = Instant.parse(chatRowDto.timestamp),
            icon = bitmap
        )
    }
}