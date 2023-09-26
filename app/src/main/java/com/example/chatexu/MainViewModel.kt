package com.example.chatexu

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.concurrent.fixedRateTimer

class MainViewModel: ViewModel() {

    private val repository = Repository()
    private val _modelData = MutableStateFlow("")
    val modelData = _modelData.asStateFlow()

    init {
        updateChatListPeriodic()
    }

    private fun updateChatListPeriodic() {
        fixedRateTimer(period = 1000L) { // every 1 second
            val chatList = repository.fetchChatList()
            _modelData.update { chatList[0] }
        }
    }

}