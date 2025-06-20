
package com.tusalud.healthapp.presentation.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController


// Fondo animado con degradado

@Composable
fun AnimatedGradientBackground(content: @Composable BoxScope.() -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "gradientShift"
    )

    val gradient = Brush.linearGradient(
        colors = listOf(Color(0xFFFFFFFF), Color(0xFF6BC800)),
        start = Offset(0f, offset),
        end = Offset(offset, 0f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient),
        content = content
    )
}

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    // Controla si se muestra la animación inicial

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val scrollState = rememberScrollState()
    // Indicador de carga mientras se realiza login

    AnimatedGradientBackground {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                // Indicador de carga mientras se realiza login

                if (viewModel.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(bottom = 24.dp),
                        color = Color(0xFF00C6A7),
                        strokeWidth = 4.dp
                    )
                }
                // Título principal

                Text(
                    "Bienvenido",
                    fontSize = 32.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                OutlinedTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !viewModel.loading
                )
                if (!viewModel.isEmailValid(viewModel.email) && viewModel.email.isNotEmpty()) {
                    Text("Correo no válido", color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                var passwordVisible by remember { mutableStateOf(false) }

                OutlinedTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    enabled = !viewModel.loading,
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Default.VisibilityOff
                        else Icons.Default.Visibility

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = image,
                                contentDescription = if (passwordVisible) "Ocultar" else "Mostrar"
                            )
                        }
                    }
                )
                if (!viewModel.isPasswordValid(viewModel.password) && viewModel.password.isNotEmpty()) {
                    Text(
                        "La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un símbolo",
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        viewModel.login {
                            navController.navigate("main") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    },
                    enabled = viewModel.isFormValid && !viewModel.loading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C6A7)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    if (viewModel.loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Iniciar sesión")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                // Navegación a registro

                TextButton(onClick = { navController.navigate("register") }) {
                    Text("¿No tienes cuenta? Regístrate", fontSize = 16.sp, color = Color.Black)
                }
                // Navegación a restablecer contraseña

                TextButton(onClick = { navController.navigate("reset") }) {
                    Text("¿Olvidaste tu contraseña?", fontSize = 16.sp, color = Color.Black)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar mensaje de error en caso de fallo en login

                viewModel.error?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
                }
            }
        }
    }
}
