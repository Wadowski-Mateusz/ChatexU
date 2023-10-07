package com.example.chatexu.data.repository

import com.example.chatexu.data.remote.ChatApi
import com.example.chatexu.data.remote.dto.ChatRowDto
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.repository.ChatRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApi
): ChatRepository {

    override suspend fun getChatRow(chatId: UUID): ChatRow {
        val chatRowResponse = api.getChatRow(chatId, chatId)
        // TODO throw exception or something
        return chatRowResponse.body() ?: ChatRow.Builder().fastBuild()
    }

    override suspend fun getUserChatRows(): List<ChatRow> {
        val chatRowResponse = api.getChatRowFast()
        return chatRowResponse.body() ?: emptyList()
    }

    override suspend fun getChatRowById(chatId: String): ChatRow {
        // TODO uncomment
//        val chatRowResponse = api.getChatRowById(chatId)
        val chatRowResponse = api.getChatRowById("123")
        val chatRow = chatRowResponse.body()?.toChatRow()
        return chatRow ?: ChatRow.Builder().fastBuild()
    }



}