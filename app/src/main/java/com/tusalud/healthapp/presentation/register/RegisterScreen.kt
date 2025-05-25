
package com.tusalud.healthapp.presentation.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tusalud.healthapp.presentation.login.AnimatedGradientBackground
import com.tusalud.healthapp.presentation.login.LoginViewModel

/**
 * Pantalla de registro de usuario.
 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    // Asociaciones de variables del ViewModel a los campos de entrada
    val nombre by remember { viewModel::nombre }
    val correo by remember { viewModel::email }
    val edad by remember { viewModel::edad }
    val peso by remember { viewModel::peso }
    val altura by remember { viewModel::altura }
    val password by remember { viewModel::password }

    val edadValida = viewModel.edadValida
    val pesoValido = viewModel.pesoValido
    val alturaValida = viewModel.alturaValida
    val isFormValid = viewModel.isRegisterFormValid

    // Visibilidad de contraseña y scroll

    var passwordVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        AnimatedGradientBackground {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(scrollState)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { viewModel.nombre = it },
                            label = { Text("Nombre") },
                            singleLine = true,
                            isError = nombre.isBlank(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = correo,
                            onValueChange = { viewModel.email = it },
                            label = { Text("Correo") },
                            singleLine = true,
                            isError = correo.isNotBlank() && !viewModel.isEmailValid(correo),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (!viewModel.isEmailValid(correo) && correo.isNotEmpty()) {
                            Text("Correo no válido", color = Color.Red, fontSize = 12.sp)
                        }

                        OutlinedTextField(
                            value = edad,
                            onValueChange = { viewModel.edad = it },
                            label = { Text("Edad") },
                            singleLine = true,
                            isError = edad.isNotBlank() && !edadValida,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (!edadValida && edad.isNotEmpty()) {
                            Text("Edad no válida (1 a 120)", color = Color.Red, fontSize = 12.sp)
                        }

                        OutlinedTextField(
                            value = peso,
                            onValueChange = { viewModel.peso = it },
                            label = { Text("Peso (kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            isError = peso.isNotBlank() && !pesoValido,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (!pesoValido && peso.isNotEmpty()) {
                            Text("Peso no válido (1 a 500 kg)", color = Color.Red, fontSize = 12.sp)
                        }
                        //Altura
                        OutlinedTextField(
                            value = altura,
                            onValueChange = { viewModel.altura = it },
                            label = { Text("Altura (cm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            isError = altura.isNotBlank() && !alturaValida,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (!alturaValida && altura.isNotEmpty()) {
                            Text("Altura no válida (50 a 250 cms)", color = Color.Red, fontSize = 12.sp)
                        }

                        OutlinedTextField(
                            value = password,
                            onValueChange = { viewModel.password = it },
                            label = { Text("Contraseña") },
                            singleLine = true,
                            isError = password.isNotBlank() && !viewModel.isPasswordValid(password),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                                val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(imageVector = icon, contentDescription = description)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (!viewModel.isPasswordValid(password) && password.isNotEmpty()) {
                            Text(
                                "La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un símbolo",
                                color = Color.Red,
                                fontSize = 12.sp
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.register {
                                    navController.navigate("main") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                }
                            },
                            enabled = isFormValid,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C6A7)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Registrarse", color = Color.White)
                        }
                        //errores

                        viewModel.error?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
