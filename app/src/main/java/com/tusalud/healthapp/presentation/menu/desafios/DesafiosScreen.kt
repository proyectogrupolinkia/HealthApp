package com.tusalud.healthapp.presentation.menu.desafios

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.asPaddingValues
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tusalud.healthapp.R

@Composable
fun DesafiosScreen(
    navController: NavHostController,
    viewModel: DesafiosViewModel = viewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.resetearDesafiosDiarios(context)
    }

    val desafios by viewModel.desafios.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFEC4899), Color(0xFF6366F1))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(WindowInsets.statusBars.asPaddingValues())
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text("Volver")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.meditation),
                contentDescription = "Meditación",
                modifier = Modifier
                    .size(160.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                "Desafíos",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            desafios.forEach { desafio ->
                DesafioCard(desafio = desafio, onCompletar = {
                    viewModel.marcarDesafioComoRealizado(desafio.id)
                })
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun DesafioCard(desafio: Desafio, onCompletar: () -> Unit) {
    var mostrarDialogo by remember { mutableStateOf(false) }
    var recordatorio by remember { mutableStateOf<String?>(null) }

    val progresoRelativo = (desafio.progreso / desafio.objetivo).coerceAtMost(1f)
    val progresoTexto = when (desafio.unidad) {
        "ml", "litros" -> "${desafio.progreso.toInt()}/${desafio.objetivo.toInt()}"
        "kg" -> "${(progresoRelativo * 100).toInt()} %"
        "veces" -> "${desafio.progreso.toInt()}/${desafio.objetivo.toInt()}"
        else -> "${desafio.progreso}/${desafio.objetivo}"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clickable { onCompletar() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0x40FFFFFF))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = desafio.nombre,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                IconButton(onClick = { mostrarDialogo = true }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Editar recordatorio",
                        tint = Color.Yellow
                    )
                }
            }

            if (!recordatorio.isNullOrEmpty()) {
                Text(
                    text = "🔔 $recordatorio",
                    fontSize = 12.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = progresoTexto,
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            LinearProgressIndicator(
                progress = progresoRelativo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = Color(0xFFFACC15),
                trackColor = Color.White.copy(alpha = 0.3f)
            )
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Agregar/Editar recordatorio") },
            text = {
                OutlinedTextField(
                    value = recordatorio ?: "",
                    onValueChange = { recordatorio = it },
                    label = { Text("Recordatorio") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    recordatorio = null
                    mostrarDialogo = false
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}




