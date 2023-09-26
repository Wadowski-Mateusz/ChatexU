package com.example.chatexu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatexu.data.models.ChatRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.time.Instant

class MainViewModel(app: Application): AndroidViewModel(app) {
    private val repository = Repository(app.applicationContext)

    fun getChatRows(): Flow<List<ChatRow>> = repository.ChatRowRepo().getAll()


    init {
        populateDatabase()
    }

    private fun populateDatabase() {
        val chatRows = generateSequence { ChatRow(
            chatName = "Chat nickname",
            message = "Message",
            from = Instant.now(),
            muted = false
        ) }
            .take(3)
            .toList()

        CoroutineScope(viewModelScope.coroutineContext).launch {
            repository.ChatRowRepo().delete(
                repository.ChatRowRepo().getAll().first()
            )
            repository.ChatRowRepo().insertAll(chatRows)
        }
    }

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