package com.example.chatexu.domain.repository


import com.example.chatexu.domain.model.ChatRow

interface ChatRepository {

    suspend fun getUserChatList(userId: String): List<ChatRow>

    suspend fun getChatRow(chatId: String, viewerId: String): ChatRow

}