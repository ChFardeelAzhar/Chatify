package com.example.chatify.screenUi.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatify.model.ChatMessage
import com.example.chatify.util.ResultState
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
//import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val generativeModel: GenerativeModel
) : ViewModel() {


    private var _chatState = MutableStateFlow<ResultState<String>>(ResultState.Idle)
    val chatState: StateFlow<ResultState<String>> = _chatState.asStateFlow()

    // State to store chat messages
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages = _chatMessages.asStateFlow()

    // Function to send user message and get AI response
    fun sendMessage(userMessage: String) {

        _chatState.value = ResultState.Loading
        Log.d("API_KEY", "Using API Key: ${generativeModel.apiKey}")

        // Ai Generated
        viewModelScope.launch {
            try {

                val chat = generativeModel.startChat(
                    history = _chatMessages.value.map {
                        content(role = if (it.isUser) "user" else "model") {  // Assign role
                            text(it.message)
                        }
                    }

                )

                _chatMessages.value += ChatMessage(userMessage, true)
                _chatMessages.value += ChatMessage("Typing...", false)

                val chatResponse = chat.sendMessage(userMessage)
                _chatMessages.value = _chatMessages.value.dropLast(1)

                _chatMessages.value += ChatMessage(
                    chatResponse.text ?: "Sorry, I couldn't understand that.", false
                )


            } catch (e: Exception) {
                Log.e("ChatApp", "Error: ${e.message}", e)
                                _chatMessages.value = _chatMessages.value.dropLast(1)
                _chatMessages.value += ChatMessage("Error: ${e.message}", false)


                _chatState.value = ResultState.Idle
            }


        }


    }

}