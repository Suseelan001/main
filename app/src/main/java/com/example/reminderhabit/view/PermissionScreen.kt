package com.example.reminderhabit.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.reminderhabit.R
import com.example.reminderhabit.viewmodel.MainViewmodel

@Composable
fun PermissionScreen(navHostController: NavHostController,mainViewmodel: MainViewmodel){

   // EmailApp()
    //AddTaskNew()
  //  MeetingCard()
  //  TaskCard()
    WeeklyBarChartPreview()

}




@Composable
fun MeetingCard() {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .wrapContentWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .padding(16.dp)
        ) {
            // Header Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp) // Ensures text starts with 16dp padding
            ) {
                Column {
                    Text(text = "Meeting with Client")
                    Text(text = "Do a meeting for NFT website")
                }
                IconButton(onClick = { /* Call Action */ }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_add_circle_outline_24),
                        contentDescription = "Call"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Details Section
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.chart),
                        contentDescription = "Category",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Digital Marketing")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_calendar_month_24),
                        contentDescription = "Date",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "12/12/2024")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.clock_24),
                        contentDescription = "Time",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "8:00 PM")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer Section
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp) // Ensures "Completed" button is at the end
            ) {
                Spacer(modifier = Modifier.weight(1f)) // Pushes the button to the end
                Button(
                    onClick = { /* Completed Action */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50), // Green color
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Completed")
                }
            }
        }
    }
}


@Composable
fun TaskCard() {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.profile),
                    contentDescription = "Checkbox",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Complete main UI components",
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(
                        text = "Would be good if we include every component in the design system...",
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Date Icon and Text
                    Icon(
                        painter = painterResource(R.drawable.baseline_calendar_month_24), // Replace with calendar icon
                        contentDescription = "Calendar",
                        tint = Color.Red,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "6 Apr 2022",
                        color = Color.Red
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Profile Icon and Name
                    Icon(
                        painter = painterResource(R.drawable.profile), // Replace with profile icon
                        contentDescription = "Profile",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Esther Howard",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}





@Composable
fun WeeklyBarChart(data: List<Pair<String, Float>>) {
    val maxBarHeight = 200.dp
    val maxDataValue = data.maxOf { it.second }

    Column(modifier = Modifier.fillMaxWidth()) {


        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxBarHeight),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            data.forEach { (day, value) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    // Bar
                    Box(
                        modifier = Modifier
                            .height((maxBarHeight * (value / maxDataValue)).coerceIn(0.dp, maxBarHeight))
                            .width(24.dp)
                            .background(
                                color = if (value == maxDataValue) Color.Red else Color.DarkGray,
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = day, style = MaterialTheme.typography.bodySmall)
                }
            }
        }


    }
}

@Composable
fun WeeklyBarChartPreview() {
    val sampleData = listOf(
        "Mon" to 1f,
        "Tue" to 2.5f,
        "Wed" to 2f,
        "Thu" to 3.5f,
        "Fri" to 4f,
        "Sat" to 3f,
        "Sun" to 5f
    )
    WeeklyBarChart(data = sampleData)
}
