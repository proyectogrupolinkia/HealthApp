package com.tusalud.healthapp.presentation.reset

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.tusalud.healthapp.presentation.login.AnimatedGradientBackground
import com.tusalud.healthapp.presentation.login.LoginViewModel

@Composable
fun PasswordResetScreen(navController: NavHostController, viewModel: LoginViewModel = hiltViewModel()) {
    var correo by remember { mutableStateOf("") }
    AnimatedGradientBackground {
        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
        ) {
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center // Centra el contenido
                        ){
            Column(Modifier.padding(16.dp)) {
                Text("Recuperar Contraseña", style = MaterialTheme.typography.titleLarge)
                TextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo") })

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
        }
    }

    @Composable
    fun AnimatedGradientBackground(content: @Composable BoxScope.() -> Unit) {
        val progress by animateFloatAsState(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                tween(15000, easing = LinearEasing),
                RepeatMode.Reverse
            )
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
}