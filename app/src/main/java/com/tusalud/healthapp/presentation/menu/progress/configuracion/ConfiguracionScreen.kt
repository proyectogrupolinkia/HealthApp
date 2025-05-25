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

    //  estado de las notificaciones (activadas o no)
    val notificaciones by viewModel.notificacionesActivadas.collectAsState()

    //  mensajes emitidos por el ViewModel para mostrarlos como Toast
    LaunchedEffect(Unit) {
        viewModel.toastMessage.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botón para volver a la pantalla anterior
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("Volver")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Título de la pantalla
        Text(
            "Configuración",
            fontSize = 28.sp,
            color = Color(0xFF00C6A7),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Botón para solicitar el restablecimiento de contraseña por correo
        Button(onClick = { viewModel.sendPasswordResetEmail() }) {
            Text("Cambiar contraseña")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sección para activar o desactivar notificaciones
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

        Spacer(modifier = Modifier.height(40.dp))

        // Botón para cerrar sesión
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
