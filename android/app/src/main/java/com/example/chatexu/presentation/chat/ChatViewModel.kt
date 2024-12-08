package com.example.chatexu.presentation.chat

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatexu.common.Constants
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConstants
import com.example.chatexu.domain.model.Message
import com.example.chatexu.domain.use_case.get_chat_messages.GetAllChatMessagesUseCase
import com.example.chatexu.domain.use_case.get_chat_participants.GetChatParticipantsUseCase
import com.example.chatexu.domain.use_case.post_message.PostMessageUseCase
import com.example.chatexu.domain.use_case.put_send_image_use_case.PutSendImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllChatMessagesUseCase: GetAllChatMessagesUseCase,
    private val postMessageUseCase: PostMessageUseCase,
    private val getChatParticipantsUseCase: GetChatParticipantsUseCase,
    private val putSendImageUseCase: PutSendImageUseCase,
): ViewModel() {

    // TODO separate chat id
    private val _state = mutableStateOf<ChatState>(ChatState())
    val state: State<ChatState> = _state


    init {
        val chatId: String? = savedStateHandle.get<String>(Constants.PARAM_CHAT_ID)
        val userId: String? = savedStateHandle.get<String>(Constants.PARAM_USER_ID)

        _state.value.chatId = chatId!! // TODO why it would be null?
        _state.value.userId = userId!!



        getMessagesPeriodically()

        getChatParticipants()

        // if chatID and userID could be null
//        chatId?.let {
//            userId?. let {
//                getMessagesPeriodically(chatId, userId)
//              //  getMessages(chatId, userId)
//            }
//        }


    }

    private fun getChatParticipants() {

        val response = getChatParticipantsUseCase(_state.value.chatId)

        response.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    require(result.data != null) { "ChatViewModel.getChatParticipants() response body is null" }
                    _state.value = _state.value.copy(
                        user = result.data.first { it.id == _state.value.userId },
                        recipients = result.data.filterNot { it.id == _state.value.userId },
                        isLoading = false,
                        error = ""
                    )
                }
                is DataWrapper.Loading -> {
//                    Log.i(DebugConstants.RESOURCE_LOADING, "Loading ChatViewModel.getChatParticipants()")
                    _state.value = _state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }
                is DataWrapper.Error -> {
                    Log.e(DebugConstants.VM_ERR, "Error ChatViewModel.getChatParticipants()")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown error"
                    )
                }

            }
        } .launchIn(viewModelScope)


    }

    private fun getMessagesPeriodically() {
        viewModelScope.launch {
            while (true) {
                getMessages()
                delay(1000)
            }
        }
    }

    private fun getMessages() {
        val messages = getAllChatMessagesUseCase(_state.value.chatId, _state.value.userId)
        messages.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    _state.value = _state.value.copy(
                        messages = result.data ?: emptyList(),
                        isLoading = false,
                        error = ""
                    )
                }
                is DataWrapper.Loading -> {
//                    Log.i(DebugConstants.RESOURCE_LOADING, "Loading ChatViewModel.getMessages()")
                    _state.value = _state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }
                is DataWrapper.Error -> {
                    Log.e(DebugConstants.VM_ERR, "Error ChatViewModel.getMessages()")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown error"
                    )
                }

            }
        } .launchIn(viewModelScope)
    }



    fun sendMessage(message: Message) {
        val responseMessage = postMessageUseCase(message)

        responseMessage.onEach { result -> // TODO what to use instead of 'onEach'
            when(result) {
                is DataWrapper.Success -> {
                    val messages = if (result.data != null)
                        state.value.messages + result.data
                    else
                        state.value.messages
                    _state.value = _state.value.copy(
                        messages = messages,
                        isLoading = false,
                        error = ""
                    )
                }
                is DataWrapper.Loading -> {
                    Log.i(DebugConstants.RESOURCE_LOADING, "Loading in: ChatViewModel.sendMessage()")
                    _state.value = state.value.copy(
                        isLoading = true,
                        error = "",
                    )
                }
                is DataWrapper.Error -> {
                    Log.e(DebugConstants.VM_ERR, "Error ChatViewModel.sendMessage()")
                    _state.value = state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown error"
                    )
                }

            }
        } .launchIn(viewModelScope)
    }


    fun sendImage(message: Message, image: MultipartBody.Part) {

        val response = putSendImageUseCase(message, image)
        response.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "ChatViewModel.sendImage() - success - start")
                    _state.value = _state.value.copy(
                        // TODO load messages?
//                        currentUser = result.data!!,
                        isLoading = false,
                        error = ""
                    )
                    Log.d(DebugConstants.PEEK, "ChatViewModel.sendImage() - success - end")
                }

                is DataWrapper.Loading -> {
                    Log.i(DebugConstants.RESOURCE_LOADING, "Loading in ChatViewModel.sendImage()")
                    _state.value = state.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }

                is DataWrapper.Error -> {
                    Log.e(DebugConstants.VM_ERR, "Error in ChatViewModel.sendImage()")
                    _state.value = state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown error"
                    )
                }

            }
        } .launchIn(viewModelScope)
    }

}