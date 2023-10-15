package com.example.chatexu.data.repository

import android.util.Log
import com.example.chatexu.converters.ChatMapper
import com.example.chatexu.data.remote.ChatApi
import com.example.chatexu.data.remote.dto.ChatRowDto
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.repository.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApi
): ChatRepository {


//        val chatRowResponse = api.getChatRow(chatId, userId)
//        return chatRowResponse.body() ?: emptyList()

    override suspend fun getUserChatList(userId: String): List<ChatRow> {
        val chats = api.getUserChatList(userId).body() ?: emptyList()
        Log.d("peek", "repo imp: size = ${chats.size}")
        return chats.map{ ChatMapper.toChatRow(it) }
    }

    override suspend fun getChatRow(chatId: String, viewerId: String): ChatRow {
        // TODO null
        return ChatMapper.toChatRow(api.getChatRow(chatId, viewerId).body()!!)
    }

}