package com.example.chatexu.domain.use_case.get_users_by_part_of_nickname_use_case

import android.util.Log
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.model.Friend
import com.example.chatexu.domain.model.User
import com.example.chatexu.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetUsersByPartOfNicknameUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke (userId: String, partOfNickname: String, jwt: String): Flow<DataWrapper<List<User>>> = flow {
        try {
            emit(DataWrapper.Loading<List<User>>())
            val users: List<User> = repository.getUsersByPartOfNickname(
                userId = userId,
                partOfNickname = partOfNickname,
                jwt = jwt
            )
            Log.d(DebugConstants.PEEK, "GetUsersByPartOfNicknameUseCase() - no. of users: ${users.size}")
            emit(DataWrapper.Success<List<User>>(users))
        } catch(e: HttpException) {
            Log.e(DebugConstants.PEEK, "GetUsersByPartOfNicknameUseCase() - e1 - ${e.message.toString()}")
            emit(DataWrapper.Error<List<User>>(e.localizedMessage ?: e.message.toString()))
        } catch(e: IOException) {
            Log.e(DebugConstants.PEEK, "GetUsersByPartOfNicknameUseCase() - e2 - ${e.message.toString()}")
            emit(DataWrapper.Error<List<User>>("No internet connection."))
        }
    }
}