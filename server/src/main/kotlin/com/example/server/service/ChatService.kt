package com.example.server.service

import com.example.server.dto.ChatViewDto
import com.example.server.model.Chat
import com.example.server.model.ChatType
import com.example.server.repository.ChatRepository
import lombok.AllArgsConstructor
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
@AllArgsConstructor
class ChatService(
    private val userService: UserService,
    private val messageService: MessageService,
) {
    
    fun findAllByUserId(userId: String): List<Chat> {
        TODO("Not yet implemented")
    }

    fun findChatById(chatId: String): Chat {
        TODO("Not yet implemented")
    }


    fun convertUserToUserChatToChatView(chat: Chat, viewerId: String): ChatViewDto {
        if(chat.typeOfChat !is ChatType.UserToUser)
            throw IllegalArgumentException("Unexpected type of chat.")


        // TODO what if second participant delete his account?
        val secondParticipantId = chat.participants.first { !it.toHexString().equals(viewerId) }
        val secondParticipant = userService.findUserById(secondParticipantId)

        val lastMessage = messageService.findMessageById(chat.lastMessage)

        val iconUri = userService.getUserIconURI(viewerId)
        val iconResource: Resource = ClassPathResource("icons/$iconUri")
        val iconAsByteArray = iconResource.inputStream.readAllBytes()


        return ChatViewDto(
            chatId = chat.chatId.toHexString(),
            chatName = secondParticipant.nickname,
            lastMessageContent = lastMessage.messageContent,
            lastMessageSender = lastMessage.senderId.toHexString(),
            timestamp =lastMessage.timestamp,
            icon = iconAsByteArray
        )

    }

    fun convertGroupChatToChatView(chat: Chat): ChatViewDto {
        if(chat.typeOfChat !is ChatType.Group)
            throw IllegalArgumentException("Unexpected type of chat.")
        TODO()
    }

}