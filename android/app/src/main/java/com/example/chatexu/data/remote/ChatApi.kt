package com.example.chatexu.data.remote

import com.example.chatexu.common.Constants
import com.example.chatexu.domain.model.ChatRow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.UUID

interface ChatApi {

    @GET("${Constants.CHAT_MAPPING}/chat_view/{id}/{id2}")
    suspend fun getChatRow(@Path("id") id: UUID, @Path("id2") id2: UUID): Response<ChatRow>

    @GET("${Constants.CHAT_MAPPING}/chat_views_test")
    suspend fun getChatRowFast(): Response<List<ChatRow>>

}