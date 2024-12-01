package com.example.chatexu.presentation.user_options

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatexu.common.Constants
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.model.Friend
import com.example.chatexu.domain.model.FriendRequest
import com.example.chatexu.domain.model.User
import com.example.chatexu.domain.use_case.debug.get_all_users.GetAllUsersUseCase
import com.example.chatexu.domain.use_case.delete_friend_request_use_case.DeleteFriendRequestUseCase
import com.example.chatexu.domain.use_case.delete_friend_request_use_case.DeleteRejectFriendRequestUseCase
import com.example.chatexu.domain.use_case.get_user_by_id_use_case.GetUserByIdUseCase
import com.example.chatexu.domain.use_case.get_user_friend_requests_use_case.GetUserFriendRequestsUseCase
import com.example.chatexu.domain.use_case.get_user_friends.GetUserFriendsUseCase
import com.example.chatexu.domain.use_case.get_users_by_part_of_nickname_use_case.GetUsersByPartOfNicknameUseCase
import com.example.chatexu.domain.use_case.post_accept_friend_request_use_case.AcceptFriendRequestUseCase
import com.example.chatexu.domain.use_case.post_friend_request_use_case.PostFriendRequestUseCase
import com.example.chatexu.domain.use_case.put_update_user_icon_use_case.PutUpdateUserIconUseCase
import com.example.chatexu.domain.use_case.put_update_user_nickname_use_case.PutUpdateUserNicknameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import javax.inject.Inject

//import android.util.Log
//import androidx.compose.runtime.State
//import androidx.compose.runtime.mutableStateOf
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.chatexu.common.DataWrapper
//import com.example.chatexu.common.Constants
//import com.example.chatexu.common.DebugConstants
//import com.example.chatexu.domain.use_case.get_user_by_id_use_case.GetUserByIdUseCase
//import com.example.chatexu.domain.use_case.put_update_user_icon_use_case.PutUpdateUserIconUseCase
//import com.example.chatexu.domain.use_case.put_update_user_nickname_use_case.PutUpdateUserNicknameUseCase
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.launchIn
//import kotlinx.coroutines.flow.onEach
//import javax.inject.Inject

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
        _state.value = _state.value.copy(
            currentUserId = savedStateHandle.get<String>(Constants.PARAM_USER_ID)
                ?: Constants.ID_DEFAULT // TODO what if null, can it be null?
        )
        fetchUser()
    }

    fun updateNickname(nickname: String) {
        val response =  putUpdateUserNicknameUseCase(_state.value.currentUserId, nickname)
        response.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "UserOptionsViewModel.updateNickname() - success - start")
                    _state.value = _state.value.copy(
                        currentUser = result.data!!,
                        isLoading = false,
                        error = ""
                    )
                    Log.d(DebugConstants.PEEK, "UserOptionsViewModel.updateNickname() - success - end")
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
                        isLoading = false,
                        error = result.message ?: "Unknown error"
                    )
                }

            }
        } .launchIn(viewModelScope)
    }

    fun updateIcon(icon: MultipartBody.Part) {
        val response =  putUpdateUserIconUseCase(_state.value.currentUserId, icon)
        response.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "UserOptionsViewModel.updateIcon() - success - start")
                    _state.value = _state.value.copy(
                        currentUser = result.data!!,
                        isLoading = false,
                        error = ""
                    )
                    Log.d(DebugConstants.PEEK, "UserOptionsViewModel.updateIcon() - success - end")
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
        val response =  getUserByIdUseCase(_state.value.currentUserId)
        response.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "UserOptionsViewModel.fetchUser() - success - start")
                    _state.value = _state.value.copy(
                        currentUser = result.data!!,
                        isLoading = false,
                        error = ""
                    )
                    Log.d(DebugConstants.PEEK, "UserOptionsViewModel.fetchUser() - success - end")
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