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
        val jwt: String = savedStateHandle.get<String>(Constants.PARAM_JWT) ?: ""
        val userId: String = savedStateHandle.get<String>(Constants.PARAM_USER_ID) ?: Constants.ID_DEFAULT

        _state.value = ChatListState(userId = userId, jwt = jwt)

        if(userId != Constants.ID_DEFAULT) {
            getChatList(userId)
        }
    }

    private fun getChatList(userId: String) {
        val response = getChatListUseCase(userId = userId, jwt = _state.value.jwt)

        response.onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    Log.d(DebugConstants.PEEK, "Success ChatListViewModel - entered")

                    val chatRows = result.data?.sortedByDescending { it.timestamp } ?: emptyList<ChatRow>()

                    _state.value = _state.value.copy(
                        chatRows = chatRows,
                        isLoading = false,
                        error = ""
                    )
                }
                is DataWrapper.Loading -> {
                    Log.i(DebugConstants.RESOURCE_LOADING, "Loading in: ChatListViewModel.")
                    _state.value = _state.value.copy(
                        isLoading = true,
                        error = "",
                    )
                }
                is DataWrapper.Error -> {
                    Log.e(DebugConstants.VM_ERR, "Error ChatListViewModel")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown error"
                    )
                }

            }
        } .launchIn(viewModelScope)
    }


//    // TODO no ids
//    private fun getSingleChatRowAsList() {
//        getChatRowUseCase() .onEach { result ->
//            when(result) {
//                is DataWrapper.Success -> {
//                    _state.value = ChatListState(
//                        chatRows = listOfNotNull(result.data)
//                    )
//                }
//                is DataWrapper.Loading -> {
//                    Log.i(DebugConstants.RESOURCE_LOADING, "Error in: ChatListViewModel.")
//                    _state.value = ChatListState(
//                        error = result.message ?: "Unknown error"
//                    )
//                }
//                is DataWrapper.Error -> {
//                    _state.value = ChatListState(isLoading = true)
//                }
//
//            }
//        } .launchIn(viewModelScope)
//
//    }

}