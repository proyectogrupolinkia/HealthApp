/**
 * Pantalla que permite al usuario gestionar recordatorios personalizados.
 * Puede agregar, visualizar y eliminar recordatorios.
 */
package com.tusalud.healthapp.presentation.menu.progress.recordatorios

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun RecordatoriosScreen(
    navController: NavHostController,
    viewModel: RecordatoriosViewModel = hiltViewModel()
) {
    // Recoge los estados desde el ViewModel

    val recordatorios by viewModel.recordatorios.collectAsState()
    val nuevoRecordatorio by viewModel.nuevoRecordatorio.collectAsState()

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
        // Título principal

        Text(
            "Recordatorios",
            fontSize = 28.sp,
            color = Color(0xFF00C6A7),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // Campo de texto para escribir un nuevo recordatorio

        OutlinedTextField(
            value = nuevoRecordatorio,
            onValueChange = viewModel::onNuevoRecordatorioChanged,
            label = { Text("Nuevo recordatorio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        // Botón para agregar un nuevo recordatorio

        Button(
            onClick = { viewModel.agregarRecordatorio() },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Agregar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (recordatorios.isEmpty()) {
            Text("No hay recordatorios aún.", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(recordatorios) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7F4))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = item, fontSize = 16.sp)

                            // Botón para eliminar el recordatorio

                            IconButton(onClick = { viewModel.eliminarRecordatorio(item) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}







