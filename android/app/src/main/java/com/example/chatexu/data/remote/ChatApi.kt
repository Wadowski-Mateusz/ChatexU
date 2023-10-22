package com.example.chatexu.data.remote

import com.example.chatexu.common.Constants
import com.example.chatexu.data.remote.dto.ChatRowDto
import com.example.chatexu.data.remote.dto.MessageDto
import com.example.chatexu.data.remote.dto.SendedMessageDto
import com.example.chatexu.data.remote.dto.UserDto
import org.jetbrains.annotations.TestOnly
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApi {


    @GET("${Constants.CHAT_MAPPING}/chat_view/chat_list/{userId}")
    suspend fun getUserChatList(@Path("userId") userId: String): Response<List<ChatRowDto>>

    @GET("${Constants.CHAT_MAPPING}/chat_view/{chatId}/{viewerId}")
    suspend fun getChatRow(
        @Path("chatId") chatId: String,
        @Path("viewerId") viewerId: String
    ): Response<ChatRowDto>

    @GET("${Constants.CHAT_MAPPING}/messages/all/{chatId}/{viewerId}")
    suspend fun getAllChatMessages(
        @Path("chatId") chatId: String,
        @Path("viewerId") viewerId: String
    ): Response<List<MessageDto>>


    @POST("${Constants.MESSAGE_MAPPING}/send")
    suspend fun sendMessage(@Body sendedMessageDto: SendedMessageDto): Response<MessageDto>


    @TestOnly
    @GET("${Constants.USER_MAPPING}/get_all")
    suspend fun getAllUsers(): Response<List<UserDto>>

    @TestOnly
    @POST("test/users_chat")
    suspend fun createUsersAndChat(): Response<List<String>>


}