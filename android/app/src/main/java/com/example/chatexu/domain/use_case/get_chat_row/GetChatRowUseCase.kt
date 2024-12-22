package com.example.chatexu.domain.use_case.get_chat_row

import android.util.Log
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetChatRowUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(jwt: String): Flow<DataWrapper<ChatRow>> = flow {
        try {
            emit(DataWrapper.Loading<ChatRow>())
            // TODO id
            Log.e(DebugConstants.TODO, "GetChatRowUseCase() - hardcoded ids")
            val chat = repository.getChatRow("123", "123", jwt = jwt)
            emit(DataWrapper.Success<ChatRow>(chat))
        } catch(e: HttpException) {
            emit(DataWrapper.Error<ChatRow>(e.localizedMessage ?: "Unknown error"))
        } catch(e: IOException) {
            emit(DataWrapper.Error<ChatRow>("No internet connection."))
        }
    }
}