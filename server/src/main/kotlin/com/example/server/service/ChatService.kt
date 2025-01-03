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
class ChatService(private val chatRepository: ChatRepository) {

    // circular reference hack
    @Lazy @Autowired
    private val _userService: UserService? = null
    private val userService: UserService by lazy { _userService!! }

    // circular reference hack
    @Lazy @Autowired
    private val _messageService: MessageService? = null
    private val messageService: MessageService by lazy { _messageService!! }


    fun findAllByUserId(userId: String): List<Chat> {
        return chatRepository.findByParticipantsContains(ObjectId(userId))
//        return chatRepository.findByParticipantsContains(userId)
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


        // TODO what if second participant delete his account?
        val secondParticipantId = chat.participants.first { !it.toHexString().equals(viewerId) }
        val secondParticipant = userService.getUserById(secondParticipantId)

        val lastMessage = try {
            messageService.findMessageById(chat.lastMessage)
        } catch (err: MessageNotFoundException) {
            if (chat.lastMessage == ObjectId().default()) {
                Message.initMessage(chat)
            } else
                throw err
        }

        val iconAsByteArray = secondParticipant.getIconAsByteArray()


        return ChatViewDto(
            chatId = chat.chatId.toHexString(),
            chatName = secondParticipant.nickname,
            lastMessageType = lastMessage.messageType,
            lastMessageSender = lastMessage.senderId.toHexString(),
            timestamp = lastMessage.timestamp,
            icon = iconAsByteArray
        )

    }

    fun convertGroupChatToChatView(chat: Chat): ChatViewDto {
        if(chat.typeOfChat !is ChatType.Group)
            throw IllegalArgumentException("Unexpected type of chat.")
        TODO()
    }

    fun getAll(): List<Chat> {
        return chatRepository.findAll()
    }


    fun getChatMessagesAfter(chatId: String, parse: Instant): List<Message> {
        TODO("Not yet implemented")
    }

    fun createChat(participants: Set<String>): Chat {
        // TODO blocked users
        // duplicates of chats between users
        // ...

        if(participants.size == 2)
            try {
                val chat = getChatWithParticipants(participants.toList())
                return chat
            } catch (e: ChatNotFoundException) {
                println("No such a chat. creating new one")
                // Chat doesn't exit
                // catch exception and create new one
            }


        val chat = Chat(
            chatId = ObjectId(),
            lastMessage = ObjectId().default(),
            typeOfChat = ChatType.UserToUser(),
            participants = participants.map { ObjectId(it) },
            created = Instant.now(),
//            lastViewedBy = mapOf(),
//            mutedBy = mapOf()
        )
        return save(chat)
    }

    fun getChatWithParticipants(participants: List<String>): Chat {
        return chatRepository.findByParticipants(participants.map { ObjectId(it) })
            ?: throw ChatNotFoundException()
    }

    fun updateLastMessage(chatId: ObjectId, savedMessage: Message) {
        // TODO no validation or anything
        chatRepository.updateLastMessage(chatId.toString(), savedMessage.messageId.toString())

    }

}