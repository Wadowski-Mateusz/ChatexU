package com.example.chatexu.data.remote

import com.example.chatexu.domain.model.ChatRow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.UUID

interface ChatRowApi {


    private fun url(): String = "${Urls.URL}/${Urls.CHAT_MAPPING}"

    @GET("${ServerUrlConsts.CHAT_MAPPING}/chat_view/{id}/{id2}")
    suspend fun getChatRow(@Path("id") id: UUID, @Path("id2") id2: UUID): Response<ChatRow>

    @GET("${ServerUrlConsts.CHAT_MAPPING}/chat_view")
    suspend fun getChatRowFast(): Response<ChatRow>


}