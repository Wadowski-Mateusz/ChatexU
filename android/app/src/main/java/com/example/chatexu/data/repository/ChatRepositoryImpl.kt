package com.example.chatexu.data.repository

import com.example.chatexu.data.remote.ChatApi
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.repository.ChatRepository
import java.util.UUID
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApi
): ChatRepository {


//        val chatRowResponse = api.getChatRow(chatId, userId)
//        return chatRowResponse.body() ?: emptyList()

    override suspend fun getUserChatList(userId: String): List<ChatRow> {
        TODO("Not yet implemented")
    }

    override suspend fun getChatRow(chatId: String, viewerId: String): ChatRow {
        TODO("Not yet implemented")
    }

}