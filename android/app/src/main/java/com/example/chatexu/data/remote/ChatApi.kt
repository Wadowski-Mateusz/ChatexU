package com.example.chatexu.data.remote

import com.example.chatexu.common.Constants
import com.example.chatexu.data.remote.dto.ChatRowDto
import com.example.chatexu.data.remote.dto.FriendDto
import com.example.chatexu.data.remote.dto.FriendRequestDto
import com.example.chatexu.data.remote.dto.LoginDto
import com.example.chatexu.data.remote.dto.MessageDto
import com.example.chatexu.data.remote.dto.ParticipantsDto
import com.example.chatexu.data.remote.dto.RegisterDto
import com.example.chatexu.data.remote.dto.SendedMessageDto
import com.example.chatexu.data.remote.dto.UserDto
import com.example.chatexu.data.remote.dto.UserViewDto
import com.example.chatexu.domain.model.Message
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.annotations.TestOnly
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path


interface ChatApi {


    @GET("${Constants.CHAT_MAPPING}/chat_view/chat_list/{userId}")
    suspend fun getUserChatList(
        @Path("userId") userId: String
    ): Response<List<ChatRowDto>>

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

    @GET("${Constants.USER_MAPPING}/get/{userId}")
    suspend fun getUserById(
        @Path("userId") userId: String
    ): Response<UserDto>

    @GET("${Constants.USER_MAPPING}/friends/{userId}")
    suspend fun getUserFriends(
        @Path("userId") userId: String
    ): Response<List<FriendDto>>



    @GET("${Constants.USER_MAPPING}/friendRequest/{userId}")
    suspend fun getAllFriendRequestsForUser(
        @Path("userId") userId: String
    ): Response<List<FriendRequestDto>>

//    @GET("${Constants.USER_MAPPING}/friends/{userId}/{partOfNickname}")
//    suspend fun getUserFriendsByNickname(
//        @Path("userId") userId: String,
//        @Path("partOfNickname") partOfNickname: String,
//    ): Response<List<FriendDto>>

    @GET("${Constants.USER_MAPPING}/nickname_part/{searcherId}/{partOfNickname}")
    suspend fun getUsersByPartOfNickname(
        @Path("searcherId") searcherId: String,
        @Path("partOfNickname") partOfNickname: String,
    ): Response<List<UserViewDto>>




    @POST("${Constants.MESSAGE_MAPPING}/send")
    suspend fun sendMessage(@Body sendedMessageDto: SendedMessageDto): Response<MessageDto>

    @POST("${Constants.AUTH_MAPPING}/login")
    suspend fun login(@Body loginDto: LoginDto): Response<String>

    @POST("${Constants.AUTH_MAPPING}/register")
    suspend fun register(@Body registerDto: RegisterDto): Response<UserDto>

    @POST("${Constants.CHAT_MAPPING}/create")
    suspend fun getOrElseCreateChat(@Body participantsDto: ParticipantsDto): Response<String>

    @POST("${Constants.USER_MAPPING}/friend/send_request/{senderId}/{recipientId}")
    suspend fun sendFriendRequest(
        @Path("senderId") senderId: String,
        @Path("recipientId") recipientId: String
    ): Response<FriendRequestDto>

    @POST("${Constants.USER_MAPPING}/friendRequest/accept/{requestId}")
    suspend fun acceptFriendRequest(
        @Path("requestId") requestId: String
    ): Response<Any> // no body

    @DELETE("${Constants.USER_MAPPING}/friendRequest/delete/{requestId}")
    suspend fun deleteFriendRequest(
        @Path("requestId") requestId: String
    ): Response<Any> // no body

    @DELETE("${Constants.USER_MAPPING}/friendRequest/reject/{friendRequestId}")
    suspend fun rejectFriendRequest(
        @Path("friendRequestId") friendRequestId: String
    ): Response<Any> // no body


    @TestOnly
    @GET("${Constants.USER_MAPPING}/get_all")
    suspend fun getAllUsers(): Response<List<UserDto>>

    @TestOnly
    @POST("test/users_chat")
    suspend fun createUsersAndChat(): Response<List<String>>

    @PUT("${Constants.USER_MAPPING}/update_nickname/{userId}/{nickname}")
    suspend fun putUpdateNickname(
        @Path("userId") userId: String,
        @Path("nickname") nickname: String
    ): Response<UserDto>


    @Multipart
    @PUT("${Constants.USER_MAPPING}/update_icon/{userId}")
    suspend fun putUpdateIcon(
        @Path("userId") userId: String,
        @Part icon: MultipartBody.Part
    ): Response<UserDto>

    @GET("${Constants.CHAT_MAPPING}/get/chat_participants/{chatId}")
    suspend fun getChatParticipants(
        @Path("chatId") chatId: String
    ): Response<List<UserDto>>

    @Multipart
    @PUT("${Constants.MESSAGE_MAPPING}/sendImage")
    suspend fun postSendImage(
        @Part("message") message: SendedMessageDto,
        @Part image: MultipartBody.Part
    ): Response<String>


}
