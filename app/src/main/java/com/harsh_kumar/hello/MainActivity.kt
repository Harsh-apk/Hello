package com.harsh_kumar.hello

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.harsh_kumar.hello.models.constants.GetWebSocketUrl
import com.harsh_kumar.hello.models.constants.LOGIN_KEY
import com.harsh_kumar.hello.models.constants.SHARED_PREFERENCES_FILENAME
import com.harsh_kumar.hello.models.websocket.MyWebSocket
import com.harsh_kumar.hello.viewmodels.MainViewModel
import com.harsh_kumar.hello.views.MainScreen
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocketListener

class MainActivity : ComponentActivity() {
    private lateinit var webSocketListener: WebSocketListener
    private lateinit var viewModel: MainViewModel
    private val okHttpClient = OkHttpClient()
    private lateinit var webSocket: okhttp3.WebSocket

    private var name :String? = null


    override fun onResume() {
        super.onResume()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        webSocketListener = MyWebSocket(viewModel)
        name = readSharedPreferences()
        if(name == null){
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)

        }else{
            //makeWebSocket
            val request = Request.Builder().url(GetWebSocketUrl(name!!)).build()
            webSocket = okHttpClient.newWebSocket(request,webSocketListener)
            //webSocket.send("{\"receiverName\":\"lola\",\"message\":\"Hii\"}")
        }
        enableEdgeToEdge()
        setContent {
            MainScreen(messages = viewModel.messages,this,webSocket,viewModel)
        }

    }
    //make it route as chatScreen or login screen
    fun readSharedPreferences():String?{
        val reader = getSharedPreferences(SHARED_PREFERENCES_FILENAME, MODE_PRIVATE)
        return reader.getString(LOGIN_KEY,null)
    }


}