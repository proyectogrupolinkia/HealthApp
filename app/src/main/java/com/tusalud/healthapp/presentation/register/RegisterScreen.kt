package com.tusalud.healthapp.presentation.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.tusalud.healthapp.presentation.login.LoginViewModel



@Composable
fun RegisterScreen(navController: NavHostController, viewModel: LoginViewModel = hiltViewModel()) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium)

                OutlinedTextField(
                    singleLine = true,
                    isError = nombre.isBlank(),
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    singleLine = true,
                    isError = correo.isBlank(),
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    singleLine = true,
                    isError = edad.toIntOrNull() == null,
                    value = edad,
                    onValueChange = { edad = it },
                    label = { Text("Edad") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    singleLine = true,
                    isError = peso.toFloatOrNull() == null,
                    value = peso,
                    onValueChange = { peso = it },
                    label = { Text("Peso (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    singleLine = true,
                    isError = altura.toFloatOrNull() == null,
                    value = altura,
                    onValueChange = { altura = it },
                    label = { Text("Altura (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                var passwordVisible by remember { mutableStateOf(false) }

                OutlinedTextField(
                    singleLine = true,
                    isError = password.length < 6,
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = if (passwordVisible) "Ocultar" else "Mostrar")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                val isFormValid = nombre.isNotBlank() &&
                        correo.isNotBlank() &&
                        edad.toIntOrNull() != null &&
                        peso.toFloatOrNull() != null &&
                        altura.toFloatOrNull() != null &&
                        password.length >= 6

                Button(
                    enabled = isFormValid,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C6A7)),
                    onClick = {
                        viewModel.register(
                            nombre,
                            correo,
                            edad.toIntOrNull() ?: 0,
                            peso.toFloatOrNull() ?: 0f,
                            altura.toFloatOrNull() ?: 0f,
                            password
                        ) {
                            navController.navigate("main") {
                                popUpTo("register") { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrarse", color = Color.White)
                }

                viewModel.error?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}