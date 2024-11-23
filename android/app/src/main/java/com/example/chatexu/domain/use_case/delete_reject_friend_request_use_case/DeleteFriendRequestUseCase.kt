package com.example.chatexu.domain.use_case.delete_friend_request_use_case

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

class DeleteRejectFriendRequestUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(requestId: String): Flow<DataWrapper<Boolean>> = flow {

        try {
            emit(DataWrapper.Loading<Boolean>())
            val success = repository.rejectFriendRequest(requestId)
            Log.d(DebugConstants.PEEK, "DeleteRejectFriendRequestUseCase()")
            emit(DataWrapper.Success<Boolean>(success))
        } catch(e: HttpException) {
            Log.e(DebugConstants.PEEK, "DeleteRejectFriendRequestUseCase() / e1 ${e.message.toString()}")
            emit(DataWrapper.Error<Boolean>(e.localizedMessage ?: e.message.toString()))
        } catch(e: IOException) {
            Log.e(DebugConstants.PEEK, "DeleteRejectFriendRequestUseCase() / e2 ${e.message.toString()}")
            emit(DataWrapper.Error<Boolean>("No internet connection."))
        }

    }
}