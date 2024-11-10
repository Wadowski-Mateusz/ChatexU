package com.example.server.converters

import com.example.server.dto.MessageDto
import com.example.server.dto.SentMessageDto
import com.example.server.model.Message
import com.example.server.model.MessageType
import org.bson.types.ObjectId
import java.time.Instant

object MessageMapper {
    fun toDto(message: Message, viewerId: String): MessageDto {
        return MessageDto(
            messageId = message.messageId.toHexString(),
            senderId = message.senderId.toHexString(),
            chatId = message.chatId.toHexString(),
            timestamp = message.timestamp.toString(),
            messageType = message.messageType,
            isEdited = message.isEdited,
            isDeletedForViewer = message.messageType is MessageType.Deleted || viewerId in message.deletedBy,
            replyTo = message.replyTo.toHexString()
        )
    }

    fun fromSendedMessage(sentMessageDto: SentMessageDto): Message {
        return Message(
            messageId = ObjectId(),
            senderId = ObjectId(sentMessageDto.senderId),
            chatId = ObjectId(sentMessageDto.chatId),
            timestamp = Instant.now(),
            messageType = sentMessageDto.messageType,
            isEdited = false,
            deletedBy = emptyList(),
            replyTo = ObjectId(sentMessageDto.replyTo)
        )
    }

}