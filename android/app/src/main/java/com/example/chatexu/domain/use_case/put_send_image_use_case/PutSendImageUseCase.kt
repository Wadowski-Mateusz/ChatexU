package com.example.chatexu.domain.use_case.put_send_image_use_case

import android.util.Log
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PutSendImageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(message: Message, image: MultipartBody.Part): Flow<DataWrapper<String>> = flow {

        try {
            emit(DataWrapper.Loading<String>())
            val response = repository.putSendImage(message, image)
            Log.d("peek", "PutSendImageUseCase()")
            emit(DataWrapper.Success<String>(response))
        } catch(e: HttpException) {
            Log.e("peek", "PutSendImageUseCase() / e1 ${e.message.toString()}")
            emit(DataWrapper.Error<String>(e.localizedMessage ?: e.message.toString()))
        } catch(e: IOException) {
            Log.e("peek", "PutSendImageUseCase() / e2 ${e.message.toString()}")
            emit(DataWrapper.Error<String>("No internet connection."))
        }

    }
}