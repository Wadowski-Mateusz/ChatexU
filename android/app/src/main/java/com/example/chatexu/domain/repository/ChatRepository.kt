package com.example.chatexu.domain.repository

import com.example.chatexu.data.remote.dto.ChatRowDto
import com.example.chatexu.domain.model.ChatRow
import retrofit2.Response
import java.util.UUID

interface ChatRepository {

    suspend fun getChatRow(chatId: UUID): ChatRow
    suspend fun getUserChatRows(): List<ChatRow>

    suspend fun getChatRowById(chatId: String): ChatRow

}