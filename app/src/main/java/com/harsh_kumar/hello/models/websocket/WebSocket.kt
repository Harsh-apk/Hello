package com.harsh_kumar.hello.models.websocket

import com.harsh_kumar.hello.models.types.Message
import com.harsh_kumar.hello.viewmodels.MainViewModel
import kotlinx.serialization.json.Json
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class MyWebSocket(private val viewModel: MainViewModel):WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        viewModel.setSocketStatus(true)
        println("yes"+"\n"+"yes"+"\n"+"yes"+"\n"+"yes"+"\n"+"yes"+"\n"+"yes"+"\n")

    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        //convert string message into object of Message Class

        val message : Message = Json.decodeFromString(text)
        viewModel.setMessage(message)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        viewModel.setSocketStatus(false)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        throw t
        //println(t.message)
    }

}