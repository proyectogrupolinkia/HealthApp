
package com.tusalud.healthapp.presentation.menu.progress


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel

import com.tusalud.healthapp.presentation.components.ProgressInfoCard
import com.tusalud.healthapp.presentation.progress.ProgressViewModel
import me.bytebeats.views.charts.line.*
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tusalud.healthapp.R
import com.tusalud.healthapp.presentation.main.MainViewModel
import me.bytebeats.views.charts.line.LineChart
import me.bytebeats.views.charts.line.LineChartData
import me.bytebeats.views.charts.line.LineChartData.Point
import me.bytebeats.views.charts.line.render.line.SolidLineDrawer
import me.bytebeats.views.charts.line.render.point.FilledCircularPointDrawer
import me.bytebeats.views.charts.line.render.xaxis.SimpleXAxisDrawer
import me.bytebeats.views.charts.line.render.yaxis.SimpleYAxisDrawer


@Composable
fun ProgressScreen(
    navController: NavHostController,
    viewModel: ProgressViewModel = hiltViewModel()
) {
    val progressState by viewModel.progress.collectAsState()
    val pesos by viewModel.pesos.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val snackbarActivo by viewModel.snackbarActivo.collectAsState()
    val snackbarMensaje by viewModel.snackbarMensaje.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProgress()
        viewModel.cargarPesosDesdeFirebase()
    }

    if (snackbarActivo) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                TextButton(onClick = { viewModel.snackbarActivo.value = false }) {
                    Text("Cerrar", color = Color.White)
                }
            },
            containerColor = Color(0xFF4CAF50)
        ) {
            Text(snackbarMensaje, color = Color.White)
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(0xFF00BCD4))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Progreso",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))

        progressState?.let { progress ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                ProgressInfoCard(title = "Peso", value = "${progress.peso} kg")
                ProgressInfoCard(title = "IMC", value = String.format("%.2f", progress.bmi))
                ProgressInfoCard(
                    title = "Peso objetivo",
                    value = progress.pesoObjetivo?.toString() ?: "--"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (pesos.isNotEmpty()) {
            val points = pesos.mapIndexed { index, peso ->
                Point(value = peso, label = "Día ${index + 1}")
            }
            val chartData = LineChartData(points)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.3f))
                    .clickable { navController.navigate("evolucion_peso") },
                contentAlignment = Alignment.Center
            ) {
                LineChart(
                    lineChartData = chartData,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    pointDrawer = FilledCircularPointDrawer(color = Color.White),
                    lineDrawer = SolidLineDrawer(color = Color.White),
                    xAxisDrawer = SimpleXAxisDrawer(labelTextColor = Color.Transparent),
                    yAxisDrawer = SimpleYAxisDrawer(labelTextColor = Color.Transparent)

                )
            }

        } else {
            Text("Gráfico de evolución", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { navController.navigate("desafio") },
            colors = CardDefaults.cardColors(containerColor = Color(0xFF9C27B0))
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Desafío activo", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        Button(
            onClick = { navController.navigate("meditacion") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Ejercicios de Relajación", style = MaterialTheme.typography.titleMedium)
        }

        Box(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.bascula),
                contentDescription = "Báscula",
                modifier = Modifier.fillMaxHeight(0.9f).aspectRatio(1f),
                contentScale = ContentScale.Fit
            )
        }

        Button(
            onClick = { navController.navigate("actualizar_peso") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C), contentColor = Color.White)
        ) {
            Text("Actualizar peso", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}
