package com.example.chatexu.domain.repository

import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Nickname
import com.example.chatexu.common.Constants
import com.example.chatexu.data.remote.dto.FriendRequestDto
import com.example.chatexu.data.remote.dto.ParticipantsDto
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.model.Friend
import com.example.chatexu.domain.model.FriendRequest
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.model.User
import okhttp3.MultipartBody
import org.jetbrains.annotations.TestOnly
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatRepository {

    suspend fun getUserChatList(userId: String): List<ChatRow>

    suspend fun getChatRow(chatId: String, viewerId: String): ChatRow

    suspend fun getAllChatMessages(chatId: String, userId: String): List<Message>

    suspend fun sendMessage(message: Message): Message

    suspend fun login(email: String, password: String): String

    suspend fun register(email: String, password: String, nickname: String): User

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
    suspend fun rejectFriendRequest(requestId: String): Boolean

    suspend fun putUpdateIcon(userId: String, iconUri: MultipartBody.Part): User
    suspend fun putUpdateNickname(userId: String, nickname: String): User

//    suspend fun getUserFriendsByNickname(userId: String, partOfNickname: String): List<Friend>

}