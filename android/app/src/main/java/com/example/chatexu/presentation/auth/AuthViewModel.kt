package com.example.chatexu.presentation.auth

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.use_case.debug.get_all_users.GetAllUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
): ViewModel() {

    private val _state = mutableStateOf<AuthState>(AuthState())
    val state: State<AuthState> = _state

    init {
        getAllUsers()
    }

    private fun getAllUsers() {
        val users = getAllUsersUseCase()
        users.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    _state.value = AuthState(
                        users = result.data ?: emptyList()
                    )
                }

                is DataWrapper.Loading -> {
                    Log.d("peek", "Loading AuthViewModel")
                    Log.d(DebugConstants.VM_ERR, "Loading in: AuthViewModel.")
                    _state.value = AuthState(
                        error = result.message ?: "Unknown error"
                    )
                }

                is DataWrapper.Error -> {
                    Log.d("peek", "Error AuthViewModel")
                    _state.value = AuthState(isLoading = true)
                }
            }

        } .launchIn(viewModelScope)
    }

}