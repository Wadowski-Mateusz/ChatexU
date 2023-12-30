package com.example.chatexu.data.remote

import com.example.chatexu.common.Constants
import com.example.chatexu.data.remote.dto.ChatRowDto
import com.example.chatexu.data.remote.dto.FriendDto
import com.example.chatexu.data.remote.dto.LoginDto
import com.example.chatexu.data.remote.dto.MessageDto
import com.example.chatexu.data.remote.dto.ParticipantsDto
import com.example.chatexu.data.remote.dto.RegisterDto
import com.example.chatexu.data.remote.dto.SendedMessageDto
import com.example.chatexu.data.remote.dto.UserDto
import com.example.chatexu.domain.model.User
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


    @GET("${Constants.USER_MAPPING}/friends/{userId}")
    suspend fun getUserFriends(
        @Path("userId") userId: String
    ): Response<List<FriendDto>>

//    @GET("${Constants.USER_MAPPING}/friends/{userId}/{partOfNickname}")
//    suspend fun getUserFriendsByNickname(
//        @Path("userId") userId: String,
//        @Path("partOfNickname") partOfNickname: String,
//    ): Response<List<FriendDto>>
//

    @POST("${Constants.MESSAGE_MAPPING}/send")
    suspend fun sendMessage(@Body sendedMessageDto: SendedMessageDto): Response<MessageDto>

    @POST("${Constants.AUTH_MAPPING}/login")
    suspend fun login(@Body loginDto: LoginDto): Response<String>

    @POST("${Constants.AUTH_MAPPING}/register")
    suspend fun register(@Body registerDto: RegisterDto): Response<UserDto>

    @POST("${Constants.CHAT_MAPPING}/create")
    suspend fun getOrElseCreateChat(@Body participantsDto: ParticipantsDto): Response<String>


    @TestOnly
    @GET("${Constants.USER_MAPPING}/get_all")
    suspend fun getAllUsers(): Response<List<UserDto>>

    @TestOnly
    @POST("test/users_chat")
    suspend fun createUsersAndChat(): Response<List<String>>


}