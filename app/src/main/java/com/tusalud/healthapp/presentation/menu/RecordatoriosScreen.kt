package com.tusalud.healthapp.presentation.menu

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
import androidx.navigation.NavHostController

@Composable
fun RecordatoriosScreen(navController: NavHostController) {
    var recordatorios by remember { mutableStateOf(listOf<String>()) }
    var nuevoRecordatorio by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))  // Espacio superior

        Text(
            "Recordatorios",
            fontSize = 24.sp,
            color = Color(0xFF00C6A7),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = nuevoRecordatorio,
            onValueChange = { nuevoRecordatorio = it },
            label = { Text("Nuevo recordatorio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (nuevoRecordatorio.isNotBlank()) {
                    recordatorios = recordatorios + nuevoRecordatorio
                    nuevoRecordatorio = ""
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Agregar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (recordatorios.isEmpty()) {
            Text("No hay recordatorios aún.", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
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
                            IconButton(onClick = {
                                recordatorios = recordatorios - item
                            }) {
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





