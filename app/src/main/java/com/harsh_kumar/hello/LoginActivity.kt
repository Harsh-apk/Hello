package com.harsh_kumar.hello

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ContentInfoCompat.Flags
import com.harsh_kumar.hello.models.constants.LOGIN_KEY
import com.harsh_kumar.hello.models.constants.SHARED_PREFERENCES_FILENAME
import com.harsh_kumar.hello.ui.theme.HelloTheme
import com.harsh_kumar.hello.views.LoginScreen

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloTheme {
                LoginScreen(context = this,::editSharedPreferences)

            }
        }
    }

    fun editSharedPreferences(value: String){
        val editor = getSharedPreferences(SHARED_PREFERENCES_FILENAME,MODE_PRIVATE).edit()
        editor.putString(LOGIN_KEY,value)
        editor.apply()
        val intent = Intent(this,MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        //this.finish()

    }

}
