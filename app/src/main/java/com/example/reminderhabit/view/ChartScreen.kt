package com.example.reminderhabit.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.reminderhabit.ui.theme.HEXFFFFFFFF
import com.example.reminderhabit.ui.theme.RobotoBoldWithHEX31394f18Sp


@Composable
fun ChartScreen(
) {
    ChartPreview()
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chart(graphModel: List<GraphModel>) {
    val maxBarHeight = 200.dp

    Column(modifier = Modifier.fillMaxSize()) {
        // Top App Bar
        TopAppBar(
            modifier = Modifier
                .shadow(4.dp)
            ,
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

        Text("Week ",
            style = RobotoBoldWithHEX31394f18Sp,
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp))

        Spacer(modifier = Modifier.height(16.dp))

        // Bar Chart Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = HEXFFFFFFFF,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Bar Chart
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(maxBarHeight),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                itemsIndexed(graphModel) { _, task ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Box(
                            modifier = Modifier
                                .height(task.total.dp)
                                .width(24.dp)
                                .background(
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(4.dp)
                                )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(task.completed.dp)
                                    .align(Alignment.BottomCenter)
                                    .background(
                                        color = Color.Green,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            )
                        }


                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = task.date,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}




// Data Model
data class GraphModel(val date: String, val completed: Float, val total: Float)

// Preview Example
@Composable
fun ChartPreview() {
    val sampleData = listOf(
        GraphModel("Mon", 0f,0f),
        GraphModel("Tue", 0f,100f),
        GraphModel("Wed", 30f,80f),
        GraphModel("Thu", 80f,100f),
        GraphModel("Fri", 60f,75f),
        GraphModel("Sat", 50f,100f),
        GraphModel("Sun", 90f,95f)
    )
    Chart(graphModel = sampleData)
}
