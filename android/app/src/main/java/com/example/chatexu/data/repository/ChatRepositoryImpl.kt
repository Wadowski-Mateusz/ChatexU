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
import com.example.chatexu.data.remote.dto.LoginDto
import com.example.chatexu.data.remote.dto.MessageDto
import com.example.chatexu.data.remote.dto.ParticipantsDto
import com.example.chatexu.data.remote.dto.RegisterDto
import com.example.chatexu.data.remote.dto.UserDto
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.model.Friend
import com.example.chatexu.domain.model.FriendRequest
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.model.User
import com.example.chatexu.domain.repository.ChatRepository
import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.http.HTTP_OK
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject


class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApi
): ChatRepository {


//        val chatRowResponse = api.getChatRow(chatId, userId)
//        return chatRowResponse.body() ?: emptyList()

    override suspend fun getUserChatList(userId: String): List<ChatRow> {
        val chats = api.getUserChatList(userId).body() ?: emptyList()
//        Log.d("peek", "repo imp: size = ${chats.size}")
        return chats.map{ ChatMapper.toChatRow(it) }
    }

    override suspend fun getChatRow(chatId: String, viewerId: String): ChatRow {
        // TODO null
        return ChatMapper.toChatRow(api.getChatRow(chatId, viewerId).body()!!)
    }

    override suspend fun getAllChatMessages(chatId: String, userId: String): List<Message> {
        val messagesDtos: List<MessageDto> = api.getAllChatMessages(chatId, userId).body()
            ?: let{
//                Log.d("peek", "empty message list")
                emptyList<MessageDto>()
            }
        return messagesDtos.map { MessageMapper.toMessage(it) }
    }

    override suspend fun sendMessage(message: Message): Message {
        val messageDto = MessageMapper.toSend(message)
        val response = api.sendMessage(messageDto)
        Log.d(DebugConstants.TODO, "ChatRepositoryImpl.sendMessage() - handle response.")
        val body = response.body()

        return if(response.body() != null)
            MessageMapper.toMessage(body!!)
        else Message.getEmpty()

    }

    override suspend fun login(email: String, password: String): String {
        val loginDto = LoginDto(email, password)
        val response = api.login(loginDto)
//        Log.d("PEEK", "login() - repository: $response")
        return response.body()
            ?: Constants.ID_DEFAULT
    }

    override suspend fun register(email: String, password: String, nickname: String): User {
        // TODO already in use
        val registerDto = RegisterDto(nickname, email, password)
        val response = api.register(registerDto)
//        Log.d("PEEK", "register() - repository: $response")
        val user = UserMapper.toUser(response.body()!!)
        return user
    }

    override suspend fun getAllUsers(): List<User> {
        val userDtos: List<UserDto> = api.getAllUsers().body()
            ?: let {
//                Log.d("ChatRepositoryImpl.getAllUsers()", "getAllUsers - empty user list")
                emptyList()
            }
//        if(userDtos.isNotEmpty())
//            Log.d("ChatRepositoryImpl.getAllUsers()", "getAllUsers size: ${userDtos.size}")
        return userDtos.map { UserMapper.toUser(it) }
    }

    override suspend fun createUsersAndChat(): Boolean {
//        Log.d(DebugConstants.PEEK, "Creating two users and chat.")
        val result = api.createUsersAndChat()
        return result.isSuccessful
    }

    override suspend fun getUserById(userId: String): User {
        val response = api.getUserById(userId)
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

    override suspend fun getUserFriends(userId: String): List<Friend> {
//        Log.d(DebugConstants.PEEK, "---ChatRepositoryImpl - Fetching friends of $userId.")
        val result = api.getUserFriends(userId)
//        Log.d(DebugConstants.PEEK, "ChatRepositoryImpl - Fetched friends of $userId.")
        val friends = result.body()
            ?: let {
//                Log.w(DebugConstants.PEEK, "repositoryImpl - getUserFriends - null")
                emptyList()
            }
        return friends.map { FriendMapper.toFriend(it) }
    }

    override suspend fun getOrElseCreateChat(participants: List<String>): String {
//        Log.d(DebugConstants.PEEK, "get or create chat - repo impl")
        val participantsDto = ParticipantsDto(participants)
        val result = api.getOrElseCreateChat(participantsDto)
        return result.body() ?: "" // TODO id cannot be "", make sure of it
    }

    override suspend fun sendFriendRequest(senderId: String, recipientId: String): FriendRequest {
//        Log.d(DebugConstants.PEEK, "repoImpl::sendFriendRequest() - start")
        val result = api.sendFriendRequest(senderId, recipientId)
        // todo errors (http codes)
//        Log.d(DebugConstants.PEEK, "repoImpl::sendFriendRequest() - ${result.body().toString()}")

        return FriendRequestMapper.fromDto(result.body()!!)
    }

    override suspend fun deleteFriendRequest(requestId: String): Boolean {
//          this throws an error on success and wont reload list of user requests
//        val result = api.deleteFriendRequest(requestId)
//        return result.code() == HTTP_OK
//
        // this works, but may stop on another error (such as http 500)
        return try {
            val result = api.deleteFriendRequest(requestId)
            result.code() == HTTP_OK
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun postAcceptFriendRequest(requestId: String): Boolean {
        return try {
            val result = api.acceptFriendRequest(requestId)
            result.code() == HTTP_OK
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getAllFriendRequestsForUser(userId: String): List<FriendRequest> {
//        Log.d(DebugConstants.PEEK, "ChatRepositoryImpl.getAllFriendRequestsForUser() - start")
        val result = api.getAllFriendRequestsForUser(userId)
//        Log.d(DebugConstants.PEEK, "ChatRepositoryImpl.getAllFriendRequestsForUser() - fetched")
        return result.body()!!.map { FriendRequestMapper.fromDto(it) }
    }

    override suspend fun getUsersByPartOfNickname(
        userId: String,
        partOfNickname: String
    ): List<User> {
        // todo exception handling
        val result = api.getUsersByPartOfNickname(userId, partOfNickname)
        return result.body()!!.map { UserMapper.toUser(it) }
    }

    override suspend fun getChatParticipants(chatId: String): List<User> {
        val response = api.getChatParticipants(chatId)
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

    override suspend fun rejectFriendRequest(requestId: String): Boolean {
        return try {
            val result = api.rejectFriendRequest(requestId)
            result.code() == HTTP_OK
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun putUpdateIcon(userId: String, icon: MultipartBody.Part): User {
        val response = api.putUpdateIcon(userId, icon)
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

    override suspend fun putUpdateNickname(userId: String, nickname: String): User {
        val response = api.putUpdateNickname(userId, nickname)

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


    override suspend fun putSendImage(message: Message, image: MultipartBody.Part): String {

        val messageDto = MessageMapper.toSend(message)

        val response = api.postSendImage(messageDto, image)
        if(response.code() == HTTP_OK) {
            val messageId: String = response.body()
                ?: throw Exception("Unexpected error in ChatRepositoryImpl.postSendImage() - returned user is null")
            return messageId
        } else {
            val errorResponse = Response.error<String>(response.code(), "Unexpected error in ChatRepositoryImpl.postSendImage()".toResponseBody())
            throw HttpException(errorResponse)
        }

    }

    // maybe after adding refresh feature
//    override suspend fun getUserFriendsByNickname(
//        userId: String,
//        partOfNickname: String
//    ): List<Friend> {
//        Log.d(DebugConstants.PEEK, "ChatRepositoryImpl - Fetching friends of $userId, with phrase: $partOfNickname.")
//        val result = api.getUserFriendsByNickname(userId, partOfNickname)
//        Log.d(DebugConstants.PEEK, "ChatRepositoryImpl - Fetched friends of $userId, with phrase: $partOfNickname.")
//        val friends = result.body()
//            ?: let {
//                Log.w(DebugConstants.PEEK, "repositoryImpl - getUserFriendsByNickname - null")
//                emptyList()
//            }
//        return friends.map { FriendMapper.toFriend(it) }
//    }

}