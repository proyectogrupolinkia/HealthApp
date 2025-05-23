package com.tusalud.healthapp.presentation.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tusalud.healthapp.presentation.login.AnimatedGradientBackground
import com.tusalud.healthapp.presentation.login.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var edadValida by remember { mutableStateOf(false) }
    var pesoValido by remember { mutableStateOf(false) }

    val isCorreoValido = viewModel.isEmailValid(correo)
    val isPasswordValido = viewModel.isPasswordValid(password)

    // Funcion para validar edad: solo numeros enteros entre 1 y 120
    fun validarEdad(input: String): Boolean {
        val regex = Regex("^\\d{1,3}$")
        if (!regex.matches(input)) return false
        val valor = input.toIntOrNull() ?: return false
        return valor in 1..120
    }

    // Funcion para validar peso: numeros con punto decimal opcional, maximo 2 decimales, entre 20 y 500
    fun validarPeso(input: String): Boolean {
        val regex = Regex("^\\d{1,3}(\\.\\d{0,2})?$")
        if (!regex.matches(input)) return false
        val valor = input.toFloatOrNull() ?: return false
        return valor in 20f..500f
    }

    // Controla la entrada de edad: solo digitos, actualiza estado y validez
    fun onEdadChange(newValue: String) {
        if (newValue.isEmpty()) {
            edad = ""
            edadValida = false
        } else if (validarEdad(newValue)) {
            edad = newValue
            edadValida = true
        } else if (newValue.matches(Regex("^\\d{0,3}$"))) {
            edad = newValue
            edadValida = validarEdad(newValue)
        }
    }

    // Controla la entrada de peso: permite numeros con punto decimal, maximo 2 decimales
    fun onPesoChange(newValue: String) {
        if (newValue.isEmpty()) {
            peso = ""
            pesoValido = false
        } else if (validarPeso(newValue)) {
            peso = newValue
            pesoValido = true
        } else if (newValue.matches(Regex("^\\d{0,3}(\\.\\d{0,2})?$"))) {
            peso = newValue
            pesoValido = validarPeso(newValue)
        }
    }

    // Validacion general del formulario
    val isFormValid = nombre.isNotBlank() &&
            isCorreoValido &&
            edadValida &&
            pesoValido &&
            viewModel.isAlturaValid(altura) &&
            isPasswordValido

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
                        .verticalScroll(scrollState) // ✅ scroll activado
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre") },
                            singleLine = true,
                            isError = nombre.isBlank(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = correo,
                            onValueChange = { correo = it },
                            label = { Text("Correo") },
                            singleLine = true,
                            isError = correo.isNotBlank() && !isCorreoValido,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (!isCorreoValido && correo.isNotEmpty()) {
                            Text("Correo no válido", color = Color.Red, fontSize = 12.sp)
                        }

                        OutlinedTextField(
                            value = edad,
                            onValueChange = { onEdadChange(it) },
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
                            onValueChange = { onPesoChange(it) },
                            label = { Text("Peso (kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            isError = peso.isNotBlank() && !pesoValido,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (!pesoValido && peso.isNotEmpty()) {
                            Text("Peso no válido (20 a 500 kg, max 2 decimales)", color = Color.Red, fontSize = 12.sp)
                        }

                        OutlinedTextField(
                            value = altura,
                            onValueChange = { altura = it },
                            label = { Text("Altura (cm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            isError = altura.isNotBlank() && !viewModel.isAlturaValid(altura),
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (!viewModel.isAlturaValid(altura) && altura.isNotEmpty()) {
                            Text("Altura no válida (50 a 250 cms)", color = Color.Red, fontSize = 12.sp)
                        }

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Contraseña") },
                            singleLine = true,
                            isError = password.isNotBlank() && !isPasswordValido,
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
                        if (!isPasswordValido && password.isNotEmpty()) {
                            Text(
                                "La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un símbolo",
                                color = Color.Red,
                                fontSize = 12.sp
                            )
                        }

                        Button(
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
                            enabled = isFormValid,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C6A7)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Registrarse", color = Color.White)
                        }

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

//