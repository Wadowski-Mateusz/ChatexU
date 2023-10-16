package com.example.chatexu.converters

import com.example.chatexu.data.remote.dto.MessageDto
import com.example.chatexu.domain.model.Message
import java.time.Instant

object MessageMapper {
    fun toMessage(msg: MessageDto): Message {
        return Message(
            messageId = msg.messageId,
            senderId = msg.senderId,
            chatId = msg.chatId,
            timestamp = Instant.parse(msg.timestamp),
            messageType = msg.messageType,
            isEdited = msg.isEdited,
            answerTo = msg.answerTo,
        )
    }
}