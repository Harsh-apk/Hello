package com.harsh_kumar.hello.models.types

import kotlinx.serialization.Serializable

@Serializable
data class Message (
    val message:String,
    val senderName:String?,
    val receiverName:String?,
    val pending:Boolean
)

//Websocket Backend Message Type
//SenderName   User   `json:"senderName"`
//ReceiverName User   `json:"receiverName"`
//Message      string `json:"message"`
//Pending      bool   `json:"pending"`