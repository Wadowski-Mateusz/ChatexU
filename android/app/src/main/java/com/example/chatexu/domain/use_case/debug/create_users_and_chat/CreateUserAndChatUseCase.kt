package com.example.chatexu.domain.use_case.debug.create_users_and_chat

import android.util.Log
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class CreateUserAndChatUseCase  @Inject constructor(
    private val repository: ChatRepository,
) {
    operator fun invoke(jwt: String): Flow<DataWrapper<Boolean>>  = flow {
        try {
            emit(DataWrapper.Loading<Boolean>())
            val result = repository.createUsersAndChat(jwt = jwt)
            emit(DataWrapper.Success<Boolean>(result))
        } catch(e: HttpException) {
            Log.e("peek", "e1/CreateUserAndChatUseCase: ${e.message.toString()}")
            emit(DataWrapper.Error<Boolean>(e.localizedMessage ?: e.message.toString()))
        } catch(e: java.io.IOException) {
            Log.e("peek", "e2/CreateUserAndChatUseCase: ${e.message.toString()}")
            emit(DataWrapper.Error<Boolean>("No internet connection."))
        }

    }
}