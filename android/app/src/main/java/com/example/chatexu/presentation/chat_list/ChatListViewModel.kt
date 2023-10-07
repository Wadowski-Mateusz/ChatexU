package com.example.chatexu.presentation.chat_list


import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatexu.common.DataWrapper
import com.example.chatexu.common.DebugConsts
import com.example.chatexu.domain.model.ChatRow
import com.example.chatexu.domain.use_case.get_chat_rows.GetChatRowsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val getChatListUseCase: GetChatRowsUseCase
    // etc...
): ViewModel() {

    private val _state = mutableStateOf<ChatListState>(ChatListState())
    val state: State<ChatListState> = _state


    init {
//        getRandList()
        getChatList()
    }


    private fun getRandList() {
        val rows = generateSequence { ChatRow.Builder().fastBuild() }
            .take(50)
            .toList()
        _state.value = ChatListState(chatRows = rows)
    }

    private fun getChatList() {
        getChatListUseCase().onEach { result ->
            when(result) {
                is DataWrapper.Success -> {
                    _state.value = ChatListState(
                        chatRows = result.data ?: emptyList<ChatRow>()
                    )
                }
                is DataWrapper.Loading -> {
                    Log.d(DebugConsts.VM_ERR, "Error in: ChatListViewModel.")
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