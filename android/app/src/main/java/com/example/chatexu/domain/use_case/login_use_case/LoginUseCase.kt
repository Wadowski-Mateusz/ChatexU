package com.example.chatexu.domain.use_case.login_use_case

import android.util.Log
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(login: String, password: String): Flow<DataWrapper<String>> = flow {

        try {
            emit(DataWrapper.Loading<String>())
            val userId = repository.login(login, password)
            emit(DataWrapper.Success<String>(userId))
        } catch(e: HttpException) {
            Log.e("peek", "LoginUseCase e1 ${e.message.toString()}")
            emit(DataWrapper.Error<String>(e.localizedMessage ?: e.message.toString()))
        } catch(e: IOException) {
            Log.e("peek", "LoginUseCase e2 ${e.message.toString()}")
            emit(DataWrapper.Error<String>("No internet connection."))
        }

    }
}