package com.tusalud.healthapp.presentation.menu.progress.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun PerfilScreen(
    navController: NavHostController,
    viewModel: PerfilViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.cargarDatosUsuario()
    }

    val displayName by viewModel.displayName.collectAsState(initial = "")
    val email by viewModel.email.collectAsState(initial = "")
    val showLogoutDialog by viewModel.showLogoutDialog.collectAsState(initial = false)
    val showHelpDialog by viewModel.showHelpDialog.collectAsState(initial = false)
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState(initial = false)
    val scrollState = rememberScrollState()

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF00C6A7), Color(0xFF007C91))
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .background(Color.White, CircleShape)
                        .shadow(4.dp, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = Color(0xFF00C6A7),
                        modifier = Modifier.size(64.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(displayName.ifBlank { "Usuario" }, fontSize = 24.sp, color = Color.White)
                Text(email.ifBlank { "Sin correo" }, fontSize = 16.sp, color = Color.White.copy(alpha = 0.85f))

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        PerfilOptionItem(icon = Icons.Default.Edit, label = "Editar perfil") {
                            navController.navigate("editar_perfil")
                        }

                        PerfilOptionItem(icon = Icons.Default.Notifications, label = "Recordatorios") {
                            navController.navigate("recordatorios")
                        }

                        PerfilOptionItem(icon = Icons.Default.Settings, label = "Configuración") {
                            navController.navigate("configuracion")
                        }

                        PerfilOptionItem(icon = Icons.Filled.Help, label = "Ayuda") {
                            viewModel.setShowHelpDialog(true)
                        }

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        PerfilOptionItem(icon = Icons.Default.Delete, label = "Cancelar cuenta") {
                            viewModel.setShowDeleteDialog(true)
                        }

                        PerfilOptionItem(icon = Icons.Default.ExitToApp, label = "Cerrar sesión") {
                            viewModel.setShowLogoutDialog(true)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp)) // Espacio adicional al final
            }

            if (showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = { viewModel.setShowLogoutDialog(false) },
                    title = { Text("Cerrar sesión") },
                    text = { Text("¿Estás seguro que deseas cerrar sesión?") },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.setShowLogoutDialog(false)
                            viewModel.logout {
                                navController.navigate("login") {
                                    popUpTo("perfil") { inclusive = true }
                                }
                            }
                        }) {
                            Text("Cerrar sesión")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.setShowLogoutDialog(false) }) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { viewModel.setShowDeleteDialog(false) },
                    title = { Text("Cancelar cuenta") },
                    text = { Text("¿Estás seguro que deseas eliminar tu cuenta? Esta acción no se puede deshacer.") },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.setShowDeleteDialog(false)
                            viewModel.eliminarCuenta {
                                navController.navigate("login") {
                                    popUpTo("perfil") { inclusive = true }
                                }
                            }
                        }) {
                            Text("Eliminar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.setShowDeleteDialog(false) }) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            if (showHelpDialog) {
                AlertDialog(
                    onDismissRequest = { viewModel.setShowHelpDialog(false) },
                    title = { Text("Ayuda") },
                    text = {
                        Text(
                            "¿Necesitás asistencia?\n\nContactanos a:\n📧 soporte@tusalud.com\n📞 +54 11 1234 5678\n\nO visitá nuestra web."
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { viewModel.setShowHelpDialog(false) }) {
                            Text("Entendido")
                        }
                    }
                )
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
            .padding(vertical = 14.dp, horizontal = 20.dp)
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = Color(0xFF00C6A7), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = label, fontSize = 16.sp, color = Color.Black)
    }
}
