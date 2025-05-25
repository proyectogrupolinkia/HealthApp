//Punto de entrada de la interfaz de usuario
package com.tusalud.healthapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.tusalud.healthapp.presentation.navigation.AppNavigation
import com.tusalud.healthapp.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

//@AndroidEntryPoint para habilitar la inyección de dependencias

@AndroidEntryPoint

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Habilita el diseño edge-to-edge
        enableEdgeToEdge()

        setContent {
            AppTheme {

                val navController = rememberNavController()
                // Inicia la navegación principal de la aplicación
                AppNavigation(navController = navController)
            }
        }
    }
}//
