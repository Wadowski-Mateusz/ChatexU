package com.example.chatexu.domain.repository

import com.example.chatexu.domain.model.Authentication
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.model.Friend
import com.example.chatexu.domain.model.FriendRequest
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.model.User
import okhttp3.MultipartBody
import org.jetbrains.annotations.TestOnly

interface ChatRepository {

    suspend fun getUserChatList(userId: String, jwt: String): List<ChatRow>

    suspend fun getChatRow(chatId: String, viewerId: String, jwt: String): ChatRow

    suspend fun getAllChatMessages(chatId: String, userId: String, jwt: String): List<Message>

    suspend fun sendMessage(message: Message, jwt: String): Message

    suspend fun login(email: String, password: String): Authentication

    suspend fun register(email: String, nickname: String, password: String): Authentication

    @TestOnly
    suspend fun getAllUsers(jwt: String): List<User>

    @TestOnly
    suspend fun createUsersAndChat(jwt: String): Boolean

    suspend fun getUserById(userId: String, jwt: String): User
    suspend fun getUserFriends(userId: String, jwt: String): List<Friend>

    suspend fun getOrElseCreateChat(participants: List<String>, jwt: String): String

    suspend fun sendFriendRequest(senderId: String, recipientId: String, jwt: String): FriendRequest
    suspend fun deleteFriendRequest(requestId: String, jwt: String): Boolean
    suspend fun postAcceptFriendRequest(requestId: String, jwt: String): Boolean

    suspend fun getAllFriendRequestsForUser(userId: String, jwt: String): List<FriendRequest>

    suspend fun getUsersByPartOfNickname(userId: String, partOfNickname: String, jwt: String): List<User>
    suspend fun getChatParticipants(chatId: String, jwt: String): List<User>
    suspend fun rejectFriendRequest(requestId: String, jwt: String): Boolean

    suspend fun putUpdateIcon(userId: String, icon: MultipartBody.Part, jwt: String): User
    suspend fun putUpdateNickname(userId: String, nickname: String, jwt: String): User
    suspend fun putSendImage(message: Message, image: MultipartBody.Part, jwt: String): String

//    suspend fun getUserFriendsByNickname(userId: String, partOfNickname: String): List<Friend>

}