package com.example.chatexu.data.remote

import com.example.chatexu.common.Constants
import com.example.chatexu.data.remote.dto.ChatRowDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatApi {

    @GET("${Constants.CHAT_MAPPING}/chat_list/{userId}")
    suspend fun getUserChatList(@Path("userId") userId: String): Response<List<ChatRowDto>>

    @GET("${Constants.CHAT_MAPPING}/chat_view/{chatId}/{viewerId}")
    suspend fun getChatRow(@Path("chatId") chatId: String, @Path("viewerId") viewerId: String): Response<ChatRowDto>

}