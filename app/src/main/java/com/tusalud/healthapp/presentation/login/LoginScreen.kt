package com.tusalud.healthapp.presentation.login

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = viewModel.email, onValueChange = { viewModel.email = it }, label = { Text("Correo") })
        TextField(value = viewModel.password, onValueChange = { viewModel.password = it }, label = { Text("Contraseña") })

        Button(onClick = {
            viewModel.login {
                navController.navigate("main")
            }
        }) {
            Text("Iniciar Sesión")
        }

        TextButton(onClick = { navController.navigate("register") }) {
            Text("¿No tienes cuenta? Regístrate")
        }

        TextButton(onClick = { navController.navigate("reset") }) {
            Text("¿Olvidaste tu contraseña?")
        }

        viewModel.error?.let {
            Text(text = it, color = Color.Red)
        }
    }
}