
//La función ProgressScreen es una pantalla principal de seguimiento de progreso del usuario en tu app HealthApp. Muestra información clave como el peso actual, IMC, peso objetivo, un gráfico de evolución del peso y botones para acceder a otras funcionalidades
// como meditación, desafíos y actualizar el peso.

package com.tusalud.healthapp.presentation.menu.progress


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel

import com.tusalud.healthapp.presentation.components.ProgressInfoCard
import com.tusalud.healthapp.presentation.progress.ProgressViewModel
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tusalud.healthapp.R
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

    // Se recogen los estados del progreso y los pesos desde el ViewModel

    val progressState by viewModel.progress.collectAsState()
    val pesos by viewModel.pesos.collectAsState()

    // Se detecta la orientación del dispositivo

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE


    // Se lanzan efectos al cargar la pantalla para obtener datos actualizados
    LaunchedEffect(Unit) {
        viewModel.loadProgress()
        viewModel.cargarPesosDesdeFirebase()
    }

    // Solo si hay datos de progreso disponibles

    progressState?.let { progress ->
        if (isLandscape) {

            // Diseño horizontal (landscape): divide en dos columnas

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF00BCD4))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Columna izquierda con tarjetas de progreso y botones

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .fillMaxHeight()
                )
                {
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
                        // Tarjetas con información: peso, IMC, objetivo


                        ProgressInfoCard(title = "Peso", value = "${progress.peso} kg")
                        ProgressInfoCard(title = "IMC", value = String.format("%.2f", progress.bmi))
                        ProgressInfoCard(title = "Peso objetivo", value = "${progress.pesoObjetivo ?: "--"} kg")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Miniatura de evolución del peso


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
                                xAxisDrawer = SimpleXAxisDrawer(
                                    axisLineColor = Color.White,
                                    labelTextColor = Color.Transparent
                                ),
                                yAxisDrawer = SimpleYAxisDrawer(
                                    axisLineColor = Color.White,
                                    labelTextColor = Color.Transparent
                                )
                            )
                        } else {
                            Text("Gráfico de evolución", color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón hacia desafíos activos
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

                    // Botón hacia meditación
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

                    // Botón para actualizar peso

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

                // Columna derecha: imagen decorativa

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bascula),
                        contentDescription = "Báscula",
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .aspectRatio(1f),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        } else {
            // Diseño vertical (portrait)

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

                // Tarjetas de peso, IMC, objetivo

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ProgressInfoCard(title = "Peso", value = "${progress.peso} kg")
                    ProgressInfoCard(title = "IMC", value = String.format("%.2f", progress.bmi))
                    ProgressInfoCard(title = "Peso objetivo", value = "${progress.pesoObjetivo ?: "--"} kg")
                }

                Spacer(modifier = Modifier.height(16.dp))
                // Gráfico

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
                            xAxisDrawer = SimpleXAxisDrawer(
                                axisLineColor = Color.White,
                                labelTextColor = Color.Transparent
                            ),
                            yAxisDrawer = SimpleYAxisDrawer(
                                axisLineColor = Color.White,
                                labelTextColor = Color.Transparent
                            )
                        )
                    } else {
                        Text("Gráfico de evolución", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Desafío activo

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
                // Botón a meditación

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
                // Imagen decorativa

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
                // Botón para actualizar peso

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
    }
}