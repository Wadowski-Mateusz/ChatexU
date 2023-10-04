package com.example.chatexu.data.repository

import com.example.chatexu.data.remote.ChatRowApi
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.repository.ChatRepository
import java.util.UUID
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatRowApi
): ChatRepository {

    override suspend fun getChatRow(chatId: UUID): ChatRow {
        val chatRowResponse = api.getChatRow(chatId, chatId)
        // TODO throw exception or something
        return chatRowResponse.body() ?: ChatRow.Builder().fastBuild()
    }

    override suspend fun getUserChatRows(userId: UUID): List<ChatRow> {
        TODO("Not yet implemented")
    }

}