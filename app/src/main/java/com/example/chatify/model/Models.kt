package com.example.chatify.model

data class ChatMessage(
    val message: String,
    val isUser: Boolean // true if user message, false if AI response
)