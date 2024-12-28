package com.example.server.converters

import com.example.server.dto.SentMessageDto
import com.example.server.model.Message
import org.bson.types.ObjectId
import java.time.Instant

object MessageMapper {

    fun fromSentMessage(sentMessageDto: SentMessageDto): Message {
        return Message(
            messageId = ObjectId(),
            senderId = ObjectId(sentMessageDto.senderId),
            chatId = ObjectId(sentMessageDto.chatId),
            timestamp = Instant.now(),
            messageType = sentMessageDto.messageType,
//            isEdited = false,
//            deletedBy = emptyList(),
//            replyTo = ObjectId(sentMessageDto.replyTo)
        )
    }
    
}