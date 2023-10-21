package com.example.chatexu.presentation.chat

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatexu.common.Constants
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.use_case.get_chat_messages.GetAllChatMessagesUseCase
import com.example.chatexu.domain.use_case.post_message.PostMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllChatMessagesUseCase: GetAllChatMessagesUseCase,
    private val postMessageUseCase: PostMessageUseCase,
): ViewModel() {

    // TODO separate chat id
    private val _state = mutableStateOf<ChatState>(ChatState())
    val state: State<ChatState> = _state


    init {
        val chatId: String? = savedStateHandle.get<String>(Constants.PARAM_CHAT_ID)
        val userId: String? = savedStateHandle.get<String>(Constants.PARAM_USER_ID)

        Log.d("peek", "ChatViewModel - ids: chatId: $chatId, userId: $userId")

        chatId?.let {
            userId?. let {
                getMessages(chatId, userId)
            }
        }
    }

    public fun sendMessage(message: Message) {
        val responseMessage = postMessageUseCase(message)

        responseMessage.onEach { result -> // TODO what to use instead of 'onEach'
            when(result) {
                is DataWrapper.Success -> {
                    val messages = if (result.data != null)
                        state.value.messages + result.data
                    else
                        state.value.messages
                    Log.d("peek", "Success ChatViewModel; sendMessage; total messages = ${messages.size}")
                    _state.value = ChatState(
                        chatId = state.value.chatId,
                        userId = state.value.userId,
                        messages = messages
                    )
                }
                is DataWrapper.Loading -> {
                    Log.d(DebugConstants.VM_ERR, "Loading in: ChatViewModel, sendMessage.")
//                    _state.value = ChatState(
//                        error = result.message
//                            ?: "Unknown error",
//                        messages = state.value.messages
//                    )
                    _state.value = state.value.copy(
                        error = result.message?: "Unknown error",
                        isLoading = false,
                    )
                }
                is DataWrapper.Error -> {
                    Log.d("peek", "Error ChatViewModel, sendMessage")
//                    _state.value = ChatState(
//                        chatId = state.value.chatId,
//                        userId = state.value.userId,
//                        isLoading = true
//                    )
                    _state.value = state.value.copy(
                        error = "",
                        isLoading = true,
                    )
                }

            }
        } .launchIn(viewModelScope)
    }


    private fun getMessages(chatId: String, userId: String) {
        val messages = getAllChatMessagesUseCase(chatId, userId)
        messages.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d("peek", "Success ChatViewModel; number of loaded messages: ${(result.data ?: emptyList()).size}")
                    _state.value = ChatState(
                        chatId = chatId,
                        userId = userId,
                        messages = result.data ?: emptyList()
                    )
                }
                is DataWrapper.Loading -> {
                    Log.d("peek", "Loading ChatViewModel")
                    Log.d(DebugConstants.VM_ERR, "Loading in: ChatViewModel.")
                    _state.value = ChatState(
                        error = result.message
                            ?: "Unknown error"
                    )
                }
                is DataWrapper.Error -> {
                    Log.d("peek", "Error ChatViewModel")
                    _state.value = ChatState(
                        chatId = chatId,
                        userId = userId,
                        isLoading = true
                    )
                }

            }
        } .launchIn(viewModelScope)
    }

}