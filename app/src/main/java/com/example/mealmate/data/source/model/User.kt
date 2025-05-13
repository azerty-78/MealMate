package com.example.mealmate.data.source.model

data class User(
    val id : String? = null,
    val email: String = "",
    val password: String = "",
    val username: String? = null,
    val profilePictureUrl: String? = null,
)