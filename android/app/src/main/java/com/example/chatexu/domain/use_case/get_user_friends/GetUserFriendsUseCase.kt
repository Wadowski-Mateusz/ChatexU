package com.example.chatexu.domain.use_case.get_user_friends

import android.util.Log
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.model.Friend
import com.example.chatexu.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetUserFriendsUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke (userId: String, partOfNickname: String = "", jwt: String): Flow<DataWrapper<List<Friend>>> = flow {
        try {
            emit(DataWrapper.Loading<List<Friend>>())
            val friends: List<Friend> = repository.getUserFriends(userId = userId, jwt = jwt)
            Log.d(DebugConstants.PEEK, "GetUserFriendsUseCase() - no. of friends: ${friends.size}")
            emit(DataWrapper.Success<List<Friend>>(friends))
        } catch(e: HttpException) {
            Log.e(DebugConstants.PEEK, "GetUserFriendsUseCase() - e1 - ${e.message.toString()}")
            emit(DataWrapper.Error<List<Friend>>(e.localizedMessage ?: e.message.toString()))
        } catch(e: IOException) {
            Log.e(DebugConstants.PEEK, "GetUserFriendsUseCase() - e2 - ${e.message.toString()}")
            emit(DataWrapper.Error<List<Friend>>("No internet connection."))
        }
    }
}