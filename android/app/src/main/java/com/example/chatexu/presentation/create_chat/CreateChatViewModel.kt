package com.example.chatexu.presentation.create_chat

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
import com.example.chatexu.domain.use_case.get_chat_or_else_create_use_case.GetChatOrElseCreateUseCase
import com.example.chatexu.domain.use_case.get_user_friends.GetUserFriendsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class CreateChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getUserFriendsUseCase: GetUserFriendsUseCase,
    private val getChatOrElseCreateUseCase: GetChatOrElseCreateUseCase,
): ViewModel() {
    private val _state = mutableStateOf<CreateChatState>(CreateChatState())
    val state: State<CreateChatState> = _state

    init {
        val jwt: String = savedStateHandle.get<String>(Constants.PARAM_JWT) ?: ""
        val userId: String = savedStateHandle.get<String>(Constants.PARAM_USER_ID) ?: Constants.ID_DEFAULT

        _state.value = _state.value.copy(
            userId = userId,
            jwt = jwt
        )

        if(userId != Constants.ID_DEFAULT)
            getUserFriends(userId)
    }

    fun filterFriendsByNickname(partOfNickname: String) {
        // todo loading?
        val trimmedPartOfNickname = partOfNickname.trim()
        _state.value = _state.value.copy(
            matchingFriends = state.value.friends.filter {
                it.nickname.contains(trimmedPartOfNickname, ignoreCase = true)
            },
            isLoading = false,
            error = ""
        )
    }

    fun getChatId(userId: String, friendId: String): String {
        val result = getChatOrElseCreateUseCase(
            participants = listOf(userId, friendId),
            jwt = state.value.jwt
        )

        var chatId: DataWrapper<String> = DataWrapper.Success<String>(Constants.ID_DEFAULT)
        runBlocking {
            result.collect { wrapper ->
                chatId = wrapper
                return@collect
            }
        }

        return chatId.data!!
    }

    private fun getUserFriends(userId: String) {
        val friends = getUserFriendsUseCase(userId, jwt = state.value.jwt)
        friends.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "Success getUserFriends")
                    _state.value = _state.value.copy(
                        friends = result.data ?: emptyList<Friend>(),
                        matchingFriends = result.data ?: emptyList<Friend>(),
                        isLoading = false,
                        error = ""
                    )
                }
                is DataWrapper.Loading -> {
                    Log.i(DebugConstants.RESOURCE_LOADING, "Loading getUserFriends()")
                    _state.value = state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }
                is DataWrapper.Error -> {
                    Log.e(DebugConstants.VM_ERR, "Error getUserFriends()")
                    _state.value = state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown error"
                    )
                }

            }
        } .launchIn(viewModelScope)
    }

}