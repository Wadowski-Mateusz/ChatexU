package com.example.chatexu.domain.repository

import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.model.Message

interface ChatRepository {

    suspend fun getUserChatList(userId: String): List<ChatRow>

    suspend fun getChatRow(chatId: String, viewerId: String): ChatRow

    suspend fun getAllChatMessages(chatId: String, userId: String): List<Message>

}