package com.example.chatexu.presentation.auth

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import com.example.chatexu.common.Constants
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.common.isStringValid
import com.example.chatexu.domain.model.Authentication
//import com.example.chatexu.domain.use_case.debug.create_users_and_chat.CreateUserAndChatUseCase
//import com.example.chatexu.domain.use_case.debug.get_all_users.GetAllUsersUseCase
import com.example.chatexu.domain.use_case.login_use_case.LoginUseCase
import com.example.chatexu.domain.use_case.register_use_case.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
//    private val getAllUsersUseCase: GetAllUsersUseCase,
//    private val createUserAndChatUseCase: CreateUserAndChatUseCase,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
): ViewModel() {

    private val _state = mutableStateOf<AuthState>(AuthState())
    val state: State<AuthState> = _state

    init {
        _state.value = AuthState(
            isLoading = true
        )
//        getAllUsers()
    }

//    fun create() {
//        val response = createUserAndChatUseCase()
//
//        response.onEach { result ->
//            when(result) {
//                is DataWrapper.Success -> {
//                    getAllUsers()
//                    _state.value = _state.value.copy(
//                        error = "",
//                        isLoading = false
//                    )
//                }
//
//                is DataWrapper.Loading -> {
//                    Log.d("peek", "Loading AuthViewModel.create()")
//                    _state.value = _state.value.copy(
//                        error = "",
//                        isLoading = true
//                    )
//                }
//
//                is DataWrapper.Error -> {
//                    Log.d("peek", "Error AuthViewModel.create()")
//                    _state.value = _state.value.copy(
//                        error = result.message ?: "unknown error",
//                        isLoading = false
//                    )
//                }
//            }
//
//        } .launchIn(viewModelScope)
//    }

    fun login(login: String, password: String) {
        val response = loginUseCase(login, password)

        response.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "Success AuthViewModel.login() - enter")
                    val data = result.data!!
                    val userId = getUserIdFromJWT(data.token)
                    if (ObjectId().isStringValid(userId)) {
//                        Log.d(DebugConstants.PEEK, "Success AuthViewModel.login() - if\nuserid: " + userId)
                        _state.value = _state.value.copy(
                            users = emptyList(),
                            userId = userId,
                            jwt = data.token,
                            loginStatus = true,
                            isLoading = false,
                            error = "",
                        )
                    } else {
//                        Log.i(DebugConstants.PEEK, "Success AuthViewModel.login() - else - invalid userid \n" + userId)
                        _state.value = _state.value.copy(
                            users = emptyList(),
                            error = userId,
                            loginStatus = false,
                            isLoading = false,
                            userId = Constants.ID_DEFAULT,
                            badLoginData = true
                        )
                    }
                }

                is DataWrapper.Loading -> {
                    Log.i(DebugConstants.RESOURCE_LOADING, "Loading AuthViewModel.login()")
                    _state.value = _state.value.copy(
                        error = "",
                        loginStatus = false,
                        isLoading = true
                    )
                }

                is DataWrapper.Error -> {
                    Log.e(DebugConstants.VM_ERR, "Error AuthViewModel.login()")
                    _state.value = _state.value.copy(
                        users = emptyList(),
                        userId = Constants.ID_DEFAULT,
                        error = result.message ?: "unknown error",
                        isLoading = false,
                        loginStatus = false,
                    )

                }
            }

        } .launchIn(viewModelScope)

    }



//    private fun getAllUsers() {
//        val users = getAllUsersUseCase()
//        users.onEach { result ->
//            when(result) {
//                is DataWrapper.Success -> {
//                    _state.value = _state.value.copy(
//                        users = result.data ?: emptyList(),
//                        error = "",
////                        isLoading = false,
//                    )
//                }
//
//                is DataWrapper.Loading -> {
//                    Log.i(DebugConstants.RESOURCE_LOADING, "Loading AuthViewModel.getAllUsers()")
//                    _state.value = _state.value.copy(
//                        error = "",
//                        isLoading = true
//                    )
//                }
//
//                is DataWrapper.Error -> {
//                    Log.d(DebugConstants.VM_ERR, "Error AuthViewModel.getAllUsers()")
//                    _state.value = _state.value.copy(
//                        error = result.message ?: "unknown error",
////                        isLoading = false
//                    )
//                }
//            }
//
//        } .launchIn(viewModelScope)
//    }



    fun registerUser(email: String, nickname: String, password: String) {

        viewModelScope.launch {
            register(email = email, nickname = nickname, password = password)
        }
    }

    private fun register(email: String, nickname: String, password: String) {
        _state.value = _state.value.copy(
            isLoading = true
        )

        val response = registerUseCase(email = email, nickname = nickname,  password = password)

        response.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {

//                    Log.d("REGISTER - VM", "VM register success")
                    val data: Authentication = result.data!!
                    val userId = getUserIdFromJWT(data.token)
                    if(ObjectId().isStringValid(userId)) {
                        _state.value = _state.value.copy(
                            users = emptyList(),
                            userId = userId,
                            jwt = data.token,
                            registerStatus = true,
                            isLoading = false,
                            error = "",
                        )
                    } else {
//                        Log.w(DebugConstants.PEEK, "vm - Register else")
                        val registerDataAlreadyInUseFlags = data.token.toInt()

                        val mailInUse = (registerDataAlreadyInUseFlags and 1) != 0
                        val nicknameInUse = (registerDataAlreadyInUseFlags and 2) != 0

                        _state.value = _state.value.copy(
                                badRegisterEmail = mailInUse,
                                badRegisterNickname = nicknameInUse,
                                isLoading = false,
                                error = "",
                            )
                    }
                }

                is DataWrapper.Loading -> {
                    Log.i(DebugConstants.RESOURCE_LOADING, "Loading AuthViewModel.register()")
                    _state.value = _state.value.copy(
                        loginPage = false,
                        registerPage = true,
                        isLoading = true,
                        error = "",
                    )
                }

                is DataWrapper.Error -> {
                    Log.e(DebugConstants.VM_ERR, "Error AuthViewModel.register()")
                    _state.value = _state.value.copy(
                        users = emptyList(),
                        userId = Constants.ID_DEFAULT,
                        loginPage = false,
                        registerPage = true,
                        isLoading = false,
                        error = result.message ?: "unknown error",
                    )

                }
            }

        } .launchIn(viewModelScope)
    }

    fun showLogin() {
        _state.value = _state.value.copy(
            badLoginData = false,
            isLoading = false,
            badRegisterEmail  = false,
            badRegisterNickname = false,
            badRegisterPasswords = false,
            error = "",
            registerPage = false,
            loginPage = true
        )
    }

    fun showRegister() {
        _state.value = _state.value.copy(
            badLoginData = false,
            isLoading = false,
            badRegisterEmail  = false,
            badRegisterNickname = false,
            badRegisterPasswords = false,
            error = "",
            loginPage = false,
            registerPage = true,
        )
    }

    fun validateRegisterInput(nickname: String, email: String, password: String, repeatedPassword: String): Boolean {

        _state.value = _state.value.copy(
            badRegisterPasswords = (
                    password != repeatedPassword
                            || password.isBlank()
                            || password.length < 8
                    )
        )


        _state.value = _state.value.copy(
            badRegisterEmail = (
                    email.count { it == '@' } != 1
                            || email.first() == '@'
                            || email.indexOf('@') >= email.length - 4
                    )
        )

        _state.value = _state.value.copy(
            badRegisterNickname = nickname.isBlank() || nickname.length < 3
        )


        return !state.value.badRegisterEmail && !state.value.badRegisterNickname && !state.value.badRegisterPasswords


    }

    fun setBadLoginDataAlertVisibility(value: Boolean) {
        _state.value = _state.value.copy(
            badLoginData = value
        )
    }

    fun trimInput(txt: TextFieldValue, txtLength: Int = 32): TextFieldValue {
        return if (txt.text.length > txtLength)
                txt.copy(text = txt.text.take(txtLength))
            else
                txt
    }

    private fun getUserIdFromJWT(jwt: String): String {
        val jwtDecoder = JWT(jwt)
        val userId = jwtDecoder.getClaim("userId").asString() ?: Constants.ID_DEFAULT
        return userId
    }

}