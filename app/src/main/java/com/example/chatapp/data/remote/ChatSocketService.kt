package com.example.chatapp.data.remote

import com.example.chatapp.domain.model.Message
import com.example.chatapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface ChatSocketService {
    suspend fun initSession(
        username: String
    ): Resource<Unit>

    suspend fun sendMessage(message: String)

    fun observeMessages(): Flow<Message>

    suspend fun closeSession()

    sealed class Endpoints(val url: String) {
        object ChatSocket: Endpoints("$BASE_URL/chat-socket")
    }

    companion object {
        const val BASE_URL = "ws://192.168.43.64:8082"
    }
}