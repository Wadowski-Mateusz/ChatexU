package com.example.chatexu.presentation.user_options

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatexu.common.Constants
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.use_case.get_user_by_id_use_case.GetUserByIdUseCase
import com.example.chatexu.domain.use_case.put_update_user_icon_use_case.PutUpdateUserIconUseCase
import com.example.chatexu.domain.use_case.put_update_user_nickname_use_case.PutUpdateUserNicknameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.MultipartBody
import javax.inject.Inject


@HiltViewModel
class UserOptionsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val putUpdateUserNicknameUseCase: PutUpdateUserNicknameUseCase,
    private val putUpdateUserIconUseCase: PutUpdateUserIconUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase
    ): ViewModel() {

    private val _state = mutableStateOf<UserOptionsState>(UserOptionsState())
    val state: State<UserOptionsState> = _state


    init {
        val currentUserId: String = savedStateHandle.get<String>(Constants.PARAM_USER_ID) ?: Constants.ID_DEFAULT
        val jwt: String = savedStateHandle.get<String>(Constants.PARAM_JWT) ?: ""

        _state.value = _state.value.copy(
            currentUserId = currentUserId,
            jwt = jwt,
        )
        fetchUser()
    }

    fun updateNickname(nickname: String) {
        val response =  putUpdateUserNicknameUseCase(
            userId = _state.value.currentUserId,
            nickname = nickname,
            jwt = state.value.jwt
        )
        response.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
//                    Log.d(DebugConstants.PEEK, "UserOptionsViewModel.updateNickname() - success - start")
                    _state.value = _state.value.copy(
                        currentUser = result.data!!,
                        nicknameUpdateFailure = false,
                        isLoading = false,
                        error = "",
                    )

//                    Log.d(DebugConstants.PEEK, "UserOptionsViewModel.updateNickname() - success - end")
                }

                is DataWrapper.Loading -> {
                    Log.i(DebugConstants.RESOURCE_LOADING, "Loading in UserOptionsViewModel.updateNickname()")
                    _state.value = state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }

                is DataWrapper.Error -> {
                    Log.e(DebugConstants.VM_ERR, "Error in UserOptionsViewModel.updateNickname()")
                    _state.value = state.value.copy(
                        nicknameUpdateFailure = true,
                        isLoading = false,
                        error = result.message ?: "Unknown error"
                    )
                }

            }
        } .launchIn(viewModelScope)
    }


    fun updateIcon(icon: MultipartBody.Part) {

//        Log.w(DebugConstants.PEEK, "-----------------------------------------------")


        val response =  putUpdateUserIconUseCase(
            userId = state.value.currentUserId,
            icon = icon,
            jwt = state.value.jwt
        )
        response.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
//                    Log.d(DebugConstants.PEEK, "UserOptionsViewModel.updateIcon() - success - start")
                    _state.value = _state.value.copy(
                        currentUser = result.data!!,
                        isLoading = false,
                        error = ""
                    )
//                    Log.d(DebugConstants.PEEK, "UserOptionsViewModel.updateIcon() - success - end")
                }

                is DataWrapper.Loading -> {
                    Log.i(DebugConstants.RESOURCE_LOADING, "Loading in UserOptionsViewModel.updateIcon()")
                    _state.value = state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }

                is DataWrapper.Error -> {
                    Log.e(DebugConstants.VM_ERR, "Error in UserOptionsViewModel.updateIcon()")
                    _state.value = state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown error"
                    )
                }

            }
        } .launchIn(viewModelScope)
    }


    private fun fetchUser() {
        val response =  getUserByIdUseCase(
            userId = _state.value.currentUserId,
            jwt = state.value.jwt
        )
        response.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
//                    Log.d(DebugConstants.PEEK, "UserOptionsViewModel.fetchUser() - success - start")
                    _state.value = _state.value.copy(
                        currentUser = result.data!!,
                        isLoading = false,
                        error = ""
                    )
//                    Log.d(DebugConstants.PEEK, "UserOptionsViewModel.fetchUser() - success - end")
                }

                is DataWrapper.Loading -> {
                    Log.i(DebugConstants.RESOURCE_LOADING, "Loading in UserOptionsViewModel.fetchUser()")
                    _state.value = state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }

                is DataWrapper.Error -> {
                    Log.e(DebugConstants.VM_ERR, "Error in UserOptionsViewModel.fetchUser()")
                    _state.value = state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown error"
                    )
                }

            }
        } .launchIn(viewModelScope)
    }

}