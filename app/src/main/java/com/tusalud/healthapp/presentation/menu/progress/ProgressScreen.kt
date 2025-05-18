
package com.tusalud.healthapp.presentation.menu.progress

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tusalud.healthapp.R
import com.tusalud.healthapp.presentation.main.MainViewModel
import com.tusalud.healthapp.presentation.main.ProgressInfoCard
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
    viewModel: MainViewModel = hiltViewModel()
){
    val progressState by viewModel.progress.collectAsState()
    val pesos by viewModel.pesos.collectAsState()

    //garantiza que se recargue al entrar a esta pantalla
    LaunchedEffect(navController.currentBackStackEntry) {
        viewModel.loadProgress()
        viewModel.cargarPesosDesdeFirebase()
    }

    progressState?.let { progress ->
        Column(
            modifier = Modifier
                .fillMaxSize()
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

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                ProgressInfoCard(title = "Peso", value = "${progress.peso} kg")
                ProgressInfoCard(title = "IMC", value = String.format("%.2f", progress.bmi))
                ProgressInfoCard(title = "Peso objetivo", value = "${progress.pesoObjetivo ?: "--"} kg")
            }

            Spacer(modifier = Modifier.height(16.dp))

            //grafico de evolucion
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.3f))
                    .clickable { navController.navigate("evolucion_peso") },
                contentAlignment = Alignment.Center
            ) {
                if (pesos.isNotEmpty()) {
                    val points = pesos.mapIndexed { index, peso ->
                        Point(value = peso, label = "Día ${index + 1}")
                    }
                    val chartData = LineChartData(points)
                    LineChart(
                        lineChartData = chartData,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        pointDrawer = FilledCircularPointDrawer(color = Color.White),
                        lineDrawer = SolidLineDrawer(color = Color.White),
                        xAxisDrawer = SimpleXAxisDrawer(axisLineColor = Color.White),
                        yAxisDrawer = SimpleYAxisDrawer(axisLineColor = Color.White)
                    )
                } else {
                    Text("Gráfico de evolución", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //boton de desafios
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { navController.navigate("desafio") },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF9C27B0))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Desafío activo",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }



            Button(
               onClick = { navController.navigate("meditacion") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Ejercicios de Relajación",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            //imagen de la bascula con tamaño más grande
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bascula),
                    contentDescription = "Báscula",
                    modifier = Modifier
                        .fillMaxHeight(0.9f)
                        .aspectRatio(1f),
                    contentScale = ContentScale.Fit
                )
            }

            Button(
                onClick = { navController.navigate("actualizar_peso") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF388E3C),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Actualizar peso",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
    @Composable
    fun ProgressInfoCard(
        pesoActual: Float,
        pesoObjetivo: Float
    ) {
        val progreso = (pesoActual / pesoObjetivo).coerceIn(0f, 1f)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Progreso", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(progress = progreso)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Peso actual: ${pesoActual} kg")
                Text("Objetivo: ${pesoObjetivo} kg")
            }
        }
    }
}