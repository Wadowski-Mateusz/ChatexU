package com.example.chatexu.domain.use_case.get_chat_participants

import android.util.Log
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.model.Friend
import com.example.chatexu.domain.model.User
import com.example.chatexu.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetChatParticipantsUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke (chatId: String, jwt: String): Flow<DataWrapper<List<User>>> = flow {
        try {
            emit(DataWrapper.Loading<List<User>>())
            val participants: List<User> = repository.getChatParticipants(chatId = chatId, jwt = jwt)
            Log.d(DebugConstants.USE_CASE, "GetChatParticipantsUseCase() - no. of : ${participants.size}")
            emit(DataWrapper.Success<List<User>>(participants))
        } catch(e: HttpException) {
            Log.e(DebugConstants.UC_ERR, "GetChatParticipantsUseCase() - e1 - ${e.message.toString()}")
            emit(DataWrapper.Error<List<User>>(e.localizedMessage ?: e.message.toString()))
        } catch(e: IOException) {
            Log.e(DebugConstants.UC_ERR, "GetChatParticipantsUseCase() - e2 - ${e.message.toString()}")
            emit(DataWrapper.Error<List<User>>("No internet connection."))
        }
    }
}