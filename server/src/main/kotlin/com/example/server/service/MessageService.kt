package com.example.server.service

import com.example.server.dto.MessageDto
import com.example.server.exceptions.ErrorMessageCommons
import com.example.server.exceptions.MessageNotFoundException
import com.example.server.model.Message
import com.example.server.model.MessageContent
import com.example.server.repository.MessageRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.time.Instant
import java.util.*

@Service
class MessageService(
    private val messageRepository: MessageRepository,
) {

    fun findMessageById(messageId: String): Message {
        return messageRepository.findMessageByMessageId(messageId)
            ?: throw MessageNotFoundException("Message not found. Id: $messageId")
    }

    fun findMessageById(messageId: ObjectId): Message {
        return messageRepository.findById(messageId)
            .orElseThrow { (MessageNotFoundException("Message not found. Id: ${messageId.toHexString()}")) }
    }

    fun getAllMessages(): List<Message> {
        return messageRepository.findAll()
    }

    fun getMessageList(messageIds: List<String>): List<Message> {
        return messageIds.mapNotNull { messageRepository.findMessageByMessageId(it) }
    }

    fun save(message: Message): Message {
        return messageRepository.save(message)
    }

    fun saveAll(messages: List<Message>): List<Message> {
        return messageRepository.saveAll(messages)
    }

    fun getMessagesFromChat(chatId: String): List<Message> {
        return messageRepository.findAllByChatId(chatId)
    }

    fun getMessageFromChatInterval(chatId: UUID, from: Instant, to: Instant): List<Message> {
        TODO("MISSING IMPLEMENTATION")
    }

    fun getMessageFromChatBeforeMessage(chatId: UUID, messageId: UUID, messageCount: Int = 30): List<Message> {
        TODO("MISSING IMPLEMENTATION")
    }


    fun deleteMessage(messageId: String, userId: String): Message {
        val message = this.findMessageById(messageId)

        val deletedMessage =
            if (message.senderId.toHexString().equals(userId))
                message.copy(
                    messageContent = MessageContent.Deleted(),
                    isEdited = true,
                    deletedBy = emptyList(),
                    answerTo = null,
                )
            else
                message.copy(deletedBy = message.deletedBy + userId,)

        return messageRepository.save(deletedMessage)
    }

    fun updateTextMessage(messageId: String, newContent: String): Message {
        val message = messageRepository.findMessageByMessageId(messageId)
            ?: throw MessageNotFoundException(ErrorMessageCommons.idNotFound("message", messageId))
        if (message.messageContent !is MessageContent.Text)
            throw IllegalArgumentException("Unexpected content type.")

        val newMessage = message.copy(
            messageContent = MessageContent.Text("newContent"),
            isEdited = true,
        )
        return messageRepository.save(newMessage)
    }

    fun convertMessageToDtoAsSender(message: Message, deletedBy: List<String> = emptyList()): MessageDto {
        return MessageDto(
            messageId = message.messageId.toHexString(),
            senderId = message.senderId.toHexString(),
            chatId = message.chatId.toHexString(),
            timestamp = message.timestamp.toString(),
            messageContent = message.messageContent,
            isEdited = message.isEdited,
            isDeletedForViewer = message.messageContent is MessageContent.Deleted,
            answerTo = message.answerTo
        )
    }

    fun convertMessageToDto(message: Message, viewerId: String): MessageDto {
        return MessageDto(
            messageId = message.messageId.toHexString(),
            senderId = message.senderId.toHexString(),
            chatId = message.chatId.toHexString(),
            timestamp = message.timestamp.toString(),
            messageContent = message.messageContent,
            isEdited = message.isEdited,
            isDeletedForViewer = message.messageContent is MessageContent.Deleted || viewerId in message.deletedBy,
            answerTo = message.answerTo
        )
    }

    fun convertMessageDtoToMessage(messageDto: MessageDto, deletedBy: List<String> = emptyList()): Message {
        return Message(
            messageId = ObjectId(messageDto.messageId),
            senderId = ObjectId(messageDto.senderId),
            chatId = ObjectId(messageDto.chatId),
            timestamp = Instant.parse(messageDto.timestamp),
            messageContent = messageDto.messageContent,
            isEdited =  messageDto.isEdited,
            deletedBy = deletedBy,
            answerTo = messageDto.answerTo
        )
    }




}