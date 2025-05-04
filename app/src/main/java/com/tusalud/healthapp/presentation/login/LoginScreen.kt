package com.tusalud.healthapp.presentation.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun LoginScreen(navController: NavHostController, viewModel: LoginViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Espacio superior opcional
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            "Bienvenido",
            fontSize = 32.sp,
            color = Color(0xFF00C6A7),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.login {
                    navController.navigate("main")
                }
            },
            enabled = viewModel.isFormValid,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C6A7)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Iniciar Sesión", fontSize = 18.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate("register") }) {
            Text("¿No tienes cuenta? Regístrate", fontSize = 16.sp, color = Color(0xFF00C6A7))
        }

        TextButton(onClick = { navController.navigate("reset") }) {
            Text("¿Olvidaste tu contraseña?", fontSize = 16.sp, color = Color(0xFF00C6A7))
        }

        Spacer(modifier = Modifier.height(16.dp))

        viewModel.error?.let {
            Text(text = it, color = Color.Red, fontSize = 14.sp)
        }
    }
}

