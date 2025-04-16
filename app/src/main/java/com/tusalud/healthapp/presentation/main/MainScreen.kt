package com.tusalud.healthapp.presentation.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tusalud.healthapp.domain.model.User
import kotlin.math.pow

@Composable
fun MainScreen(user: User) {
    var pesoActual by remember { mutableStateOf(user.peso) }
    var pesosHistoricos by remember { mutableStateOf(listOf(user.peso)) }

    val imc = if (user.altura > 0) pesoActual / (user.altura / 100).pow(2) else 0f

    Column(Modifier.padding(16.dp)) {
        Text("Bienvenido, ${user.nombre}", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Text("Peso inicial: ${user.peso} kg")
        Text("Peso actual: $pesoActual kg")
        Text("Peso objetivo: 70 kg") // puedes hacerlo editable si lo deseas
        Text("IMC actual: ${String.format("%.1f", imc)}")
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            pesoActual += 0.5f
            pesosHistoricos = pesosHistoricos + pesoActual
        }) {
            Text("Agregar peso (+0.5 kg)")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Progreso de peso", style = MaterialTheme.typography.titleMedium)
        PesoChart(pesos = pesosHistoricos)
    }
}

@Composable
fun PesoChart(pesos: List<Float>) {
    val maxPeso = pesos.maxOrNull() ?: 1f
    val minPeso = pesos.minOrNull() ?: 0f
    val heightRange = maxPeso - minPeso

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .padding(8.dp)) {

        val spacing = size.width / (pesos.size.coerceAtLeast(2) - 1)

        pesos.forEachIndexed { index, peso ->
            if (index > 0) {
                val x1 = spacing * (index - 1)
                val y1 = size.height - ((pesos[index - 1] - minPeso) / heightRange * size.height)
                val x2 = spacing * index
                val y2 = size.height - ((peso - minPeso) / heightRange * size.height)

                drawLine(
                    color = Color.Blue,
                    start = androidx.compose.ui.geometry.Offset(x1, y1),
                    end = androidx.compose.ui.geometry.Offset(x2, y2),
                    strokeWidth = 4f
                )
            }
        }
    }
}