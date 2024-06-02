package com.harsh_kumar.hello.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.harsh_kumar.hello.models.types.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList

class MainViewModel:ViewModel() {
    private val _socketStatus = mutableStateOf(false)
    val socketStatus: State<Boolean> = _socketStatus
    private val _messages = mutableStateOf(listOf<Message>())
    val messages: State<List<Message>>  = _messages


    fun setSocketStatus(status:Boolean) = GlobalScope.launch(Dispatchers.IO) {
        _socketStatus.value = status
    }

    fun setMessage(message:Message) = GlobalScope.launch(Dispatchers.IO) {
        if(_socketStatus.value){
            _messages.value = _messages.value.toMutableList().apply {
                this.add(message)
            }.toImmutableList()
        }

    }
}