package com.example.chatify.util

sealed class ResultState<out T> {

    data class Success<out T>(val data: T) : ResultState<T>()
    data class Failure(val message: String) : ResultState<Nothing>()
    object Loading : ResultState<Nothing>()
    object Idle : ResultState<Nothing>()

}