import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import me.bytebeats.views.charts.line.LineChart
import me.bytebeats.views.charts.line.LineChartData.Point
import androidx.compose.runtime.getValue


import me.bytebeats.views.charts.line.LineChartData
import me.bytebeats.views.charts.line.render.line.SolidLineDrawer
import me.bytebeats.views.charts.line.render.point.FilledCircularPointDrawer
import me.bytebeats.views.charts.line.render.xaxis.SimpleXAxisDrawer
import me.bytebeats.views.charts.line.render.yaxis.SimpleYAxisDrawer


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvolucionPesoScreen(
    navController: NavHostController,
    viewModel: EvolucionPesoViewModel = viewModel()
) {
    val pesos by viewModel.pesos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Evolución del peso") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (pesos.isNotEmpty()) {
                val points = pesos.mapIndexed { index, peso ->
                    Point(value = peso, label = "Día ${index + 1}")
                }
                val lineChartData = LineChartData(points)

                LineChart(
                    lineChartData = lineChartData,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    pointDrawer = FilledCircularPointDrawer(color = Color.Blue),
                    lineDrawer = SolidLineDrawer(color = Color.Blue),
                    xAxisDrawer = SimpleXAxisDrawer(axisLineColor = Color.Gray),
                    yAxisDrawer = SimpleYAxisDrawer(axisLineColor = Color.Gray)
                )
            } else {
                Text("Cargando datos...")
            }
        }
    }
}