package com.example.reminderhabit.view


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.reminderhabit.R
import com.example.reminderhabit.bottomnavigation.NavRote
import com.example.reminderhabit.model.AddTask
import com.example.reminderhabit.model.CompletedTask
import com.example.reminderhabit.model.SkippedTask
import com.example.reminderhabit.ui.theme.RobotoItalicWithHEX989ba214sp
import com.example.reminderhabit.ui.theme.RobotoMediumWithHEX31394f18sp
import com.example.reminderhabit.ui.theme.RobotoRegularWithHEX31394f18Sp
import com.example.reminderhabit.ui.theme.RobotoRegularWithHEX31394f20Sp
import com.example.reminderhabit.viewmodel.CompletedTaskViewModel
import com.example.reminderhabit.viewmodel.MainViewmodel
import com.example.reminderhabit.viewmodel.SkippedTaskViewModel
import com.example.reminderhabit.viewmodel.TaskViewModel
import com.example.reminderhabit.worker.NotificationWorkerClass
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewmodel,
    taskViewmodel: TaskViewModel,
    skippedTaskViewModel: SkippedTaskViewModel,
    completedTaskViewModel: CompletedTaskViewModel,

    ) {
    val startTime: String = if (mainViewModel.selectedDate?.equals(LocalDate.now()) == true) {
            getCurrent24HrTime()
        } else {
            "00:00"
        }


    val getListFromTimeAndDate by taskViewmodel.getListFromTimeAndDate(startTime, mainViewModel.selectedDate.toString()).observeAsState(emptyList())
    val allTasks by taskViewmodel.getUpcomingList(mainViewModel.selectedDate.toString()).observeAsState(emptyList())



    val getBackLogList by taskViewmodel.getBackLogList(startTime,LocalDate.now().toString()).observeAsState(emptyList())


    val getSkippedTaskList by skippedTaskViewModel.getAllRecord( mainViewModel.selectedDate.toString()).observeAsState(emptyList())
    val getCompletedTaskList by completedTaskViewModel.getAllRecord().observeAsState(emptyList())


    val selectedDayOfWeek = mainViewModel.selectedDate?.dayOfWeek?.name?.take(3)
        ?: LocalDate.now().dayOfWeek.name.take(3)


    val selectedDateViewModel = mainViewModel.selectedDate?.format(DateTimeFormatter.ISO_DATE)
        ?: LocalDate.now().format(DateTimeFormatter.ISO_DATE)

    println("CHECK_TAG_getBackLogList  " + Gson().toJson(getBackLogList))

    LaunchedEffect(getBackLogList) {
        getBackLogList.forEach { task ->
            val backlogTask = SkippedTask(
                title = task.title,
                description = task.description,
                days = task.days,
                startTime = task.startTime,
                endTime = task.endTime,
                color = task.color,
                type = task.type,
                isNotificationEnabled = task.isNotificationEnabled,
                createdDate = task.createdDate
            )
            skippedTaskViewModel.insertTask(backlogTask)

            if (task.type == "Tracker"){
                val nextDate= task.createdDate?.let { getNextDate(it) }
                if (nextDate != null) {
                    taskViewmodel.updateCreatedDate(task.id,nextDate)
                }
            }else{
                if (task.type == "Task"){
                    taskViewmodel.deleteSingleRecord(task.id)

                }
            }

        }

    }





    NotificationPermissionsRequest(
        onPermissionsGranted = {
            // Proceed with notification functionality
            Log.d("MainScreen", "All permissions granted")
        },
        onPermissionsDenied = {
            Log.d("MainScreen", "Permissions denied by the user")
        }
    )



  //  scheduleTaskNotifications(context, allTasks)



    Column(
        modifier = Modifier
            .wrapContentHeight()
            .background(color = colorResource(id = R.color.fbfbfb))
            .fillMaxSize()
            .padding(bottom = 90.dp)

    ) {

        TopSection()
        DayDateSelector(mainViewModel)


        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
            BackLogList(
                mainViewModel,
                skippedTaskViewModel,
                completedTaskViewModel,
                getSkippedTaskList,
                navHostController,
                selectedDateViewModel,
                selectedDayOfWeek
            )}

            item {
            Todolist(
                mainViewModel,
                completedTaskViewModel,
                taskViewmodel,
                getListFromTimeAndDate,
                navHostController,
                selectedDateViewModel,
                selectedDayOfWeek
            )}
            item {
                CompletedList(
                    mainViewModel,
                    getCompletedTaskList,
                    navHostController,
                    selectedDateViewModel
                )
            }

        }
    }

}

fun getCurrent24HrTime(): String {
    val currentTime = LocalTime.now()
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return currentTime.format(formatter)
}

@Composable
fun TopSection() {
    Row(

        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF7981ff))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "Profile",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .border(2.dp, Color.White, CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = "Hi Suseelan",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            GreetingWithTime()
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notification",
            tint = Color.White,
            modifier = Modifier.size(28.dp)

        )
    }
}

@Composable
fun GreetingWithTime() {
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    val greeting = when (currentHour) {
        in 5..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        in 17..20 -> "Good Evening"
        else -> "Good Night"
    }

    Text(
        text = "Hi Suseelan, $greeting!",
        color = Color.White,
        fontSize = 14.sp
    )
}

@Composable
fun DayDateSelector(mainViewModel: MainViewmodel) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    if (mainViewModel.selectedDate != null) {
        selectedDate = mainViewModel.selectedDate
    }

    val startDate = LocalDate.now().withDayOfMonth(1)
    val endDate = LocalDate.now().plusYears(10)

    val totalDays = ChronoUnit.DAYS.between(startDate, endDate).toInt()
    val allDays = List(totalDays) { startDate.plusDays(it.toLong()) }
    val lazyListState = rememberLazyListState()

    var currentMonthYear by remember { mutableStateOf(getMonthYear(selectedDate)) }
    LaunchedEffect(remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }) {
        val currentVisibleDate = allDays[lazyListState.firstVisibleItemIndex]
        currentMonthYear = getMonthYear(currentVisibleDate)
    }

    LaunchedEffect(selectedDate) {
        val index = allDays.indexOf(selectedDate)
        if (index != -1) {
            lazyListState.animateScrollToItem(index)
        }
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = currentMonthYear,
            color = colorResource(id = R.color.A31394f),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            state = lazyListState,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(allDays) { date ->
                val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                val isSelected = date == selectedDate
                mainViewModel.selectedDate = selectedDate
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(
                            if (isSelected) colorResource(id = R.color.A7981ff) else Color.Transparent,
                            shape = MaterialTheme.shapes.medium
                        )
                        .clickable {
                            mainViewModel.selectedDate = date
                            selectedDate = date
                        }
                        .padding(16.dp)
                ) {
                    Text(
                        text = dayName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.White else Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${date.dayOfMonth}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isSelected) Color.White else Color.Black
                    )
                }
            }
        }
    }
}

fun getMonthYear(date: LocalDate): String {
    return "${date.month.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }} ${date.year}"
}


fun scheduleTaskNotifications(context: Context, allTasks: List<AddTask>) {
    allTasks.forEach { task ->
        if (task.type == "Tracker" && task.isNotificationEnabled) {
            task.days.forEach { day ->
                if (day.isNotEmpty()) {
                    val delay = calculateDelayForDay(day, task.startTime)
                    periodicWorkScheduleNotification(context, delay, task.title)
                }
            }
        } else if (task.type == "Task" && task.isNotificationEnabled) {
            val delay = task.createdDate?.let { convertToTimestamp(it, task.startTime) }
                ?.let { calculateDelay(it) }

            if (delay != null && delay > 0) {
                scheduleNotification(context, delay, task.title)
            }
        }
    }
}


fun calculateDelay(targetTime: Long): Long {
    val currentTime = System.currentTimeMillis()
    return targetTime - currentTime
}

fun calculateDelayForDay(day: String, startTime: String): Long {
    val dayOfWeekMap = mapOf(
        "Sun" to Calendar.SUNDAY,
        "Mon" to Calendar.MONDAY,
        "Tue" to Calendar.TUESDAY,
        "Wed" to Calendar.WEDNESDAY,
        "Thu" to Calendar.THURSDAY,
        "Fri" to Calendar.FRIDAY,
        "Sat" to Calendar.SATURDAY
    )

    val targetDayOfWeek = dayOfWeekMap[day] ?: return 0L
    val calendar = Calendar.getInstance()
    val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    val daysUntilTarget = (targetDayOfWeek - currentDayOfWeek + 7) % 7

    val currentTimeMillis = System.currentTimeMillis()

    if (daysUntilTarget == 0) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)

        val targetTimeMillis = convertToTimestamp(formattedDate, startTime)

        if (targetTimeMillis > currentTimeMillis) {
            return targetTimeMillis - currentTimeMillis
        }
    }

    calendar.add(Calendar.DAY_OF_YEAR, if (daysUntilTarget == 0) 7 else daysUntilTarget)

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val formattedDate = dateFormat.format(calendar.time)
    val targetTimeMillis = convertToTimestamp(formattedDate, startTime)

    return targetTimeMillis - currentTimeMillis
}


fun scheduleNotification(context: Context, delay: Long, title: String) {
    val inputData = workDataOf("title" to title)

    val workRequest = OneTimeWorkRequestBuilder<NotificationWorkerClass>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setInputData(inputData)
        .build()

    WorkManager.getInstance(context).enqueueUniqueWork(
        title,
        ExistingWorkPolicy.REPLACE,
        workRequest
    )
}


fun periodicWorkScheduleNotification(context: Context, delay: Long, title: String) {
    val inputData = workDataOf("title" to title)

    val workRequest = PeriodicWorkRequestBuilder<NotificationWorkerClass>(
        7, TimeUnit.DAYS
    ).setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setInputData(inputData)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        title,
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}

fun convertToTimestamp(date: String, time: String): Long {
    val dateTimeString = "$date $time"
    val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
    val parsedDate = dateFormat.parse(dateTimeString)
    return parsedDate?.time ?: 0L
}


@Composable
fun NotificationPermissionsRequest(
    onPermissionsGranted: () -> Unit,
    onPermissionsDenied: () -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allPermissionsGranted = permissions.all { it.value }
        if (allPermissionsGranted) {
            onPermissionsGranted()
        } else {
            onPermissionsDenied()
        }
    }

    val notificationPermissions = listOfNotNull(
        Manifest.permission.ACCESS_NOTIFICATION_POLICY,
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.POST_NOTIFICATIONS
        } else null
    )

    LaunchedEffect(Unit) {
        val permissionsStatus = notificationPermissions.associateWith { permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }

        if (permissionsStatus.all { it.value }) {
            onPermissionsGranted()
        } else {
            permissionLauncher.launch(notificationPermissions.toTypedArray())
        }
    }
}


@Composable
fun BackLogList(
    mainViewModel: MainViewmodel,
    skippedTaskViewModel: SkippedTaskViewModel,
    completedTaskViewModel: CompletedTaskViewModel,
    allTasks: List<SkippedTask>,
    navHostController: NavHostController,
    selectedDate: String,
    selectedDateViewModel: String
) {
    var isLazyColumnVisible by remember { mutableStateOf(true) }

    val iconResource = if (isLazyColumnVisible) {
        R.drawable.baseline_keyboard_arrow_up_24
    } else {
        R.drawable.baseline_keyboard_arrow_down_24
    }

    val filteredList = allTasks.filter { task ->
        val isMatchingDay =
            task.days.any { day -> day.equals(selectedDateViewModel, ignoreCase = true) }
        val isTaskWithEmptyDaysAndCurrentDate = task.type.equals("Task", ignoreCase = true) &&
                task.createdDate == selectedDate

        isMatchingDay || isTaskWithEmptyDaysAndCurrentDate
    }
    if (filteredList.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable (
                    onClick = { isLazyColumnVisible = !isLazyColumnVisible },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }

                )
                .padding(start = 16.dp, top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconResource),
                contentDescription = "Toggle Icon",
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 8.dp),
                contentScale = ContentScale.Crop
            )


            Text(
                text = "Backlog",
                style =RobotoMediumWithHEX31394f18sp ,
                modifier = Modifier
            )
        }

        if (isLazyColumnVisible) {

            val taskClickStates = remember { mutableStateMapOf<Int, Boolean>() }

            filteredList.forEach { task ->
                mainViewModel.selectedDate?.let { selectedDate ->
                    val isLiked = taskClickStates[task.id] ?: false
                    SkippedTaskItem(
                        selectedDate = selectedDate,
                        task = task,
                        isLiked = isLiked,
                        onClick = {
                            navHostController.navigate("${NavRote.AddTaskScreen.path}/${task.type}/${task.id}/${"Edit"}")
                        },
                        onCompleteClick = {
                            val completedTask = CompletedTask(
                                title = task.title,
                                description = task.description,
                                days = task.days,
                                color = task.color,
                                startTime = task.startTime,
                                endTime = task.endTime,
                                type = task.type,
                                isNotificationEnabled = task.isNotificationEnabled,
                                createdDate = task.createdDate
                            )
                            completedTaskViewModel.insertTask(completedTask)
                            skippedTaskViewModel.deleteSingleRecord(task.id)
                            taskClickStates[task.id] = false
                        },
                         onToggleClick = { clicked ->
                            taskClickStates[task.id] = clicked
                        }
                    )
                }
            }
        }

    }


}

@Composable
fun Todolist(
    mainViewModel: MainViewmodel,
    completedTaskViewModel: CompletedTaskViewModel,
    taskViewmodel: TaskViewModel,
    allTasks: List<AddTask>,
    navHostController: NavHostController,
    selectedDate: String,
    selectedDateViewModel: String
) {
    var isLazyColumnVisible by remember { mutableStateOf(true) }

    val iconResource = if (isLazyColumnVisible) {
        R.drawable.baseline_keyboard_arrow_up_24
    } else {
        R.drawable.baseline_keyboard_arrow_down_24
    }

    val filteredList = allTasks.filter { task ->
        val isMatchingDay =
            task.days.any { day -> day.equals(selectedDateViewModel, ignoreCase = true) }

        val isTaskWithEmptyDaysAndCurrentDate = task.type.equals("Task", ignoreCase = true) &&
                task.createdDate == selectedDate

        isMatchingDay || isTaskWithEmptyDaysAndCurrentDate
    }

    if (filteredList.isNotEmpty()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp)
                .clickable (
                    onClick = { isLazyColumnVisible = !isLazyColumnVisible },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }

                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconResource),
                contentDescription = "Toggle Icon",
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 8.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "To Do",
                style =RobotoMediumWithHEX31394f18sp,
                modifier = Modifier.padding(top = 0.dp)
            )
        }
        if (isLazyColumnVisible) {

            val taskClickStates = remember { mutableStateMapOf<Int, Boolean>() }

            filteredList.forEach { task ->
                mainViewModel.selectedDate?.let { selectedDate ->
                    val isLiked = taskClickStates[task.id] ?: false
                    TaskItem(
                        selectedDate = selectedDate,
                        task = task,
                        isLiked = isLiked,
                        onClick = {
                            navHostController.navigate("${NavRote.AddTaskScreen.path}/${task.type}/${task.id}/${"Edit"}")
                        },
                        onCompleteClick = {
                            val completedTask = CompletedTask(
                                title = task.title,
                                description = task.description,
                                days = task.days,
                                color = task.color,
                                startTime = task.startTime,
                                endTime = task.endTime,
                                type = task.type,
                                isNotificationEnabled = task.isNotificationEnabled,
                                createdDate = task.createdDate
                            )
                            completedTaskViewModel.insertTask(completedTask)

                            if (task.type == "Tracker") {
                                val nextDate = task.createdDate?.let { getNextDate(it) }
                                if (nextDate != null) {
                                    taskViewmodel.updateCreatedDate(task.id, nextDate)
                                }
                            } else if (task.type == "Task") {
                                taskViewmodel.deleteSingleRecord(task.id)
                            }
                        },
                        onToggleClick = { clicked ->
                            taskClickStates[task.id] = clicked
                        }
                    )
                }
            }
        }
    }
}

fun getNextDate(dateString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val currentDate = LocalDate.parse(dateString, formatter)

    val nextDate = currentDate.plusDays(1)

    return nextDate.format(formatter)
}
@Composable
fun CompletedList(
    mainViewModel: MainViewmodel,
    completedList: List<CompletedTask>,
    navHostController: NavHostController,
    selectedDate: String) {

    var isLazyColumnVisible by remember { mutableStateOf(true) }

    val iconResource = if (isLazyColumnVisible) {
        R.drawable.baseline_keyboard_arrow_up_24
    } else {
        R.drawable.baseline_keyboard_arrow_down_24
    }


    val filteredList = completedList.filter { task ->
        task.createdDate == selectedDate
    }


    if (filteredList.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable (
                    onClick = { isLazyColumnVisible = !isLazyColumnVisible },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }

                )
                .padding(start = 16.dp, top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconResource),
                contentDescription = "Toggle Icon",
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 8.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "Completed",
                style =RobotoMediumWithHEX31394f18sp ,
                modifier = Modifier.padding(top = 0.dp)
            )
        }

        if (isLazyColumnVisible) {
            filteredList.forEach{task ->
                mainViewModel.selectedDate?.let {
                    CompletedTaskItem(it,task = task, onClick = {
                        navHostController.navigate("${NavRote.AddTaskScreen.path}/${task.type}/${task.id}/${"Edit"}")
                    })
                }

            }

        }
    }
}

@Composable
fun TaskItem(
    selectedDate: LocalDate,
    task: AddTask,
    isLiked: Boolean,
    onClick: () -> Unit,
    onCompleteClick: () -> Unit,
    onToggleClick: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(
                onClick = onClick
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, top = 8.dp, end = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = task.title,
                style = RobotoRegularWithHEX31394f20Sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            )

            if (selectedDate == LocalDate.now()) {
                val coroutineScope = rememberCoroutineScope()

                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            color = if (isLiked) {
                                colorResource(id = R.color.A90EE90)
                            } else {
                                colorResource(id = R.color.Aefefef)
                            },
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            onToggleClick(!isLiked) // Toggle the liked state
                            if (!isLiked) {
                                coroutineScope.launch {
                                    delay(500)
                                    onCompleteClick()
                                }
                            }
                        }
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedCompleted(isLiked = isLiked)
                }
            }
        }
        if (task.description.isNotEmpty()) {
            Text(
                text = task.description,
                style = RobotoRegularWithHEX31394f18Sp,
                modifier = Modifier.padding(start = 32.dp, top = 8.dp)
            )
        }

        convertTo12HourFormat(task.startTime)?.let {
            Text(
                text = it,
                style = RobotoRegularWithHEX31394f18Sp,
                modifier = Modifier
                    .padding(start = 32.dp, top = 8.dp, end = 16.dp)
            )
        }

        Text(
            text = task.type,
            style = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.A989ba2),
            modifier = Modifier
                .padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
        )

        Spacer(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(start = 32.dp, end = 32.dp),
            color = colorResource(id = R.color.A989ba2)
        )
        Spacer(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
        )
    }
}


fun convertTo12HourFormat(time: String): String? {
    val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val date = inputFormat.parse(time)
    return date?.let { outputFormat.format(it) }
}


@Composable
fun CompletedTaskItem(
    selectedDate: LocalDate,
    task: CompletedTask,
    onClick: () -> Unit

) {
    Column(
        modifier = Modifier
            .clickable(
                onClick = onClick
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, top = 8.dp, end = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = task.title,
                  style = RobotoRegularWithHEX31394f20Sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            )
            if (selectedDate == LocalDate.now()) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            color = colorResource(id = R.color.A90EE90),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Check mark",
                        tint = Color.White,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

        }

        if (task.description.isNotEmpty()){
            Text(
                text = task.description,
                style = RobotoRegularWithHEX31394f18Sp,
                modifier = Modifier.padding(start = 32.dp, top = 8.dp)
            )
        }


        convertTo12HourFormat(task.startTime)?.let {
            Text(
                text = it,
                style = RobotoRegularWithHEX31394f18Sp,
                modifier = Modifier
                    .padding(start = 32.dp, top = 8.dp,end = 16.dp)
            )
        }

            Text(
                text = task.type,
                style = RobotoItalicWithHEX989ba214sp,
                modifier = Modifier
                    .padding(start = 32.dp, top = 8.dp, bottom = 4.dp)
            )

        Spacer(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(start = 32.dp, end = 32.dp),
            color = colorResource(id = R.color.A989ba2)
        )
        Spacer(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
        )

    }
}



@Composable
fun SkippedTaskItem(
    selectedDate: LocalDate,
    task: SkippedTask,
    isLiked: Boolean,
    onClick: () -> Unit,
    onCompleteClick: () -> Unit,
    onToggleClick: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, top = 8.dp, end = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = task.title,
                style = RobotoRegularWithHEX31394f20Sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            )

            if (selectedDate == LocalDate.now()) {
                val coroutineScope = rememberCoroutineScope()

                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            color = if (isLiked) {
                                colorResource(id = R.color.A90EE90)
                            } else {
                                colorResource(id = R.color.Aefefef)
                            },
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            onToggleClick(!isLiked)
                            if (!isLiked) {
                                coroutineScope.launch {
                                    delay(500)
                                    onCompleteClick()
                                }
                            }
                        }
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedCompleted(isLiked = isLiked)
                }
            }
        }

        if (task.description.isNotEmpty()) {
            Text(
                text = task.description,
                style = RobotoRegularWithHEX31394f18Sp,
                modifier = Modifier.padding(start = 32.dp, top = 8.dp)
            )
        }

        convertTo12HourFormat(task.startTime)?.let {
            Text(
                text = it,
                style = RobotoRegularWithHEX31394f18Sp,
                modifier = Modifier
                    .padding(start = 32.dp, top = 8.dp, end = 16.dp)
            )
        }

        Text(
            text = task.type,
            style = RobotoItalicWithHEX989ba214sp,
            modifier = Modifier
                .padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
        )

        Spacer(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(start = 32.dp, end = 32.dp),
            color = colorResource(id = R.color.A989ba2)
        )
        Spacer(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
        )
    }
}


@Composable
fun AnimatedCompleted(
    isLiked: Boolean
) {
    if (isLiked){
        val scale = remember { Animatable(1f) }
        val alpha = remember { Animatable(1f) }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(isLiked) {

            coroutineScope.launch {
                scale.animateTo(
                    targetValue = 1.5f,
                    animationSpec = tween(durationMillis = 300)
                )
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 200)
                )
            }
        }

        Box(
            modifier = Modifier
                .size(25.dp)
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    alpha = alpha.value
                )
                .zIndex(1f)
                .background(
                    color = colorResource(id = R.color.A90EE90) ,
                    shape = RoundedCornerShape(50)
                )
                .padding(2.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Check mark",
                tint = Color.White,
                modifier = Modifier.fillMaxSize()
                    .zIndex(1f)
            )
        }
    }

}