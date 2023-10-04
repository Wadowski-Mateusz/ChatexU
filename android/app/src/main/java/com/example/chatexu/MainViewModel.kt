package com.example.chatexu

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatexu.domain.model.ChatRow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel(app: Application): AndroidViewModel(app) {



    private val repository = Repository(app.applicationContext)

    private val _chatRowList = MutableStateFlow<List<ChatRow>>(emptyList())
    val chatRowList = _chatRowList.asStateFlow()

    fun getChatRows(chatId: UUID, viewerId: UUID, context: Context) = viewModelScope.launch {
        Log.d("HERE", "getChatRows 1")
        val rows = repository.testChatList(context = context)
//        val rows = repository.ChatRowRepo().loadChatViewList(chatId, viewerId)
//        val rows = repository.ChatRowRepo().loadChatView(chatId, viewerId)
        Log.d("HERE", "getChatRows 2")
        _chatRowList.update { rows }
    }


// ROOM
//    fun getChatRows(): Flow<List<ChatRow>> = repository.ChatRowRepo().getAll()
//    init {
//        populateDatabase()
//    }
//
//    private fun populateDatabase() {
//        val chatRows = generateSequence { ChatRow(
//            chatName = "Chat nickname",
//            message = "Message",
//            from = Instant.now(),
//            muted = false
//        ) }
//            .take(3)
//            .toList()
//
//        CoroutineScope(viewModelScope.coroutineContext).launch {
//            repository.ChatRowRepo().delete(
//                repository.ChatRowRepo().getAll().first()
//            )
//            repository.ChatRowRepo().insertAll(chatRows)
//        }
//    }

//    private val _modelData = MutableStateFlow("")
//    val modelData = _modelData.asStateFlow()
//
//    init {
//        updateChatListPeriodic()
//    }
//
//    private fun updateChatListPeriodic() {
//        fixedRateTimer(period = 1000L) { // every 1 second
//            val chatList = repository.fetchChatList()
//            _modelData.update { chatList[0] }
//        }
//    }

}