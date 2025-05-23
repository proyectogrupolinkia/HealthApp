package com.tusalud.healthapp.presentation.reset

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tusalud.healthapp.presentation.login.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordResetScreen(navController: NavHostController, viewModel: LoginViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
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
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Recuperar Contraseña",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        OutlinedTextField(
                            value = viewModel.email,
                            onValueChange = { viewModel.email = it },
                            label = { Text("Correo electrónico") },
                            isError = viewModel.email.isNotBlank() && !viewModel.isEmailValid(viewModel.email),
                            keyboardOptions = KeyboardOptions.Default,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !viewModel.loading
                        )

                        if (!viewModel.isEmailValid(viewModel.email) && viewModel.email.isNotEmpty()) {
                            Text("Correo no válido", color = Color.Red, fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                viewModel.resetPassword(viewModel.email) {
                                    navController.popBackStack()
                                }
                            },
                            enabled = viewModel.isEmailValid(viewModel.email),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            Text("Enviar Correo")
                        }
                        Text(
                            text = "⚠ La nueva contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un símbolo.",
                            color = Color.Red,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )


                        viewModel.error?.let {
                            Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedGradientBackground(content: @Composable BoxScope.() -> Unit) {
    val progress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(15000, easing = LinearEasing),
            RepeatMode.Reverse
        ), label = ""
    )
    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color.White, Color(0xFF6BC800), Color.White),
                    start = Offset(progress * 1500f, 0f),
                    end = Offset(0f, progress * 1500f)
                )
            ), content = content
    )
}