package com.harsh_kumar.hello.views

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.harsh_kumar.hello.models.types.Message
import com.harsh_kumar.hello.viewmodels.MainViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.WebSocket

@Composable
fun MainScreen(messages :State<List<Message>>,context:Context,webSocket: WebSocket,viewModel: MainViewModel) {
    val viewState by messages
    var message by remember {
        mutableStateOf("")
    }
    var receiverName by remember {
        mutableStateOf("")
    }
   Column(modifier = Modifier.fillMaxSize()) {
       Box(modifier = Modifier
           .fillMaxWidth()

           .background(color = Color.Black)){
           Text(text ="Hello Chat", color = Color.Yellow, modifier = Modifier
               .fillMaxWidth()
               .padding(10.dp), textAlign = TextAlign.Center, fontStyle = FontStyle.Italic, fontSize = TextUnit(40f,TextUnitType.Sp) )
       }
       Box(modifier = Modifier.fillMaxWidth().padding(10.dp)){
           Row(modifier = Modifier
               .fillMaxWidth()
               .background(Color.White), verticalAlignment = Alignment.CenterVertically) {
               TextField(
                   value = receiverName,
                   onValueChange = { text: String -> receiverName = text },
                   placeholder = {
                       Text(
                           text = "To"
                       )
                   },
                   singleLine = true,
                   colors = TextFieldDefaults.colors(
                       focusedContainerColor = Color.White,
                       unfocusedContainerColor = Color.White,
                       focusedIndicatorColor = Color.Yellow,
                       unfocusedIndicatorColor = Color.Yellow,
                       focusedTextColor = Color.Black,
                       focusedTrailingIconColor = Color.Black
                   ),
                   modifier = Modifier
                       .border(border = BorderStroke(2.dp,Color.Black))
                       .clip(
                           RoundedCornerShape(40.dp)
                       )
                       .fillMaxWidth(0.85f)
                       .padding(5.dp)
               )
               IconButton(onClick = {
                   Toast.makeText(context,"Messages will be delivered to "+receiverName,Toast.LENGTH_SHORT).show()
               }) {
                   Icon(Icons.Filled.CheckCircle, contentDescription =null, modifier = Modifier
                       .padding(
                           PaddingValues(start = 10.dp)
                       )
                       .size(50.dp) )
               }
           }
       }
       LazyColumn(modifier = Modifier
           .fillMaxWidth()
           .fillMaxHeight(0.91f)
           .background(color = Color.White)){
            items(viewState){
                if(it.senderName!=null){
//                    Text(text = "From: "+it.senderName+"\n"+it.message, modifier = Modifier.background(
//                        Color.Yellow).fillMaxWidth(), color = Color.Black, textAlign = TextAlign.Start)
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.Start){
                        Text(text="From: "+it.senderName, fontWeight = FontWeight.Bold, fontSize = TextUnit(20f,TextUnitType.Sp))
                        Text(text=it.message,fontSize = TextUnit(18f,TextUnitType.Sp))
                    }
                }else{
                    Column(modifier = Modifier.padding(20.dp).fillMaxWidth(), horizontalAlignment = Alignment.End){
                        Text(text="To: "+it.receiverName, fontWeight = FontWeight.Bold, fontSize = TextUnit(20f,TextUnitType.Sp))
                        Text(text=it.message,fontSize = TextUnit(18f,TextUnitType.Sp))
                    }
                }

            }


       }

           Row(modifier = Modifier
               .fillMaxSize()
               .background(Color.White), verticalAlignment = Alignment.CenterVertically) {
               TextField(
                   value = message,
                   onValueChange = { text: String -> message = text },
                   placeholder = {
                       Text(
                           text = "Message"
                       )
                   },
                   singleLine = true,
                   colors = TextFieldDefaults.colors(
                       focusedContainerColor = Color.White,
                       unfocusedContainerColor = Color.White,
                       focusedIndicatorColor = Color.Yellow,
                       unfocusedIndicatorColor = Color.Yellow,
                       focusedTextColor = Color.Black,
                       focusedTrailingIconColor = Color.Black
                   ),
                   modifier = Modifier
                       .shadow(30.dp)
                       .clip(
                           RoundedCornerShape(40.dp)
                       )
                       .fillMaxWidth(0.85f)
                       .padding(5.dp)
               )
               IconButton(onClick = {
                   if(message.isEmpty()){
                       Toast.makeText(context,"Can't send empty messages ",Toast.LENGTH_SHORT).show()
                   }else if(receiverName.isEmpty()){
                       Toast.makeText(context,"Enter your friend's name ",Toast.LENGTH_SHORT).show()
                   }else{
                       val data = Message(message=message, senderName =null, receiverName=receiverName,pending=false )
                       val msg = Json.encodeToString(data)
                       message=""
                       webSocket.send(msg)
                       viewModel.setMessage(data)
                   }

               }) {
                   Icon(Icons.Filled.Send, contentDescription =null, modifier = Modifier
                       .padding(
                           PaddingValues(start = 10.dp)
                       )
                       .size(50.dp) )
               }
           }
       }
}
