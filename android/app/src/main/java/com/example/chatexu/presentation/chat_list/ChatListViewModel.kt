package com.example.chatexu.presentation.chat_list


import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatexu.common.Constants
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.use_case.get_chat_row.GetChatRowUseCase
import com.example.chatexu.domain.use_case.get_chat_rows.GetChatListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getChatListUseCase: GetChatListUseCase,
    private val getChatRowUseCase: GetChatRowUseCase,

    ): ViewModel() {

    private val _state = mutableStateOf<ChatListState>(ChatListState())
    val state: State<ChatListState> = _state


    init {
        savedStateHandle.get<String>(Constants.PARAM_USER_ID)?.let { chatId ->
            getChatList(chatId)
        }
    }

    private fun getChatList(userId: String) {
        val chats = getChatListUseCase(userId)
        chats.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d("peek", "Success ChatListViewModel")
                    _state.value = ChatListState(
                        chatRows = result.data ?: emptyList<ChatRow>(),
                        userId = userId
                    )
                }
                is DataWrapper.Loading -> {
                    Log.i(DebugConstants.RESOURCE_LOADING, "Loading in: ChatListViewModel.")
                    _state.value = ChatListState(
                        error = result.message ?: "Unknown error"
                    )
                }
                is DataWrapper.Error -> {
                    Log.d("peek", "Error ChatListViewModel")
                    _state.value = ChatListState(
                        isLoading = true,
                        userId = userId
                    )
                }

            }
        } .launchIn(viewModelScope)
    }


    // TODO no ids
    private fun getSingleChatRowAsList() {
        getChatRowUseCase() .onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    _state.value = ChatListState(
                        chatRows = listOfNotNull(result.data)
                    )
                }
                is DataWrapper.Loading -> {
                    Log.i(DebugConstants.RESOURCE_LOADING, "Error in: ChatListViewModel.")
                    _state.value = ChatListState(
                        error = result.message ?: "Unknown error"
                    )
                }
                is DataWrapper.Error -> {
                    _state.value = ChatListState(isLoading = true)
                }

            }
        } .launchIn(viewModelScope)

    }

}