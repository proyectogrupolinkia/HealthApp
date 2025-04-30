package com.tusalud.healthapp.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: ProgressViewModel = hiltViewModel()
) {
    val progressState by viewModel.progress.collectAsState()

    progressState?.let { progress ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF00BCD4)) // Fondo azul tipo progresión
                .padding(16.dp)
        ) {
            Text(
                text = "Progreso",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Peso e IMC
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                ProgressInfoCard(title = "Peso", value = "${progress.weightKg} kg")
                ProgressInfoCard(title = "IMC", value = "${progress.bmi}")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Aquí podrías poner una gráfica
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Gráfico de evolución", color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Porcentaje de grasa
            ProgressInfoCard(title = "Porcentaje de grasa", value = "${progress.bodyFatPercentage} %")

            Spacer(modifier = Modifier.height(24.dp))

            // Desafío activo
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF7E57C2) // Violeta suave
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Desafío activo",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = progress.activeChallenge,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ProgressInfoCard(title: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.2f))
            .padding(16.dp)
    ) {
        Text(text = title, color = Color.White, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}