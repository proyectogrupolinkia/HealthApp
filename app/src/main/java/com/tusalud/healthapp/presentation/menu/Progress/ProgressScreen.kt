package com.tusalud.healthapp.presentation.menu.Progress

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tusalud.healthapp.presentation.main.ProgressInfoCard

@Composable
fun ProgressScreen(navController: NavHostController, viewModel: ProgressViewModel) {
    val progressState by viewModel.progress.collectAsState()

    progressState?.let { progress ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF00BCD4))
                .padding(16.dp)
        ) {
            Text(
                text = "Progreso",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                ProgressInfoCard(title = "Peso", value = "${progress.weightKg} kg")
                ProgressInfoCard(title = "IMC", value = "${progress.bmi}")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text("Gráfico de evolución", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
            ProgressInfoCard(title = "Grasa corporal", value = "${progress.bodyFatPercentage} %")
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("desafio")
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF7E57C2))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
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
