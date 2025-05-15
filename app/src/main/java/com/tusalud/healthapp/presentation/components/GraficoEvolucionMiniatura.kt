package com.tusalud.healthapp.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.bytebeats.views.charts.line.LineChart
import me.bytebeats.views.charts.line.LineChartData
import me.bytebeats.views.charts.line.LineChartData.Point
import me.bytebeats.views.charts.line.render.line.SolidLineDrawer
import me.bytebeats.views.charts.line.render.point.FilledCircularPointDrawer
import me.bytebeats.views.charts.line.render.xaxis.SimpleXAxisDrawer
import me.bytebeats.views.charts.line.render.yaxis.SimpleYAxisDrawer

@Composable
fun GraficoEvolucionMiniatura(pesos: List<Float>, modifier: Modifier = Modifier) {
    val points = pesos.mapIndexed { index, peso ->
        Point(value = peso, label = index.toString())
    }
    val chartData = LineChartData(points)

    LineChart(
        lineChartData = chartData,
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),
        pointDrawer = FilledCircularPointDrawer(color = Color.Blue),
        lineDrawer = SolidLineDrawer(color = Color.Blue),
        xAxisDrawer = SimpleXAxisDrawer(axisLineColor = Color.LightGray),
        yAxisDrawer = SimpleYAxisDrawer(axisLineColor = Color.LightGray)
    )
}