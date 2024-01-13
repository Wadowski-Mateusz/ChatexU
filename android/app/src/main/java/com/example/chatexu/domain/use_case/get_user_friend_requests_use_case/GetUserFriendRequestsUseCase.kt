package com.example.chatexu.domain.use_case.get_user_friend_requests_use_case

import android.util.Log
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.model.Friend
import com.example.chatexu.domain.model.FriendRequest
import com.example.chatexu.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetUserFriendRequestsUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke (userId: String): Flow<DataWrapper<List<FriendRequest>>> = flow {
        try {
            emit(DataWrapper.Loading<List<FriendRequest>>())
            val requests: List<FriendRequest> = repository.getAllFriendRequestsForUser(userId)
            Log.d(DebugConstants.PEEK, "GetUserFriendRequestsUseCase() - no. of requests: ${requests.size}")
            emit(DataWrapper.Success<List<FriendRequest>>(requests))
        } catch(e: HttpException) {
            Log.e(DebugConstants.PEEK, "GetUserFriendRequestsUseCase() - e1 - ${e.message.toString()}")
            emit(DataWrapper.Error<List<FriendRequest>>(e.localizedMessage ?: e.message.toString()))
        } catch(e: IOException) {
            Log.e(DebugConstants.PEEK, "GetUserFriendRequestsUseCase() - e2 - ${e.message.toString()}")
            emit(DataWrapper.Error<List<FriendRequest>>("No internet connection."))
        }
    }
}