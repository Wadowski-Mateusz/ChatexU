package com.example.chatexu.data.repository

import android.util.Log
import com.example.chatexu.common.Constants
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.converters.ChatMapper
import com.example.chatexu.converters.FriendMapper
import com.example.chatexu.converters.FriendRequestMapper
import com.example.chatexu.converters.MessageMapper
import com.example.chatexu.converters.UserMapper
import com.example.chatexu.data.remote.ChatApi
import com.example.chatexu.data.remote.dto.AuthenticationDto
import com.example.chatexu.data.remote.dto.ChatRowDto
import com.example.chatexu.data.remote.dto.LoginDto
import com.example.chatexu.data.remote.dto.MessageDto
import com.example.chatexu.data.remote.dto.ParticipantsDto
import com.example.chatexu.data.remote.dto.RegisterDto
import com.example.chatexu.data.remote.dto.UserDto
import com.example.chatexu.domain.model.Authentication
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.model.Friend
import com.example.chatexu.domain.model.FriendRequest
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.model.User
import com.example.chatexu.domain.repository.ChatRepository
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.http.HTTP_CONFLICT
import okhttp3.internal.http.HTTP_OK
import okhttp3.internal.http.HTTP_UNAUTHORIZED
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject


class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApi
): ChatRepository {

    private val BEARER: String = "Bearer "

    override suspend fun getUserChatList(userId: String, jwt: String): List<ChatRow> {
        val response = api.getUserChatList(userId = userId, jwt = BEARER + jwt)
        if(response.code() != HTTP_OK) {
            val errorResponse = Response.error<String>(
                response.code(),
                "Unexpected error while fetching from server.".toResponseBody()
            )
            throw HttpException(errorResponse)
        }
        val chats: List<ChatRowDto> = response.body() ?: emptyList()
        return chats.map{ ChatMapper.toChatRow(it) }
    }

    override suspend fun getChatRow(chatId: String, viewerId: String, jwt: String): ChatRow {
        // TODO null
        return ChatMapper.toChatRow(api.getChatRow(chatId = chatId, viewerId = viewerId, jwt = BEARER + jwt).body()!!)
    }

    override suspend fun getAllChatMessages(chatId: String, userId: String, jwt: String): List<Message> {
        val messagesDtos: List<MessageDto> = api.getAllChatMessages(chatId = chatId, viewerId = userId, jwt = BEARER + jwt).body()
            ?: let{
//                Log.d("peek", "empty message list")
                emptyList<MessageDto>()
            }
        return messagesDtos.map { MessageMapper.toMessage(it) }
    }

    override suspend fun sendMessage(message: Message, jwt: String): Message {
        val messageDto = MessageMapper.toSend(message)
        val response = api.sendMessage(sentMessageDto = messageDto, jwt = BEARER + jwt)
        Log.d(DebugConstants.TODO, "ChatRepositoryImpl.sendMessage() - handle response.")
        val body = response.body()

        return if(response.body() != null)
            MessageMapper.toMessage(body!!)
        else Message.getEmpty()

    }

    override suspend fun login(email: String, password: String): Authentication {
        val loginDto = LoginDto(email, password)
        val response = api.login(loginDto)
        if (response.code() == HTTP_UNAUTHORIZED) {
            return Authentication("")
        } else if (response.code() != HTTP_OK) {
            val errorResponse = Response.error<String>(
                response.code(),
                "Unexpected error in login()".toResponseBody()
            )
            throw HttpException(errorResponse)
        }

        val body = response.body() ?: return Authentication("")
        return Authentication(token = body.token)
    }

    override suspend fun register(email: String, nickname: String, password: String): Authentication {
        val registerDto: RegisterDto = RegisterDto(email = email, nickname = nickname,  password = password)
        val response = api.register(registerDto = registerDto)

        if (response.code() == HTTP_CONFLICT) {

            // reuse authenticationdto and authentication to report data in the database
            val errorBody = response.errorBody()
                ?: return Authentication("")
            val authJson = errorBody.string()
            val auth = Gson().fromJson(authJson, Authentication::class.java)
            return auth
        } else if (response.code() != HTTP_OK) {
            val errorResponse = Response.error<String>(
                response.code(),
                "Unexpected error in register()".toResponseBody()
            )
            throw HttpException(errorResponse)
        }

        val body = response.body() ?: return Authentication("")
        return Authentication(token = body.token)
    }

    override suspend fun getAllUsers(jwt: String): List<User> {
        val userDtos: List<UserDto> = api.getAllUsers(BEARER + jwt).body()
            ?: let {
//                Log.d("ChatRepositoryImpl.getAllUsers()", "getAllUsers - empty user list")
                emptyList()
            }
//        if(userDtos.isNotEmpty())
//            Log.d("ChatRepositoryImpl.getAllUsers()", "getAllUsers size: ${userDtos.size}")
        return userDtos.map { UserMapper.toUser(it) }
    }

    override suspend fun createUsersAndChat(jwt: String): Boolean {
//        Log.d(DebugConstants.PEEK, "Creating two users and chat.")
        val result = api.createUsersAndChat(jwt = BEARER + jwt)
        return result.isSuccessful
    }

    override suspend fun getUserById(userId: String, jwt: String): User {
        val response = api.getUserById(userId = userId, jwt = BEARER + jwt)
        if(response.code() == HTTP_OK) {
            val userDto: UserDto = response.body()
                ?: throw Exception("Unexpected error in ChatRepositoryImpl.getUserById() - returned user is null")
            val user = UserMapper.toUser(userDto)
            return user
        } else {
            val errorResponse = Response.error<String>(response.code(), "Unexpected error in ChatRepositoryImpl.getUserById()".toResponseBody())
            throw HttpException(errorResponse)
        }
    }

    override suspend fun getUserFriends(userId: String, jwt: String): List<Friend> {
//        Log.d(DebugConstants.PEEK, "---ChatRepositoryImpl - Fetching friends of $userId.")
        val result = api.getUserFriends(userId = userId, jwt = BEARER + jwt)
//        Log.d(DebugConstants.PEEK, "ChatRepositoryImpl - Fetched friends of $userId.")
        val friends = result.body()
            ?: let {
//                Log.w(DebugConstants.PEEK, "repositoryImpl - getUserFriends - null")
                emptyList()
            }
        return friends.map { FriendMapper.toFriend(it) }
    }

    override suspend fun getOrElseCreateChat(participants: List<String>, jwt: String): String {
//        Log.d(DebugConstants.PEEK, "get or create chat - repo impl")
        val participantsDto = ParticipantsDto(participants)
        val result = api.getOrElseCreateChat(participantsDto = participantsDto, jwt = BEARER + jwt)
        return result.body() ?: "" // TODO id cannot be "", make sure of it
    }

    override suspend fun sendFriendRequest(senderId: String, recipientId: String, jwt: String): FriendRequest {
//        Log.d(DebugConstants.PEEK, "repoImpl::sendFriendRequest() - start")
        val result = api.sendFriendRequest(senderId = senderId, recipientId = recipientId, jwt = BEARER + jwt)
        // todo errors (http codes)
//        Log.d(DebugConstants.PEEK, "repoImpl::sendFriendRequest() - ${result.body().toString()}")

        return FriendRequestMapper.fromDto(result.body()!!)
    }

    override suspend fun deleteFriendRequest(requestId: String, jwt: String): Boolean {
//          this throws an error on success and wont reload list of user requests
//        val result = api.deleteFriendRequest(requestId)
//        return result.code() == HTTP_OK
//
        // this works, but may stop on another error (such as http 500)
        return try {
            val result = api.deleteFriendRequest(requestId = requestId, jwt = BEARER + jwt)
            result.code() == HTTP_OK
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun postAcceptFriendRequest(requestId: String, jwt: String): Boolean {
        return try {
            val result = api.acceptFriendRequest(requestId = requestId, jwt = BEARER + jwt)
            result.code() == HTTP_OK
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getAllFriendRequestsForUser(userId: String, jwt: String): List<FriendRequest> {
//        Log.d(DebugConstants.PEEK, "ChatRepositoryImpl.getAllFriendRequestsForUser() - start")
        val result = api.getAllFriendRequestsForUser(userId = userId, jwt = BEARER + jwt)
//        Log.d(DebugConstants.PEEK, "ChatRepositoryImpl.getAllFriendRequestsForUser() - fetched")
        return result.body()!!.map { FriendRequestMapper.fromDto(it) }
    }

    override suspend fun getUsersByPartOfNickname(
        userId: String,
        partOfNickname: String,
        jwt: String
    ): List<User> {
        // todo exception handling
        val result = api.getUsersByPartOfNickname(searcherId = userId, partOfNickname = partOfNickname, jwt = BEARER + jwt)
        return result.body()!!.map { UserMapper.toUser(it) }
    }

    override suspend fun getChatParticipants(chatId: String, jwt: String): List<User> {
        val response = api.getChatParticipants(chatId = chatId, jwt = BEARER + jwt)
        if(response.code() == HTTP_OK) {
            val userDtos: List<UserDto> = response.body()
                ?: throw Exception("Unexpected error in ChatRepositoryImpl.getChatParticipants() - returned list is null")
            val users: List<User> = userDtos.map { UserMapper.toUser(it) }
            return users
        } else {
            throw HttpException( Response.error<String>(
                response.code(),
                "Unexpected error in ChatRepositoryImpl.getChatParticipants()".toResponseBody()
                )
            )
        }
    }

    override suspend fun rejectFriendRequest(requestId: String, jwt: String): Boolean {
        return try {
            val result = api.rejectFriendRequest(friendRequestId = requestId, jwt = BEARER + jwt)
            result.code() == HTTP_OK
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun putUpdateIcon(userId: String, icon: MultipartBody.Part, jwt: String): User {
        val response = api.putUpdateIcon(userId = userId, icon = icon, jwt = BEARER + jwt)
        if(response.code() == HTTP_OK) {
            val userDto: UserDto = response.body()
                ?: throw Exception("Unexpected error in ChatRepositoryImpl.putUpdateIcon() - returned user is null")
            val user = UserMapper.toUser(userDto)
            return user
        } else {
            val errorResponse = Response.error<String>(response.code(), "Unexpected error in ChatRepositoryImpl.putUpdateIcon()".toResponseBody())
            throw HttpException(errorResponse)
        }

    }

    override suspend fun putUpdateNickname(userId: String, nickname: String, jwt: String): User {
        val response = api.putUpdateNickname(userId = userId, nickname = nickname, jwt = BEARER + jwt)

        if(response.code() == HTTP_OK) {
            val userDto: UserDto = response.body()
                ?: throw Exception("Unexpected error in ChatRepositoryImpl.putUpdateNickname() - returned user is null")
            val user = UserMapper.toUser(userDto)
            return user
        } else {
            val errorResponse = Response.error<String>(response.code(), "Unexpected error in ChatRepositoryImpl.putUpdateNickname()".toResponseBody())
            throw HttpException(errorResponse)
        }

    }

    override suspend fun putSendImage(message: Message, image: MultipartBody.Part, jwt: String): String {

        val messageDto = MessageMapper.toSend(message)

        val response = api.postSendImage(message = messageDto, image = image, jwt = BEARER + jwt)
        if(response.code() == HTTP_OK) {
            val messageId: String = response.body()
                ?: throw Exception("Unexpected error in ChatRepositoryImpl.postSendImage() - returned user is null")
            return messageId
        } else {
            val errorResponse = Response.error<String>(response.code(), "Unexpected error in ChatRepositoryImpl.postSendImage()".toResponseBody())
            throw HttpException(errorResponse)
        }

    }

}