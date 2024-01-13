package com.example.chatexu.domain.use_case.debug.get_all_users

import android.util.Log
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.model.User
import com.example.chatexu.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(): Flow<DataWrapper<List<User>>> = flow {
        try {
            emit(DataWrapper.Loading<List<User>>())
            val users: List<User> = repository.getAllUsers()
            emit(DataWrapper.Success<List<User>>(users))
        } catch(e: HttpException) {
            Log.e("peek", "e1/GetAllUsersUseCase: ${e.message.toString()}")
            emit(DataWrapper.Error<List<User>>(e.localizedMessage ?: e.message.toString()))
        } catch(e: java.io.IOException) {
            Log.e("peek", "e2/GetAllUsersUseCase: ${e.message.toString()}")
            emit(DataWrapper.Error<List<User>>("No internet connection."))
        }

    }
}