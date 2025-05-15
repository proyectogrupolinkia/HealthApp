package com.tusalud.healthapp.presentation.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.tusalud.healthapp.presentation.components.BottomNavigationBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PerfilScreen(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf(2) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                navController = navController
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {

            val auth = FirebaseAuth.getInstance()
            var displayName by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var showLogoutDialog by remember { mutableStateOf(false) }
            var showHelpDialog by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                auth.currentUser?.let { user ->
                    displayName = user.displayName ?: "Usuario"
                    email = user.email ?: "sin correo"
                }
            }

            if (showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = { showLogoutDialog = false },
                    title = { Text("Cerrar sesión") },
                    text = { Text("¿Estás seguro que deseas cerrar sesión?") },
                    confirmButton = {
                        TextButton(onClick = {
                            showLogoutDialog = false
                            auth.signOut()
                            navController.navigate("login") {
                                popUpTo("perfil") { inclusive = true }
                            }
                        }) {
                            Text("Cerrar sesión")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showLogoutDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            if (showHelpDialog) {
                AlertDialog(
                    onDismissRequest = { showHelpDialog = false },
                    title = { Text("Ayuda") },
                    text = {
                        Text("¿Necesitás asistencia?\n\nContactanos a:\n📧 soporte@tusalud.com\n📞 +54 11 1234 5678\n\nO visitá nuestra web.")
                    },
                    confirmButton = {
                        TextButton(onClick = { showHelpDialog = false }) {
                            Text("Entendido")
                        }
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF00C6A7))
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(40.dp))

                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = Color(0xFF00C6A7),
                            modifier = Modifier.size(60.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(displayName, fontSize = 24.sp, color = Color.White)
                    Text(email, fontSize = 16.sp, color = Color.White)

                    Spacer(modifier = Modifier.height(32.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .padding(8.dp)
                    ) {
                        PerfilOptionItem(icon = Icons.Default.Edit, label = "Editar perfil") {
                            navController.navigate("editarPerfil")
                        }

                        PerfilOptionItem(icon = Icons.Default.Notifications, label = "Recordatorios") {
                            navController.navigate("recordatorios")
                        }

                        PerfilOptionItem(icon = Icons.Default.Settings, label = "Configuración") {
                            navController.navigate("configuracion")
                        }

                        PerfilOptionItem(icon = Icons.Filled.Help, label = "Ayuda") {
                            showHelpDialog = true
                        }

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        PerfilOptionItem(icon = Icons.Default.ExitToApp, label = "Cerrar sesión") {
                            showLogoutDialog = true
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PerfilOptionItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = Color(0xFF00C6A7))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, fontSize = 16.sp, color = Color.Black)
    }
}