package com.example.chatexu.domain.use_case.get_chat_rows

import android.util.Log
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetChatListUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(userId: String): Flow<DataWrapper<List<ChatRow>>> = flow {

        try {
            emit(DataWrapper.Loading<List<ChatRow>>())
            // TODO id
//            Log.i(DebugConsts.TODO, "GetChatListUseCase() - hardcoded id")
//            val chats = repository.getUserChatList(DebugConsts.HARD_USER_ID)
            val chats = repository.getUserChatList(userId)
            Log.d("peek", "GetChatListUseCase() - no. of chats: ${chats.size.toString()}")
            emit(DataWrapper.Success<List<ChatRow>>(chats))
        } catch(e: HttpException) {
            Log.e("peek", "e1 ${e.message.toString()}")
            emit(DataWrapper.Error<List<ChatRow>>(e.localizedMessage ?: e.message.toString()))
        } catch(e: IOException) {
            Log.e("peek", "e2 ${e.message.toString()}")
            emit(DataWrapper.Error<List<ChatRow>>("No internet connection."))
        }

    }
}