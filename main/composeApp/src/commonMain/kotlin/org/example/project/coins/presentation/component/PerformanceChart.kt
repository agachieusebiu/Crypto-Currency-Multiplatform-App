package org.example.project.coins.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/**
 * A composable function that displays a performance chart based on a list of nodes (data points).
 * The chart is drawn using a Canvas, and the color of the line is determined by whether the last value is greater than the first value (profit) or not (loss).
 *
 * @param modifier The modifier to be applied to the chart.
 * @param nodes A list of Double values representing the data points for the performance chart.
 * @param profitColor The color to be used for the line if the performance is positive (profit).
 * @param lossColor The color to be used for the line if the performance is negative (loss).
 */
@Composable
fun PerformanceChart(
    modifier: Modifier = Modifier,
    nodes: List<Double>,
    profitColor: Color,
    lossColor: Color
) {
    if (nodes.isEmpty()) return

    val max = nodes.maxOrNull() ?: return
    val min = nodes.minOrNull() ?: return
    val lineColor = if (nodes.last() > nodes.first()) profitColor else lossColor

    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        val path = Path()
        nodes.forEachIndexed { index, value ->
            val x = index * (size.width / (nodes.size - 1))
            val y = size.height * (1 - ((value - min) / (max - min)).toFloat())

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 3.dp.toPx())
        )
    }
}