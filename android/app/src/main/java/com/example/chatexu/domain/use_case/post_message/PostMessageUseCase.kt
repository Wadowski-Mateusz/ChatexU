package com.example.chatexu.domain.use_case.post_message

import android.util.Log
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PostMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(message: Message, jwt: String): Flow<DataWrapper<Message>> = flow {

        try {
            emit(DataWrapper.Loading<Message>())
            val messageResponse = repository.sendMessage(message = message, jwt = jwt)
            Log.d("peek", "PostMessageUseCase()")
            emit(DataWrapper.Success<Message>(messageResponse))
        } catch(e: HttpException) {
            Log.e("peek", "PostMessageUseCase() / e1 ${e.message.toString()}")
            emit(DataWrapper.Error<Message>(e.localizedMessage ?: e.message.toString()))
        } catch(e: IOException) {
            Log.e("peek", "PostMessageUseCase() / e2 ${e.message.toString()}")
            emit(DataWrapper.Error<Message>("No internet connection."))
        }

    }
}