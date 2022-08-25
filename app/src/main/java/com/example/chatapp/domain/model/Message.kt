package com.example.chatapp.domain.model

data class Message(
    val text: String,
    val username: String,
    val formattedTime: String
)
