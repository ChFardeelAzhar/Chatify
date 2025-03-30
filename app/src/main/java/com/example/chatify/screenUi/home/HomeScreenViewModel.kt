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


        /*
        viewModelScope.launch {


            try {
                val chat = generativeModel.startChat()
                val response = chat.sendMessage(userMessage)
                Log.d("AI_RESPONSE", "Response: ${response.text.toString()}")

            } catch (e: Exception) {
                Log.d("AI_RESPONSE", "Error : ${e.message.toString()}")

            }


        }


         */

        _chatState.value = ResultState.Loading
        Log.d("API_KEY", "Using API Key: ${generativeModel.apiKey}")


        // Ai Generated
        viewModelScope.launch {


            try {

                /*
                // Update chat state with user message
                val updatedMessages = _chatMessages.value + ChatMessage(userMessage, true)
                _chatMessages.value = updatedMessages

                // Get AI response
                val response = generativeModel.generateContent(userMessage ?: "hi")
                val aiReply = response.text ?: "Sorry, I couldn't understand that."

                // Update chat state with AI response
                _chatMessages.value += ChatMessage(aiReply, false)


                 */

                val chat = generativeModel.startChat(
//                    history = _chatMessages.value.map {
//                        content(
//
//                            ) {
//
//                        }
//                    }.toList()
                )
                _chatMessages.value += ChatMessage(userMessage, true)

                val chatResponse = chat.sendMessage(userMessage)
                _chatMessages.value += ChatMessage(
                    chatResponse.text ?: "Sorry, I couldn't understand that.", false
                )

            } catch (e: Exception) {
                Log.e("ChatApp", "Error: ${e.message}", e)
                _chatMessages.value += ChatMessage("Error: ${e.message}", false)
            }


        }


    }


    /*
    fun sendMessage(message: String) {

        viewModelScope.launch {


            try {
                val chat = generativeModel.startChat()
                val response = chat.sendMessage(message)

                Log.d("AI_RESPONSE", "Response: ${response.text.toString()}")

            } catch (e: Exception) {

                Log.d("AI_RESPONSE", "Error : ${e.printStackTrace()}")

            }




        }
    }


     */

}