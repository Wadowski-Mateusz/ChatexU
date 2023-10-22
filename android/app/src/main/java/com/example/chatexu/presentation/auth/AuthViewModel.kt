package com.example.chatexu.presentation.auth

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.use_case.debug.create_users_and_chat.CreateUserAndChatUseCase
import com.example.chatexu.domain.use_case.debug.get_all_users.GetAllUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val createUserAndChatUseCase: CreateUserAndChatUseCase,
): ViewModel() {

    private val _state = mutableStateOf<AuthState>(AuthState())
    val state: State<AuthState> = _state

    init {
        getAllUsers()
    }

    fun create() {
        val result = createUserAndChatUseCase()

        result.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    getAllUsers()
                }

                is DataWrapper.Loading -> {
                    Log.d("peek", "Loading AuthViewModel.create()")
                    _state.value = _state.value.copy(
                        error = "",
                        isLoading = true
                    )
                }

                is DataWrapper.Error -> {
                    Log.d("peek", "Error AuthViewModel.create()")
                    _state.value = _state.value.copy(
                        error = result.message ?: "unknown error",
                        isLoading = true
                    )

                }
            }

        } .launchIn(viewModelScope)

    }

    private fun getAllUsers() {
        val users = getAllUsersUseCase()
        users.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    _state.value = AuthState(
                        users = result.data ?: emptyList(),
                        error = "",
                        isLoading = false,
                    )
                }

                is DataWrapper.Loading -> {
                    Log.d("peek", "Loading AuthViewModel.getAllUsers()")
                    _state.value = _state.value.copy(
                        error = "",
                        isLoading = true
                    )
                }

                is DataWrapper.Error -> {
                    Log.d("peek", "Error AuthViewModel.getAllUsers()")
                    _state.value = _state.value.copy(
                        error = result.message ?: "unknown error",
                        isLoading = true
                    )
                }
            }

        } .launchIn(viewModelScope)
    }

}