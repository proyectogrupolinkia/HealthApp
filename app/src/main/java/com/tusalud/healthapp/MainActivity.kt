package com.tusalud.healthapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.FirebaseApp
import com.tusalud.healthapp.ui.theme.HealthappTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //inicializar firebase
        FirebaseApp.initializeApp(this)

        setContent {
            HealthappTheme {
                //LoginScreen() //mostrar la pantalla de login

                PlaceholderScreen() //pantalla temporal
            }
        }
    }
}
//pantalla temporal
@Composable
fun PlaceholderScreen() {
    Text(text = "Pantalla en construcción...")
}
