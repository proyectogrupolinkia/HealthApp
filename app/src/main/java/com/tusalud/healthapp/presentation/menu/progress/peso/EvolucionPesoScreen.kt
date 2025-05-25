package com.tusalud.healthapp.presentation.menu.progress.peso

import android.util.Log
import android.view.ViewGroup
import android.graphics.Color as AndroidColor
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import androidx.compose.ui.viewinterop.AndroidView

val LightGreenBackground = Color(0xFFDFF5E3)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvolucionPesoScreen(
    navController: NavHostController,
    viewModel: EvolucionPesoViewModel = hiltViewModel()
) {
    val pesosConFechas by viewModel.pesosConFechas.collectAsState()
    val pesoObjetivo by viewModel.pesoObjetivo.collectAsState()
    val context = LocalContext.current

    // 🔄 Recargar SIEMPRE al entrar
    LaunchedEffect(true) {
        viewModel.cargarDatosEvolucion()
    }

    val pesoObjetivoFloat: Float? = when (pesoObjetivo) {
        is Float -> pesoObjetivo
        is Double -> pesoObjetivo.toFloat()
        is Int -> pesoObjetivo.toFloat()
        is String -> pesoObjetivo.toFloatOrNull()
        else -> null
    } as Float?

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
                .fillMaxSize()
                .background(LightGreenBackground)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (pesosConFechas.isNotEmpty() && pesoObjetivoFloat != null) {

                    val fechas = pesosConFechas.map { it.second }

                    AndroidView(
                        factory = { context: Context ->
                            val chart = LineChart(context).apply {
                                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                                setTouchEnabled(true)
                                isDragEnabled = true
                                setScaleEnabled(true)
                                description.isEnabled = false

                                xAxis.position = XAxisPosition.BOTTOM
                                xAxis.granularity = 1f
                                xAxis.setLabelCount(fechas.size, true)
                                xAxis.valueFormatter = object : ValueFormatter() {
                                    override fun getFormattedValue(value: Float): String {
                                        val index = value.toInt()
                                        return if (index in fechas.indices) fechas[index] else ""
                                    }
                                }

                                axisRight.isEnabled = false
                                axisLeft.removeAllLimitLines()
                                axisLeft.enableGridDashedLine(10f, 10f, 0f)
                                axisLeft.setDrawLimitLinesBehindData(true)
                            }

                            val entries = pesosConFechas.mapIndexedNotNull { index, (pesoRaw, _) ->
                                try {
                                    val peso = when (pesoRaw) {
                                        is Float -> pesoRaw
                                        is Double -> pesoRaw.toFloat()
                                        is Int -> pesoRaw.toFloat()
                                        is String -> pesoRaw.toFloatOrNull()
                                        else -> null
                                    }
                                    peso?.let { Entry(index.toFloat(), it) }
                                } catch (e: Exception) {
                                    Log.e("EvolucionPesoScreen", "Error al convertir peso: $pesoRaw", e)
                                    null
                                }
                            }

                            val pesosSolo = entries.map { it.y }
                            val yMin = minOf(pesosSolo.minOrNull() ?: 0f, pesoObjetivoFloat)
                            val yMax = maxOf(pesosSolo.maxOrNull() ?: 0f, pesoObjetivoFloat)

                            chart.axisLeft.axisMinimum = yMin - 1f
                            chart.axisLeft.axisMaximum = yMax + 1f

                            chart.axisLeft.addLimitLine(
                                LimitLine(pesoObjetivoFloat, "Peso Objetivo").apply {
                                    lineColor = AndroidColor.RED
                                    lineWidth = 2f
                                    textColor = AndroidColor.RED
                                    textSize = 12f
                                }
                            )

                            val lineDataSet = LineDataSet(entries, "Peso").apply {
                                color = AndroidColor.BLUE
                                valueTextColor = AndroidColor.BLACK
                                lineWidth = 2f
                                circleRadius = 4f
                                setCircleColor(AndroidColor.BLUE)
                                setDrawValues(true)
                            }

                            val data = LineData(lineDataSet as ILineDataSet)
                            chart.data = data
                            chart.invalidate()
                            chart.requestLayout()

                            chart
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    )
                } else {
                    Text("Cargando datos...", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
