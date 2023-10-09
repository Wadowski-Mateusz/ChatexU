package com.example.chatexu.data.repository

import com.example.chatexu.data.remote.ChatApi
import com.example.chatexu.domain.model.ChatRow
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


}