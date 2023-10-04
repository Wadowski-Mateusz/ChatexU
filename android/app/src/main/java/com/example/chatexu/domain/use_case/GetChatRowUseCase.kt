package com.example.chatexu.domain.use_case

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
    operator fun invoke(): Flow<ChatRow> = flow {
        try {
//            emit() // ???


        } catch (e: HttpException) {

        } catch (e: IOException) {

        }
    }
}