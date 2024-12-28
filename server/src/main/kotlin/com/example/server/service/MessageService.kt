package com.example.server.service

import com.example.server.commons.default
import com.example.server.dto.MessageDto
import com.example.server.exceptions.ClassName
import com.example.server.exceptions.ErrorMessageCommons
import com.example.server.exceptions.Field
import com.example.server.exceptions.MessageNotFoundException
import com.example.server.model.Message
import com.example.server.model.MessageType
import com.example.server.repository.MessageRepository
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.Instant
import java.util.*

@Service
class MessageService(private val messageRepository: MessageRepository) {

    // circular reference hack
    @Lazy @Autowired
    private val _chatService: ChatService? = null
    private val chatService: ChatService by lazy { _chatService!! }


    fun findMessageById(messageId: String): Message {
        return messageRepository.findMessageByMessageId(messageId)
            ?: throw MessageNotFoundException("Message not found. Id: $messageId")
    }

    fun findMessageById(messageId: ObjectId): Message {
        return messageRepository.findById(messageId.toHexString())
            .orElseThrow { (MessageNotFoundException("Message not found. Id: ${messageId.toHexString()}")) }
    }

    fun getAllMessages(): List<Message> {
        return messageRepository.findAll()
    }

    fun getMessageList(messageIds: List<String>): List<Message> {
        return messageIds.mapNotNull { messageRepository.findMessageByMessageId(it) }
    }

    fun save(message: Message): Message {
        // TODO check if user is blocked
        // change last message in the chat document

        val savedMessage = messageRepository.save(message)


        chatService.updateLastMessage(message.chatId, savedMessage)

        return savedMessage
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


//    fun deleteMessage(messageId: String, userId: String): Message {
//        val message = this.findMessageById(messageId)
//
//        val deletedMessage =
//            if (message.senderId.toHexString().equals(userId))
//                message.copy(
//                    messageType = MessageType.Deleted(),
//                    isEdited = true,
//                    deletedBy = emptyList(),
//                    replyTo = ObjectId().default(),
//                )
//            else
//                message.copy(deletedBy = message.deletedBy + userId)
//
//        return messageRepository.save(deletedMessage)
//    }

//    fun updateTextMessage(messageId: String, newContent: String): Message {
//        val message = messageRepository.findMessageByMessageId(messageId)
//            ?: throw MessageNotFoundException(ErrorMessageCommons.notFound(ClassName.MESSAGE, Field.ID, messageId))
//        if (message.messageType !is MessageType.Text)
//            throw IllegalArgumentException("Unexpected content type.")
//
//        val newMessage = message.copy(
//            messageType = MessageType.Text("newContent"),
//            isEdited = true,
//        )
//        return messageRepository.save(newMessage)
//    }

    private fun getMessageTypeForDtoConversion(message: Message): MessageType {
        return if(message.messageType is MessageType.Resource)
            MessageType.Resource(message.getImageAsBase64()) //base64 image
        else
            message.messageType
    }

    fun toDto(message: Message, viewerId: String): MessageDto {
        return MessageDto(
            messageId = message.messageId.toHexString(),
            senderId = message.senderId.toHexString(),
            chatId = message.chatId.toHexString(),
            timestamp = message.timestamp.toString(),
            messageType = getMessageTypeForDtoConversion(message),
//            isEdited = message.isEdited,
//            isDeletedForViewer = message.messageType is MessageType.Deleted || viewerId in message.deletedBy,
//            replyTo = message.replyTo.toHexString()
        )
    }
    fun convertMessageToDtoAsSender(message: Message, deletedBy: List<String> = emptyList()): MessageDto {
        return MessageDto(
            messageId = message.messageId.toHexString(),
            senderId = message.senderId.toHexString(),
            chatId = message.chatId.toHexString(),
            timestamp = message.timestamp.toString(),
            messageType = getMessageTypeForDtoConversion(message),
//            isEdited = message.isEdited,
//            isDeletedForViewer = message.messageType is MessageType.Deleted,
//            replyTo = message.replyTo.toHexString()
        )
    }

    fun convertMessageToDto(message: Message, viewerId: String): MessageDto {
        return MessageDto(
            messageId = message.messageId.toHexString(),
            senderId = message.senderId.toHexString(),
            chatId = message.chatId.toHexString(),
            timestamp = message.timestamp.toString(),
            messageType = getMessageTypeForDtoConversion(message),
//            isEdited = message.isEdited,
//            isDeletedForViewer = message.messageType is MessageType.Deleted || viewerId in message.deletedBy,
//            replyTo = message.replyTo.toHexString()
        )
    }

    fun convertMessageDtoToMessage(messageDto: MessageDto, deletedBy: List<String> = emptyList()): Message {
        return Message(
            messageId = ObjectId(messageDto.messageId),
            senderId = ObjectId(messageDto.senderId),
            chatId = ObjectId(messageDto.chatId),
            timestamp = Instant.parse(messageDto.timestamp),
            messageType = messageDto.messageType,
//            isEdited =  messageDto.isEdited,
//            deletedBy = deletedBy,
//            replyTo = ObjectId(messageDto.replyTo)
        )
    }

    fun getAllMessagesFromChat(chatId: String): List<Message> {
        return messageRepository.findAllMessagesByChatId(ObjectId(chatId))
    }

    fun saveImage(image: MultipartFile, chatId: String): String {

        val resourceFolder: File = File("src/main/resources/chats/$chatId")

        if (!resourceFolder.exists()) {
            val created = resourceFolder.mkdirs()
            if (!created)
                throw IOException("Cannot create directory to store chat images $chatId")
        }

        val iconAsBytes: ByteArray = image.bytes
        val imageUri: String = ObjectId().toHexString() + ".png"
        val outputFile: File = File(resourceFolder, imageUri)

        FileOutputStream(outputFile).use { outputStream -> outputStream.write(iconAsBytes) }

        return imageUri
    }


}