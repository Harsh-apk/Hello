package com.harsh_kumar.hello.models.constants


val SHARED_PREFERENCES_FILENAME: String = "LoginDetails"
val LOGIN_KEY: String = "Login"
val NAME_CANNOT_BE_EMPTY: String= "Can't login with an empty name!"
val WEBSOCKET_URL: String = "ws://172.22.94.53:3000/ws/"  //Standard link is: ws://172.22.94.53:3000/ws/shani


//Get Standard WobSocket backend URL
fun GetWebSocketUrl(name:String):String{
    return WEBSOCKET_URL+name
}