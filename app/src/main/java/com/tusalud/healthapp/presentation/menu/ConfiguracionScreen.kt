package com.tusalud.healthapp.presentation.menu

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.graphics.Color

@Composable
fun ConfiguracionScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    var notificacionesActivadas by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp)) // Espacio superior

        Text(
            "Configuración",
            fontSize = 24.sp,
            color = Color(0xFF00C6A7),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        //cambiar contraseña
        Button(onClick = {
            val email = auth.currentUser?.email
            if (!email.isNullOrBlank()) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Se ha enviado un correo para restablecer la contraseña",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Error al enviar correo de restablecimiento",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(context, "No se encontró un correo asociado", Toast.LENGTH_LONG).show()
            }
        }) {
            Text("Cambiar contraseña")
        }

        Spacer(modifier = Modifier.height(16.dp))

        //configuracion de notificaciones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Notificaciones", fontSize = 16.sp)
            Switch(
                checked = notificacionesActivadas,
                onCheckedChange = { notificacionesActivadas = it }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        //cerrar sesion
        Button(onClick = {
            auth.signOut()
            navController.navigate("login") {
                popUpTo("configuracion") { inclusive = true }
            }
        }) {
            Text("Cerrar sesión")
        }
    }
}



