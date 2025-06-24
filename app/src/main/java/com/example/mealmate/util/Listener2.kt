package com.example.mealmate.util

sealed class Listener2<T>(
    val data: T? = null,
    val e: Throwable? = null,
    val message: String? = null,
) {
    class Loading<T> : Listener2<T>()
    class Success<T>(data: T) : Listener2<T>(data = data)
    class Error<T>(e: Throwable, message: String?) : Listener2<T>(e = e, message = message)
}