package com.example.chatexu.domain.use_case.register_use_case

import android.util.Log
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.model.Authentication
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
    operator fun invoke(email: String, nickname: String, password: String): Flow<DataWrapper<Authentication>> = flow {

        try {
            emit(DataWrapper.Loading<Authentication>())
            val authentication = repository.register(email = email, nickname = nickname, password =  password)
            emit(DataWrapper.Success<Authentication>(authentication))
        } catch(e: HttpException) {
            Log.e(DebugConstants.UC_ERR, "RegisterUseCase e1 ${e.message.toString()}")
            emit(DataWrapper.Error<Authentication>(e.localizedMessage ?: e.message.toString()))
        } catch(e: IOException) {
            Log.e(DebugConstants.UC_ERR, "RegisterUseCase e2 ${e.message.toString()}")
            emit(DataWrapper.Error<Authentication>("No internet connection."))
        }

    }
}