package com.example.chatexu.domain.use_case.get_chat_rows

import android.util.Log
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConsts
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class GetChatRowsUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(): Flow<DataWrapper<List<ChatRow>>> = flow {

        try {
            emit(DataWrapper.Loading<List<ChatRow>>())
            Log.d(DebugConsts.TODO, "GetChatRowsUseCase() - user id")
//            val coins = repository.getUserChatRows(UUID.randomUUID())//.map { it.toChatRow() }
            val coins = repository.getUserChatRows()
            emit(DataWrapper.Success<List<ChatRow>>(coins))
        } catch(e: HttpException) {
            emit(DataWrapper.Error<List<ChatRow>>(e.localizedMessage ?: "Unknown error"))
        } catch(e: IOException) {
            emit(DataWrapper.Error<List<ChatRow>>("No internet connection."))
        }
    }
}