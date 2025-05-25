package com.tusalud.healthapp.presentation.menu.meditacion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults


/** Función encargada de mostrar la pantalla general de la
 * actividad Meditacion
 * */
@Composable
fun MeditacionScreen(navController: NavHostController,
                     viewModel: MeditacionViewModel = viewModel()) {
    val estado by viewModel.estado.collectAsState()
    val scrollState = rememberScrollState() // Estado del scroll

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // Habilita scroll vertical
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        // Título
        Text(
            text = "Ejercicios de Meditación",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        // Contenido con scroll
        when (val estadoActual = estado) {
            is MeditacionViewModel.MeditacionEstado.Cargando -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            is MeditacionViewModel.MeditacionEstado.Exito -> {
                ListaEjercicios(estadoActual.ejercicios)
            }

            is MeditacionViewModel.MeditacionEstado.Error -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error: ${estadoActual.mensaje}",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.cargarEjercicios() }) {
                        Text("Reintentar")
                    }
                }
            }
        }
    }
}

/** Función que hace un listado con todos los ejercicios disponibles y
 * los muestra en un determinado formato.
 * */
@Composable
fun ListaEjercicios(ejercicios: List<EjercicioMeditacion>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ejercicios.forEach { ejercicio ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEB3B)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = ejercicio.nombre,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF1B5E20)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = ejercicio.descripcion,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Duración: ${ejercicio.duracionMinutos} min",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF000901)
                    )
                }
            }
        }
    }
}
