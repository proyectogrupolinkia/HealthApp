package com.tusalud.healthapp.presentation.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
fun RegisterScreen(viewModel: LoginViewModel, navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        TextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo") })
        TextField(value = edad, onValueChange = { edad = it }, label = { Text("Edad") })
        TextField(value = peso, onValueChange = { peso = it }, label = { Text("Peso") })
        TextField(value = altura, onValueChange = { altura = it }, label = { Text("Altura") })
        TextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") })

        Button(onClick = {
            viewModel.register(
                nombre,
                correo,
                edad.toIntOrNull() ?: 0,
                peso.toFloatOrNull() ?: 0f,
                altura.toFloatOrNull() ?: 0f,
                password
            ) {
                navController.navigate("main")
            }
        }) {
            Text("Registrarse")
        }

        viewModel.error?.let {
            Text(text = it, color = Color.Red)
        }
    }
}