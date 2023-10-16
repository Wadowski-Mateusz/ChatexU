package com.example.chatexu.presentation.chat

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatexu.common.Constants
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConsts
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.model.MessageType
import com.example.chatexu.domain.use_case.get_chat_messages.GetAllChatMessagesUseCase
import com.example.chatexu.presentation.chat_list.ChatListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllChatMessagesUseCase: GetAllChatMessagesUseCase,
): ViewModel() {


    // TODO separate chat id
    private val _state = mutableStateOf<ChatState>(ChatState())
    val state: State<ChatState> = _state


    init {
        savedStateHandle.get<String>(Constants.PARAM_CHAT_ID)?.let { chatId ->
            getMessages(chatId)
        }
    }

    private fun getMessages(chatId: String) {
        val messages = getAllChatMessagesUseCase()
        messages.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d("peek", "Success ChatViewModel; number of loaded messages: ${(result.data ?: emptyList<Message>()).size}")
                    _state.value = ChatState(
                        chatId = _state.value.chatId,
                        messages = result.data ?: emptyList<Message>()
                    )
                }
                is DataWrapper.Loading -> {
                    Log.d("peek", "Loading ChatViewModel")
                    Log.d(DebugConsts.VM_ERR, "Loading in: ChatViewModel.")
                    _state.value = ChatState(
                        error = result.message ?: "Unknown error"
                    )
                }
                is DataWrapper.Error -> {
                    Log.d("peek", "Error ChatViewModel")
                    _state.value = ChatState(
                        chatId = _state.value.chatId,
                        isLoading = true
                    )
                }

            }
        } .launchIn(viewModelScope)
    }

}