package com.example.chatexu.domain.use_case.get_user_friends_by_nickname

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

class GetUserFriendsByNicknameUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke (userId: String, partOfNickname: String = "", jwt: String): Flow<DataWrapper<List<Friend>>> = flow {
        TODO("Not implemented GetUserFriendsByNicknameUseCase")
//        try {
//            emit(DataWrapper.Loading<List<Friend>>())
//            val friends: List<Friend> = repository.getUserFriendsByNickname(userId, partOfNickname)
//            Log.d(DebugConstants.PEEK, "GetUserFriendsByNicknameUseCase() - no. of friends: ${friends.size}")
//            emit(DataWrapper.Success<List<Friend>>(friends))
//        } catch(e: HttpException) {
//            Log.e(DebugConstants.PEEK, "GetUserFriendsByNicknameUseCase() - e1 - ${e.message.toString()}")
//            emit(DataWrapper.Error<List<Friend>>(e.localizedMessage ?: e.message.toString()))
//        } catch(e: IOException) {
//            Log.e(DebugConstants.PEEK, "GetUserFriendsByNicknameUseCase() - e2 - ${e.message.toString()}")
//            emit(DataWrapper.Error<List<Friend>>("No internet connection."))
//        }
    }
}