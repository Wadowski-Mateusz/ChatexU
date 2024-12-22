package com.example.chatexu.domain.use_case.get_user_by_id_use_case

import android.util.Log
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.model.User
import com.example.chatexu.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke (userId: String, jwt: String): Flow<DataWrapper<User>> = flow {
        try {
            emit(DataWrapper.Loading<User>())
            val user: User = repository.getUserById(userId = userId, jwt = jwt)
            Log.d(DebugConstants.PEEK, "GetUserByIdUseCase()")
            emit(DataWrapper.Success<User>(user))
        } catch(e: HttpException) {
            Log.e(DebugConstants.UC_ERR, "GetUsersByPartOfNicknameUseCase() - e1 - ${e.message.toString()}")
            emit(DataWrapper.Error<User>(e.localizedMessage ?: e.message.toString()))
        } catch(e: IOException) {
            Log.e(DebugConstants.UC_ERR, "GetUsersByPartOfNicknameUseCase() - e2 - ${e.message.toString()}")
            emit(DataWrapper.Error<User>("No internet connection."))
        }
    }
}