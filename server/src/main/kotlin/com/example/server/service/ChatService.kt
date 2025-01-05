package com.example.server.service

import com.example.server.commons.default
import com.example.server.dto.ChatViewDto
import com.example.server.exceptions.*
import com.example.server.model.Chat
import com.example.server.model.ChatType
import com.example.server.model.Message
import com.example.server.repository.ChatRepository
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ChatService(
    private val chatRepository: ChatRepository
) {

    @Autowired @Lazy
    private lateinit var _userService: UserService
    private val userService: UserService by lazy { _userService }

    @Autowired @Lazy
    private lateinit var _messageService: MessageService
    private val messageService: MessageService by lazy { _messageService }

    fun findAllChatsByUserId(userId: String): List<Chat> {
        return chatRepository.findByParticipantsContains(ObjectId(userId))
    }

    fun findChatById(chatId: String): Chat {
        return chatRepository.findByChatId(chatId) ?:
            throw ChatNotFoundException(ErrorMessageCommons.notFound(ClassName.CHAT, Field.ID, chatId))
    }

    fun findChatById(chatId: ObjectId): Chat {
        return chatRepository.findByChatId(chatId) ?:
            throw ChatNotFoundException(ErrorMessageCommons.notFound(ClassName.CHAT, Field.ID, chatId.toHexString()))
    }

    fun save(chat: Chat): Chat {
        return chatRepository.save(chat)
    }

    fun getAllChatMessages(chatId: String): List<Message> {
        return messageService.getAllMessagesFromChat(chatId)
    }

    fun convertUserToUserChatToChatView(chat: Chat, viewerId: String): ChatViewDto {
        if(chat.typeOfChat !is ChatType.UserToUser)
            throw IllegalArgumentException("Unexpected type of chat.")

        val secondParticipantId = chat.participants.first { !it.toHexString().equals(viewerId) }
        val secondParticipant = userService.getUserById(secondParticipantId)

        val lastMessage = try {
                this.getLastChatMessage(chat)
            } catch (e: MessageNotFoundException) {
                // if message does not exist, show init message
                // new message sent on chat should resolve problem
                Message.initMessage(chat)
            }

        val iconAsByteArray = secondParticipant.getIconAsByteArray()

        return ChatViewDto(
            chatId = chat.chatId.toHexString(),
            chatName = secondParticipant.nickname,
            lastMessageType = lastMessage.messageType,
            lastMessageSender = lastMessage.senderId.toHexString(),
            timestamp = lastMessage.creationTime,
            icon = iconAsByteArray
        )

    }

    private fun getLastChatMessage(chat: Chat): Message {
        val lastMessage = if (chat.lastMessageId == ObjectId().default()) {
            Message.initMessage(chat)
        } else {
            messageService.findMessageById(chat.lastMessageId)
        }
        return lastMessage
    }

    fun convertGroupChatToChatView(chat: Chat): ChatViewDto {
        if(chat.typeOfChat !is ChatType.Group)
            throw IllegalArgumentException("Unexpected type of chat.")
        TODO("ChatService.convertGroupChatToChatView()")
    }

    fun getAllChats(): List<Chat> {
        return chatRepository.findAll()
    }

    fun getChatMessagesAfter(chatId: String, parse: Instant): List<Message> {
        TODO("Not yet implemented")
    }

    fun getOrCreateChat(participants: Set<String>): Chat {
        val chat: Chat = try {
            // will throw ChatNotFoundException if such a chat does not exist
            val fetchedChat: Chat = getChatWithParticipants(participants.toList())
            fetchedChat
        } catch (e: ChatNotFoundException) {
            val chatToSave = Chat(
                chatId = ObjectId(),
                lastMessageId = ObjectId().default(),
                typeOfChat = ChatType.UserToUser(),
                participants = participants.map { ObjectId(it) },
                created = Instant.now(),
            )
            val savedChat: Chat = save(chatToSave)
            savedChat
        }

        return chat
    }

    fun getChatWithParticipants(participants: List<String>): Chat {
        return chatRepository.findByParticipants(participants.map { ObjectId(it) })
            ?: throw ChatNotFoundException()
    }

    fun updateLastMessage(chatId: ObjectId, savedMessage: Message) {

        val chat: Chat = chatRepository.findByChatId(chatId)
            ?: throw ChatNotFoundException()

        if(chat.lastMessageId == ObjectId().default()) {
            chatRepository.updateLastMessage(
                chatId = chatId.toString(),
                lastMessageId = savedMessage.messageId
            )
        }

        val lastMessage: Message = messageService.findMessageById(chat.lastMessageId)
        if (lastMessage.creationTime < savedMessage.creationTime) {
            chatRepository.updateLastMessage(
                chatId = chatId.toString(),
                lastMessageId = savedMessage.messageId
            )
        }
    }

}