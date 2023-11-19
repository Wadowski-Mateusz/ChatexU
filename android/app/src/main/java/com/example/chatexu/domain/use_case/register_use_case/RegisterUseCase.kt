package com.example.chatexu.domain.use_case.register_use_case

import android.util.Log
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.domain.model.User
import com.example.chatexu.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(email: String, nickname: String, password: String): Flow<DataWrapper<User>> = flow {

        try {
            emit(DataWrapper.Loading<User>())
            val userId = repository.register(email, nickname, password)
            emit(DataWrapper.Success<User>(userId))
        } catch(e: HttpException) {
            Log.e("peek", "LoginUseCase e1 ${e.message.toString()}")
            emit(DataWrapper.Error<User>(e.localizedMessage ?: e.message.toString()))
        } catch(e: IOException) {
            Log.e("peek", "LoginUseCase e2 ${e.message.toString()}")
            emit(DataWrapper.Error<User>("No internet connection."))
        }

    }
}