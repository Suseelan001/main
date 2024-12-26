package com.example.reminderhabit.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.reminderhabit.viewmodel.MainViewmodel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.Dp
import com.example.reminderhabit.ui.theme.HEX7981ff
import com.example.reminderhabit.ui.theme.Pink40

private val defaultMaxHeight = 200.dp

@Composable
fun ChartScreen(
    navHostController: NavHostController,
    mainViewmodel: MainViewmodel
) {
    BarChartScreen()

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarChartScreen() {
    val data = listOf(50f, 100f) // Example values
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Chart",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)

                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )
        BarChart(
            modifier=Modifier,data
        )
    }
}

@Composable
internal fun BarChart(
    modifier: Modifier = Modifier,
    values: List<Float>,
    maxHeight: Dp = defaultMaxHeight
) {
    assert(values.isNotEmpty()) { "Input values are empty" }

    val density = LocalDensity.current
    val strokeWidth = with(density) { 1.dp.toPx() }

    Row(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .height(maxHeight)
                .drawBehind {
                    // Draw X-Axis
                    drawLine(
                        color = Pink40,
                        start = Offset(100f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth
                    )
                    // Draw Y-Axis
                    drawLine(
                        color = Pink40,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = strokeWidth
                    )
                }
        ),
        horizontalArrangement = Arrangement.spacedBy(8.dp), // Adds consistent spacing
        verticalAlignment = Alignment.Bottom
    ) {
        values.forEach { item ->
            Bar(
                value = item,
                color = HEX7981ff,
                maxHeight = maxHeight
            )
        }
    }
}


@Composable
private fun RowScope.Bar(
    value: Float,
    color: Color,
    maxHeight: Dp
) {

    val itemHeight = remember(value) { value * maxHeight.value / 100 }

    Spacer(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .height(itemHeight.dp)
            .width(10.dp)
            .background(color)
    )

}

