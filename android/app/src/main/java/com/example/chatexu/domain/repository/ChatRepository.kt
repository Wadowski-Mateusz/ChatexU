package com.example.chatexu.domain.repository

import com.example.chatexu.domain.model.ChatRow
import java.util.UUID

interface ChatRepository {

    suspend fun getChatRow(chatId: UUID): ChatRow
    suspend fun getUserChatRows(userId: UUID): List<ChatRow>


}