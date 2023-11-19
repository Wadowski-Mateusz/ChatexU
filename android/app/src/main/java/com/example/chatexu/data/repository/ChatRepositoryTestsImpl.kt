package com.example.chatexu.data.repository

import com.example.chatexu.data.remote.ChatApi
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.model.User
import com.example.chatexu.domain.repository.ChatRepository
import javax.inject.Inject


// Connects to "{{url}}/test"
class ChatRepositoryTestImpl @Inject constructor(
    private val api: ChatApi
): ChatRepository {

    override suspend fun getUserChatList(userId: String): List<ChatRow> {
        TODO("Not yet implemented")
    }

    override suspend fun getChatRow(chatId: String, viewerId: String): ChatRow {
        TODO("Not yet implemented")
    }

    override suspend fun getAllChatMessages(chatId: String, userId: String): List<Message> {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(message: Message): Message {
        TODO("Not yet implemented")
    }

    override suspend fun login(email: String, password: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun register(email: String, password: String, nickname: String): User {
        TODO("Not yet implemented")
    }

    override suspend fun getAllUsers(): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun createUsersAndChat(): Boolean {
        TODO("Not yet implemented")
    }


}