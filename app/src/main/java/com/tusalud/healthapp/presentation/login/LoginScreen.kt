package com.tusalud.healthapp.presentation.login

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

//@Composable
//fun AnimatedGradientBackground(content: @Composable BoxScope.() -> Unit) {
//    val infiniteTransition = rememberInfiniteTransition(label = "gradient")
//    val offset by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 1000f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 4000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        ), label = "gradientShift"
//    )
//
//    val gradient = Brush.linearGradient(
//        colors = listOf(Color(0xFFFFFFFF), Color(0xFF6BC800)),
//        start = Offset(0f, offset),
//        end = Offset(offset, 0f)
//    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(brush = gradient),
//        content = content
//    )
//}

@Composable
fun AnimatedGradientBackground(content: @Composable BoxScope.() -> Unit) {
    val progress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(15000, easing = LinearEasing),
            RepeatMode.Reverse
        )
    )
    Box(Modifier.fillMaxSize().background(
        Brush.linearGradient(
            listOf(Color.White, Color(0xFF6BC800), Color.White),
            start = Offset(progress * 1500f, 0f),
            end = Offset(0f, progress * 1500f)
        )
    ), content = content)
}

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedGradientBackground {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center // Centra el contenido
            ) {
                // Spinner grande arriba del título
                if (viewModel.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(bottom = 24.dp),
                        //color = Color(0xFF00C6A7),
                        color = Color(0xFF006400),
                        strokeWidth = 4.dp
                    )
                }

                Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(24.dp),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .clip(RoundedCornerShape(16.dp)) // Bordes redondeados
                        .padding(16.dp), // Padding interno
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(48.dp))

                    // Spinner grande arriba del título
//                if (viewModel.loading) {
//                    CircularProgressIndicator(
//                        modifier = Modifier
//                            .size(48.dp)
//                            .padding(bottom = 24.dp),
//                        color = Color(0xFF00C6A7),
//                        strokeWidth = 4.dp
//                    )
//                }

                    Text(
//                    modifier = Modifier.padding(bottom = 32.dp)
                        "Bienvenido",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF006400),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    OutlinedTextField(
                        value = viewModel.email,
                        onValueChange = { viewModel.email = it },
                        label = { Text("Correo electrónico") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Blue, //Azul al estar activo
                            unfocusedBorderColor = Color.Gray, //Gris al no estar activo
                            focusedTextColor = Color(0xFF006400), //Texto en VERDE oSCURO
                        ),
                        enabled = !viewModel.loading
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    var passwordVisible by remember { mutableStateOf(false) }

                    OutlinedTextField(
                        value = viewModel.password,
                        onValueChange = { viewModel.password = it },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Blue, //Azul al estar activo
                            unfocusedBorderColor = Color.Gray, //Gris al no estar activo
                            focusedTextColor = Color(0xFF006400), //Texto en VERDE oSCURO
                        ),
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
//                            CircularProgressIndicator(
//                                modifier = Modifier.size(20.dp),
//                                color = Color.White,
//                                strokeWidth = 2.dp
//                            )
                        } else {
                            Text("Iniciar sesión")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(onClick = { navController.navigate("register") }) {
                        Text("¿No tienes cuenta? Regístrate",
                            fontSize = 16.sp,
                            color = Color.Blue,
                        )

                    }

                    TextButton(onClick = { navController.navigate("reset") }) {
                        Text("¿Olvidaste tu contraseña?",
                            fontSize = 16.sp,
                            color = Color.Blue,
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    viewModel.error?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}