package com.harsh_kumar.hello.views

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.harsh_kumar.hello.models.constants.NAME_CANNOT_BE_EMPTY


@Composable
//@Preview(showBackground = true, showSystemUi = true)
fun LoginScreen(context: Context, editSharedPreference: (value:String)->Unit){
    var name by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {
        Box( modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4F), contentAlignment = Alignment.Center){
                Text(
                    text = "Hello",
                    color = Color.Yellow,
                    fontSize = TextUnit(50F, TextUnitType.Sp),
                    fontFamily = FontFamily.SansSerif,
                    fontStyle = FontStyle.Italic
                )
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 150.dp))
            .background(Color.White), contentAlignment = Alignment.Center){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {


            TextField(
                value = name,
                onValueChange = { text: String -> name = text },
                placeholder = {
                    Text(
                        text = "Enter Name"
                    )
                },
                trailingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                singleLine = true,
                colors = TextFieldDefaults.colors(focusedContainerColor = Color.White,unfocusedContainerColor = Color.White, focusedIndicatorColor = Color.Yellow, unfocusedIndicatorColor = Color.Yellow, focusedTextColor = Color.Black, focusedTrailingIconColor = Color.Black),
                modifier = Modifier
                    .shadow(30.dp)
                    .clip(
                        RoundedCornerShape(10.dp)
                    )
            )
            Button(onClick = {
                             if(name.isNotEmpty()){
                                 editSharedPreference(name)

                                 //initiate Websocket Connection
                             }else{

                                 Toast.makeText(context, NAME_CANNOT_BE_EMPTY,Toast.LENGTH_SHORT).show()
                             }
            }, modifier = Modifier
                .padding(20.dp)
                .shadow(elevation = 30.dp)
                .clip(
                    RoundedCornerShape(20.dp)
                )
            , colors= ButtonDefaults.buttonColors(containerColor = Color.Yellow)) {
                Text(text = "Login", color = Color.Black)
            }
        }
        }

    }
}