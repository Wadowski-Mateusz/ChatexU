package com.example.chatexu.domain.repository

import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.model.Friend
import com.example.chatexu.domain.model.FriendRequest
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.model.User
import okhttp3.MultipartBody
import org.jetbrains.annotations.TestOnly

interface ChatRepository {

    suspend fun getUserChatList(userId: String): List<ChatRow>

    suspend fun getChatRow(chatId: String, viewerId: String): ChatRow

    suspend fun getAllChatMessages(chatId: String, userId: String): List<Message>

    suspend fun sendMessage(message: Message): Message

    suspend fun login(email: String, password: String): String

    suspend fun register(email: String, nickname: String, password: String): User

    @TestOnly
    suspend fun getAllUsers(): List<User>

    @TestOnly
    suspend fun createUsersAndChat(): Boolean

    suspend fun getUserById(userId: String): User
    suspend fun getUserFriends(userId: String): List<Friend>

    suspend fun getOrElseCreateChat(participants: List<String>): String

    suspend fun sendFriendRequest(senderId: String, recipientId: String): FriendRequest
    suspend fun deleteFriendRequest(requestId: String): Boolean
    suspend fun postAcceptFriendRequest(requestId: String): Boolean

    suspend fun getAllFriendRequestsForUser(userId: String): List<FriendRequest>

    suspend fun getUsersByPartOfNickname(userId: String, partOfNickname: String): List<User>
    suspend fun getChatParticipants(chatId: String): List<User>
    suspend fun rejectFriendRequest(requestId: String): Boolean

    suspend fun putUpdateIcon(userId: String, icon: MultipartBody.Part): User
    suspend fun putUpdateNickname(userId: String, nickname: String): User
    suspend fun putSendImage(message: Message, image: MultipartBody.Part): String

//    suspend fun getUserFriendsByNickname(userId: String, partOfNickname: String): List<Friend>

}