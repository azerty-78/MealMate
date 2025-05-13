package com.example.mealmate.util

sealed class Listener<T>(
    val data: T? = null,
    val e: Throwable? = null,
    val message: String? = null,
) {
    object Loading : Listener<Nothing>()
    class Success<T>(data: T?): Listener<T>(data =data)
    class Error<T>(e: Throwable?, message: String?): Listener<T>(e = e, message = message)
}