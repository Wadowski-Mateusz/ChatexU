package com.example.chatexu.presentation.auth

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatexu.common.Constants
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.common.isStringValid
import com.example.chatexu.domain.model.User
import com.example.chatexu.domain.use_case.debug.create_users_and_chat.CreateUserAndChatUseCase
import com.example.chatexu.domain.use_case.debug.get_all_users.GetAllUsersUseCase
import com.example.chatexu.domain.use_case.login_use_case.LoginUseCase
import com.example.chatexu.domain.use_case.register_use_case.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val createUserAndChatUseCase: CreateUserAndChatUseCase,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
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

    fun login(login: String, password: String) {
        val result = loginUseCase(login, password)

        result.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    val userId = result.data!!
                    if (ObjectId().isStringValid(userId)) {
                        _state.value = AuthState(
                            users = emptyList(),
                            error = "",
                            isLoading = false,
                            userId = result.data,
                        )
                    } else {
                        Log.e("PEEK", userId)
                        _state.value = AuthState(
                            users = emptyList(),
                            error = userId,
                            isLoading = false,
                            userId = Constants.ID_DEFAULT,
                            badLoginData = true
                        )
                    }
                }

                is DataWrapper.Loading -> {
                    Log.d("peek", "Loading AuthViewModel.login()")
                    _state.value = _state.value.copy(
                        error = "",
                        isLoading = true
                    )
                }

                is DataWrapper.Error -> {
                    Log.d("peek", "Error AuthViewModel.login()")
                    _state.value = _state.value.copy(
                        users = emptyList(),
                        userId = Constants.ID_DEFAULT,
                        error = result.message ?: "unknown error",
                        isLoading = false
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

    fun register(email: String, nickname: String, password: String) {
        val result = registerUseCase(email, nickname, password)

        result.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    val user = result.data!!
                    _state.value = AuthState(
                        users = emptyList(),
                        error = "",
                        isLoading = false,
                        userId = user.id
                    )
                }

                is DataWrapper.Loading -> {
                    Log.d("peek", "Loading AuthViewModel.login()")
                    _state.value = _state.value.copy(
                        error = "",
                        isLoading = true
                    )
                }

                is DataWrapper.Error -> {
                    Log.d("peek", "Error AuthViewModel.login()")
                    _state.value = _state.value.copy(
                        users = emptyList(),
                        userId = Constants.ID_DEFAULT,
                        error = result.message ?: "unknown error",
                        isLoading = false
                    )

                }
            }

        } .launchIn(viewModelScope)
    }

    fun showLogin() {
        _state.value = _state.value.copy(
            registerPage = false,
            loginPage = true
        )
    }

    fun showRegister() {
        _state.value = _state.value.copy(
            loginPage = false,
            registerPage = true
        )
    }

    fun setBadLoginDataAlertVisibility(value: Boolean) {
        _state.value = _state.value.copy(
            badLoginData = value
        )
    }



}