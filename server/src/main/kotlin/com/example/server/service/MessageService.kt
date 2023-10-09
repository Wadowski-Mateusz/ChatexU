package com.example.server.service

import com.example.server.dto.MessageDto
import com.example.server.exceptions.MessageNotFoundException
import com.example.server.model.Message
import com.example.server.model.MessageContent
import com.example.server.repository.MessageRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class MessageService(
    private val messageRepository: MessageRepository,
) {

    fun findMessageById(messageId: String): Message {
        return messageRepository.findById(ObjectId(messageId))
            .orElseThrow { (MessageNotFoundException("Message not found. Id: $messageId")) }
    }

    fun findMessageById(messageId: ObjectId): Message {
        return messageRepository.findById(messageId)
            .orElseThrow { (MessageNotFoundException("Message not found. Id: ${messageId.toHexString()}")) }
    }

    fun getAllMessages(): List<Message> {
        return messageRepository.findAll()
    }

    fun save(message: Message): Message {
        return messageRepository.save(message)
    }

    fun saveAll(messages: List<Message>): List<Message> {
        return messageRepository.saveAll(messages)
    }

    fun getMessageFromChatInterval(chatId: UUID, from: Instant, to: Instant): List<Message> {
        TODO("MISSING IMPLEMENTATION")
    }

    fun getMessageFromChatBeforeMessage(chatId: UUID, messageId: UUID, messageCount: Int = 30): List<Message> {
        TODO("MISSING IMPLEMENTATION")
    }

    fun postMessage(message: Message): Message {
        TODO("MISSING IMPLEMENTATION")
    }

    fun deleteMessage(messageId: String, userId: String): Message {
        val message = this.findMessageById(messageId)

        val deletedMessage =
            if (message.senderId.toHexString().equals(userId))
                Message(
                    messageId = message.messageId,
                    senderId = message.senderId,
                    chatId = message.chatId,
                    timestamp = message.timestamp,
                    messageContent = MessageContent.Deleted(),
                    isEdited = true,
                    deletedBy = emptyList(),
                    answerTo = message.answerTo,
                )
            else
                Message(
                    messageId = message.messageId,
                    senderId = message.senderId,
                    chatId = message.chatId,
                    timestamp = message.timestamp,
                    messageContent = message.messageContent,
                    isEdited = message.isEdited,
                    deletedBy = message.deletedBy + userId,
                    answerTo = message.answerTo,
                )

        return messageRepository.save(deletedMessage)
    }

    fun updateTextMessage(messageId: String, newContent: String): Message {
        TODO("MISSING IMPLEMENTATION")
    }

    fun convertMessageToDtoAsSender(message: Message): MessageDto {
        TODO("Not yet implemented")
    }

    fun convertMessageToDto(message: Message, viewerId: String): MessageDto {
        TODO("Not yet implemented")
    }

    fun convertMessageDtoToMessageAsSender(messageDto: MessageDto): Message {
        TODO("Not yet implemented")
    }

    fun convertMessageDtoToMessage(messageDto: MessageDto, viewerId: String): Message {
        TODO("Not yet implemented")
    }



}