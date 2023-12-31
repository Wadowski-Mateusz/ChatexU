package com.example.chatexu.data.repository

import android.util.Log
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.converters.ChatMapper
import com.example.chatexu.converters.MessageMapper
import com.example.chatexu.converters.UserMapper
import com.example.chatexu.data.remote.ChatApi
import com.example.chatexu.data.remote.dto.MessageDto
import com.example.chatexu.data.remote.dto.UserDto
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.model.User
import com.example.chatexu.domain.repository.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApi
): ChatRepository {


//        val chatRowResponse = api.getChatRow(chatId, userId)
//        return chatRowResponse.body() ?: emptyList()

    override suspend fun getUserChatList(userId: String): List<ChatRow> {
        val chats = api.getUserChatList(userId).body() ?: emptyList()
        Log.d("peek", "repo imp: size = ${chats.size}")
        return chats.map{ ChatMapper.toChatRow(it) }
    }

    override suspend fun getChatRow(chatId: String, viewerId: String): ChatRow {
        // TODO null
        return ChatMapper.toChatRow(api.getChatRow(chatId, viewerId).body()!!)
    }

    override suspend fun getAllChatMessages(chatId: String, userId: String): List<Message> {
        val messagesDtos: List<MessageDto> = api.getAllChatMessages(chatId, userId).body()
            ?: let{
                Log.d("peek", "empty message list")
                emptyList<MessageDto>()
            }
        return messagesDtos.map { MessageMapper.toMessage(it) }
    }

    override suspend fun sendMessage(message: Message): Message {
        val messageDto = MessageMapper.toSend(message)
        val response = api.sendMessage(messageDto)
        Log.d(DebugConstants.TODO, "ChatRepositoryImpl.sendMessage() - handle response.")
        val body = response.body()

        return if(response.body() != null)
            MessageMapper.toMessage(body!!)
        else Message.getEmpty()

    }

    override suspend fun getAllUsers(): List<User> {
        val userDtos: List<UserDto> = api.getAllUsers().body()
            ?: let {
                Log.d("peek", "getAllUsers - empty user list")
                emptyList()
            }
        return userDtos.map { UserMapper.toUser(it) }
    }

    override suspend fun createUsersAndChat(): Boolean {
        Log.d(DebugConstants.PEEK, "Creating two users and chat.")
        val result = api.createUsersAndChat()
        return result.isSuccessful
    }

}