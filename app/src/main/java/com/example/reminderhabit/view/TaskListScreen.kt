package com.example.reminderhabit.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.reminderhabit.R
import com.example.reminderhabit.bottomnavigation.NavRote
import com.example.reminderhabit.model.AddTask
import com.example.reminderhabit.viewmodel.MainViewmodel
import com.example.reminderhabit.viewmodel.TaskViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    type: String,
    navHostController: NavHostController,
    taskViewmodel: TaskViewModel,
    mainViewmodel: MainViewmodel
) {
    val context = LocalContext.current

    val selectedDate = mainViewmodel.selectedDate?.format(DateTimeFormatter.ISO_DATE)
        ?: LocalDate.now().format(DateTimeFormatter.ISO_DATE)

    val getUpcomingList by taskViewmodel.getTasksFromDate(type, selectedDate).observeAsState(emptyList())

    val selectedDayOfWeek = mainViewmodel.selectedDate?.dayOfWeek?.name?.take(3)
        ?: LocalDate.now().dayOfWeek.name.take(3)

    val scheduleNotificationList by taskViewmodel.getUpcomingList(selectedDate).observeAsState(emptyList())

   // scheduleTaskNotifications(context,scheduleNotificationList)

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 60.dp)
            .background(color = colorResource(id = R.color.white))
    ) {
        val (topBar, taskList) = createRefs()

        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.White
            ),
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        type,
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                        color = Color.Black
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = { navHostController.popBackStack() }) {
                    AsyncImage(
                        model = R.drawable.baseline_arrow_back_24,
                        contentDescription = "Back Icon",
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    navHostController.navigate("${NavRote.AddTaskScreen.path}/${type}/${"0"}/${"add"}")

                }) {
                    AsyncImage(
                        model = R.drawable.baseline_add_circle_outline_24,
                        contentDescription = "Add Icon",
                    )
                }
            },
            modifier = Modifier
                .constrainAs(topBar) {
                    top.linkTo(parent.top)
                }
                .fillMaxWidth()
                .shadow(4.dp),
        )

        val filteredList = if (type == "Task") {
            getUpcomingList.filter { it.createdDate == selectedDate }
        } else {
            getUpcomingList.filter { task ->
                task.days.any { day ->
                    day.equals(selectedDayOfWeek, ignoreCase = true)
                }
            }
        }

        if (filteredList.isEmpty()) {
            Text(
                text = "No $type found \n Kindly add $type",
                style = TextStyle(fontSize = 18.sp, color = Color.Gray),
                modifier = Modifier
                    .constrainAs(taskList) {
                        top.linkTo(topBar.bottom, margin = 32.dp)
                    }
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
        } else {
            LazyColumn(
                modifier = Modifier.constrainAs(taskList) {
                    top.linkTo(topBar.bottom)
                }
            ) {
                itemsIndexed(filteredList) { index, task ->
                    TaskCard(task = task, index = index, onClick = {
                        navHostController.navigate("${NavRote.AddTaskScreen.path}/${type}/${task.id}/${"Edit"}")
                    })
                }
            }
        }


    }
}

@Composable
fun TaskCard(
    task: AddTask,
    index: Int,
    onClick: () -> Unit
) {
    val colorsMap = mapOf(
        "red" to Color.Red,
        "green" to Color.Green,
        "blue" to Color.Blue,
        "yellow" to Color.Yellow,
        "magenta" to Color.Magenta
    )
    val taskColor = colorsMap[task.color] ?: Color.Gray

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = taskColor
        )    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = task.title,
                style = TextStyle(fontSize = 16.sp, color = Color.Black),
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (task.description.isNotEmpty()) {
                Text(
                    text = task.description,
                    style = TextStyle(fontSize = 16.sp, color = Color.Black),
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (task.days.isNotEmpty()) {
                Text(
                    text = "Days: ${task.days.joinToString(", ")}",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black),
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            task.startTime.let {
                Text(
                    text = "Start Time: $it",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black),
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            task.endTime.let {
                Text(
                    text = "End Time: $it",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black),
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
