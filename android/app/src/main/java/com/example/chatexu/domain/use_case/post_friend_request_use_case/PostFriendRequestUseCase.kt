package com.example.chatexu.domain.use_case.post_friend_request_use_case

import android.util.Log
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.model.FriendRequest
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PostFriendRequestUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(senderId: String, recipientId: String, jwt: String): Flow<DataWrapper<FriendRequest>> = flow {

        try {
            emit(DataWrapper.Loading<FriendRequest>())
            val friendRequest = repository.sendFriendRequest(
                senderId = senderId,
                recipientId = recipientId,
                jwt = jwt
            )
            Log.d(DebugConstants.USE_CASE, "PostFriendRequestUseCase()")
            emit(DataWrapper.Success<FriendRequest>(friendRequest))
        } catch(e: HttpException) {
            Log.e(DebugConstants.USE_CASE, "PostFriendRequestUseCase() / e1 ${e.message.toString()}")
            emit(DataWrapper.Error<FriendRequest>(e.localizedMessage ?: e.message.toString()))
        } catch(e: IOException) {
            Log.e(DebugConstants.USE_CASE, "PostFriendRequestUseCase() / e2 ${e.message.toString()}")
            emit(DataWrapper.Error<FriendRequest>("No internet connection."))
        }

    }
}