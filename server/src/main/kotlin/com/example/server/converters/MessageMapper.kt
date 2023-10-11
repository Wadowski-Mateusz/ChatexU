package com.example.server.converters

import com.example.server.dto.MessageDto
import com.example.server.model.Message
import com.example.server.model.MessageType

object MessageMapper {
    fun messageToDto(message: Message, viewerId: String): MessageDto {
        return MessageDto(
            messageId = message.messageId.toHexString(),
            senderId = message.senderId.toHexString(),
            chatId = message.chatId.toHexString(),
            timestamp = message.timestamp.toString(),
            messageType = message.messageType,
            isEdited = message.isEdited,
            isDeletedForViewer = message.messageType is MessageType.Deleted || viewerId in message.deletedBy,
            answerTo = message.answerTo.toHexString()
        )
    }
}