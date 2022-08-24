package com.example.room

import com.example.data.MessageDataSource
import com.example.data.model.Message
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController(
    private val messageDataSource: MessageDataSource
) {
    private val members = ConcurrentHashMap<String, Member>()

    fun onJoin(
        username: String,
        sessionId: String,
        socket: WebSocketSession
    ) {
        if (members.containsKey(username)) throw MemberAlreadyExistsException() else members[username] = Member(
            username,
            sessionId,
            socket
        )

    }

    suspend fun sendMessage(senderUsername: String, message: String) {
        members.values.forEach {
            val messageEntity = Message(
                message,
                senderUsername,
                System.currentTimeMillis()
            )
            messageDataSource.insertMessage(messageEntity)

            val parsedMessage = Json.encodeToString(messageEntity)
            it.socket.send(Frame.Text(parsedMessage))
        }
    }

    suspend fun getAllMesssages(): List<Message> = messageDataSource.getAllMessages()

    suspend fun tryDisconnect(username: String){
        members[username]?.socket?.close()
        if (members.containsKey(username)){
            members.remove(username)
        }
    }
}