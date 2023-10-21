package com.example.chatexu.domain.repository

import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.model.User
import org.jetbrains.annotations.TestOnly

interface ChatRepository {

    suspend fun getUserChatList(userId: String): List<ChatRow>

    suspend fun getChatRow(chatId: String, viewerId: String): ChatRow

    suspend fun getAllChatMessages(chatId: String, userId: String): List<Message>

    suspend fun sendMessage(message: Message): Message

    @TestOnly
    suspend fun getAllUsers(): List<User>

}