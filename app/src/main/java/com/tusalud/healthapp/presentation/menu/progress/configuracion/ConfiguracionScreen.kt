package com.tusalud.healthapp.presentation.menu.progress.configuracion

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ConfiguracionScreen(
    navController: NavHostController,
    viewModel: ConfiguracionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val notificaciones by viewModel.notificacionesActivadas.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.toastMessage.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            "Configuración",
            fontSize = 24.sp,
            color = Color(0xFF00C6A7),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(onClick = { viewModel.sendPasswordResetEmail() }) {
            Text("Cambiar contraseña")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Notificaciones", fontSize = 16.sp)
            Switch(
                checked = notificaciones,
                onCheckedChange = { viewModel.toggleNotificaciones(it) }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            viewModel.logout {
                navController.navigate("login") {
                    popUpTo("configuracion") { inclusive = true }
                }
            }
        }) {
            Text("Cerrar sesión")
        }
    }
}



