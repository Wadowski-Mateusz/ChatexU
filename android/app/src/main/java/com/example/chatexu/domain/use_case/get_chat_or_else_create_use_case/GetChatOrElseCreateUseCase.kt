package com.example.chatexu.domain.use_case.get_chat_or_else_create_use_case

import android.util.Log
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class GetChatOrElseCreateUseCase  @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(participants: List<String>): Flow<DataWrapper<String>>  = flow {
        try {
            emit(DataWrapper.Loading<String>())
            val result = repository.getOrElseCreateChat(participants)
            Log.d("PEEK", "UseCase: $result")
            emit(DataWrapper.Success<String>(result))
        } catch(e: HttpException) {
            Log.e("peek", "e1 |GetChatOrElseCreateUseCase: ${e.message.toString()}")
            emit(DataWrapper.Error<String>(e.localizedMessage ?: e.message.toString()))
        } catch(e: java.io.IOException) {
            Log.e("peek", "e2 |GetChatOrElseCreateUseCase: ${e.message.toString()}")
            emit(DataWrapper.Error<String>("No internet connection."))
        }

    }
}