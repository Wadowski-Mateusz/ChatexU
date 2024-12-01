package com.example.chatexu.domain.use_case.put_update_user_nickname_use_case

import android.util.Log
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.model.User
import com.example.chatexu.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PutUpdateUserNicknameUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(userId: String, nickname: String): Flow<DataWrapper<User>> = flow {

        try {
            emit(DataWrapper.Loading<User>())
            val success = repository.putUpdateNickname(userId, nickname)
            Log.d(DebugConstants.PEEK, "PutUpdateUserNicknameUseCase()")
            emit(DataWrapper.Success<User>(success))
        } catch(e: HttpException) {
            Log.e(DebugConstants.UC_ERR, "PutUpdateUserNicknameUseCase() / e1 ${e.message.toString()}")
            emit(DataWrapper.Error<User>(e.localizedMessage ?: e.message.toString()))
        } catch(e: IOException) {
            Log.e(DebugConstants.UC_ERR, "PutUpdateUserNicknameUseCase() / e2 ${e.message.toString()}")
            emit(DataWrapper.Error<User>("No internet connection."))
        }

    }
}