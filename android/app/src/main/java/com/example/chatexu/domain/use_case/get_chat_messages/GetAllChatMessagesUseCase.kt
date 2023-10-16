package com.example.chatexu.domain.use_case.get_chat_messages

import android.util.Log
import com.example.chatexu.common.Constants
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConsts
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.model.MessageType
import com.example.chatexu.domain.repository.ChatRepository
import com.example.chatexu.presentation.chat.ChatState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import java.time.Instant
import javax.inject.Inject
import kotlin.random.Random

class GetAllChatMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(
        chatId: String = DebugConsts.HARD_CHAT_ID,
        userId: String = DebugConsts.HARD_USER_ID,
    ): Flow<DataWrapper<List<Message>>> = flow {
        try {
            emit(DataWrapper.Loading<List<Message>>())
            // TODO id
            Log.i(DebugConsts.TODO, "GetChatMessagesUseCase() - hardcoded id")
//            val messages = repository.getAllChatMessages(chatId, userId)
            val m = Message(
                messageId = Constants.ID_DEFAULT,
                senderId = Constants.ID_DEFAULT,
                chatId = Constants.ID_DEFAULT,
                timestamp = Instant.now(),
                messageType = MessageType.Text("Message 1"),
                isEdited = false,
                answerTo = Constants.ID_DEFAULT
            )
            val a = generateSequence { m.copy(messageId = (Random.nextInt()).toString()) }
                .take(50)
                .toList()
            val messages = mutableListOf(
                m,
                m.copy(messageType = MessageType.Text("Message 2"), messageId = "123")
            )
            messages.addAll(a)


            emit(DataWrapper.Success<List<Message>>(messages))

        } catch(e: HttpException) {
            Log.e("peek", "e1 ${e.message.toString()}")
            emit(DataWrapper.Error<List<Message>>(e.localizedMessage ?: e.message.toString()))
        } catch(e: java.io.IOException) {
            Log.e("peek", "e2 ${e.message.toString()}")
            emit(DataWrapper.Error<List<Message>>("No internet connection."))
        }

    }

}