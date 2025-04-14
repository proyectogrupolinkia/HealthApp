package com.tusalud.healthapp.presentation.reset

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tusalud.healthapp.presentation.login.LoginViewModel

@Composable
fun PasswordResetScreen(viewModel: LoginViewModel, navController: NavController) {
    var correo by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        Text("Recuperar Contraseña", style = MaterialTheme.typography.titleLarge)
        TextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo") })

        Button(onClick = {
            viewModel.resetPassword(correo) {
                navController.popBackStack()
            }
        }) {
            Text("Enviar Correo")
        }

        viewModel.error?.let {
            Text(text = it, color = Color.Red)
        }
    }
}