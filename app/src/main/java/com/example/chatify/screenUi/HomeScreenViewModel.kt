package com.example.chatify.screenUi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatify.constants.API_KEY
import com.example.chatify.model.ChatMessage
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
//import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val generativeModel: GenerativeModel
) : ViewModel() {

    // State to store chat messages
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages = _chatMessages.asStateFlow()

    // Function to send user message and get AI response
    fun sendMessage(userMessage: String) {

        viewModelScope.launch {
            // Update chat state with user message
            val updatedMessages = _chatMessages.value + ChatMessage(userMessage, true)
            _chatMessages.value = updatedMessages

            try {
                // Get AI response
                val response = generativeModel.generateContent(userMessage ?: "hi")
                val aiReply = response.text ?: "Sorry, I couldn't understand that."

                // Update chat state with AI response
                _chatMessages.value += ChatMessage(aiReply, false)

            } catch (e: Exception) {
                Log.e("ChatApp", "Error: ${e.message}", e)
                _chatMessages.value += ChatMessage("Error: ${e.message}", false)
            }
        }
    }


    /*
    fun sendMessage(message: String) {

        viewModelScope.launch {

            /*
            try {
                val chat = generativeModel.startChat()
                val response = chat.sendMessage(message)

                Log.d("AI_RESPONSE", "Response: ${response.text.toString()}")

            } catch (e: Exception) {

                Log.d("AI_RESPONSE", "Error : ${e.printStackTrace()}")

            }


             */

        }
    }

     */
}