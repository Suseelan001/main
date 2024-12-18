package com.example.reminderhabit.view


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.reminderhabit.R
import com.example.reminderhabit.TittleTextView
import com.example.reminderhabit.viewmodel.MainViewmodel
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.Dimension
import com.example.reminderhabit.model.AddTask
import com.example.reminderhabit.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    edit:String,
    taskId:String,
    type: String,
    navHostController: NavHostController,
    taskViewModel: TaskViewModel,
    mainViewmodel: MainViewmodel
) {
    var screentitle by remember { mutableStateOf("") }
    var habitName by remember { mutableStateOf("") }
    var descrptiontxt by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    var selectedDays by rememberSaveable { mutableStateOf(daysOfWeek.toSet()) }
    var pushenabled by remember { mutableStateOf(true) }
    val context = LocalContext.current
    var selectedColor by remember { mutableStateOf("blue") }
    var selectedDatefortask by remember { mutableStateOf("") }
    if (edit.equals("Edit")){
        screentitle = "Edit $type"
    }else{
        screentitle="Add $type"

    }

    val allTasks by taskViewModel.getSingleRecord(taskId.toInt()).observeAsState()
    val taskLoaded = remember { mutableStateOf(false) }

    if (allTasks != null && !taskLoaded.value) {
        if (type == "Tracker" || type == "Task") {
            habitName = allTasks?.title ?: ""
            descrptiontxt = allTasks?.description ?: ""
            startTime = allTasks?.startTime ?: ""
            endTime = allTasks?.endTime ?: ""
            selectedDays = allTasks?.days?.toSet() ?: daysOfWeek.toSet()
            selectedColor = allTasks?.color ?: "blue"
            selectedDatefortask = allTasks?.createdDate ?: ""
        }
        taskLoaded.value = true
    }

    val displayedText = if (selectedDays.size == daysOfWeek.size) {
        "Every Day"
    } else {
        selectedDays.joinToString(", ")
    }




    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.White
            ),
            title = {
                Text(
                    text = screentitle,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    navHostController.popBackStack()
                }) {
                    AsyncImage(
                        model = R.drawable.baseline_arrow_back_24,
                        contentDescription = "Back Icon",
                    )
                }
            },
            actions = {
                if (screentitle.contains("edit", ignoreCase = true)) {
                    IconButton(onClick = {
                        if (taskId.isNotEmpty()) {
                            taskViewModel.deleteSingleRecord(taskId.toInt())
                            navHostController.popBackStack()
                            Toast.makeText(
                                context,
                                "Your $type has been deleted",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    }) {
                        AsyncImage(
                            model = R.drawable.baseline_delete_24,
                            contentDescription = "Save Icon",
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp),
        )
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(scrollState)

        ) {
            val (habitField, habitDays, dateselecter, dayslist, dayRow, colorRow, selectTime, enablepush, descrptionTxt, adddescription, savebutton) = createRefs()



            TextField(
                value = habitName,
                onValueChange = { habitName = it },
                textStyle = TextStyle(fontSize = 24.sp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .constrainAs(habitField) {
                        top.linkTo(parent.top, margin = 32.dp)
                    }
                    .fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Enter your Habit here",
                        color = Color.Gray
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.LightGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(6.dp)
            )

            TittleTextView(
                text = "Habit Days",
                modifier = Modifier.constrainAs(habitDays) {
                    top.linkTo(habitField.bottom, margin = 32.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                },
                fontSize = 24
            )

            if (type == "Tracker") {
                if (displayedText.isNotEmpty()) {
                    TittleTextView(
                        text = displayedText,
                        modifier = Modifier.constrainAs(dayslist) {
                            top.linkTo(habitDays.bottom, margin = 16.dp)
                            start.linkTo(parent.start, margin = 16.dp)
                        },
                        fontSize = 16
                    )
                } else {
                    Spacer(
                        modifier = Modifier
                            .constrainAs(dayslist) {
                                top.linkTo(habitDays.bottom, margin = 16.dp)
                                start.linkTo(parent.start, margin = 16.dp)
                            }
                            .height(24.dp)
                    )
                }

                LazyRow(
                    modifier = Modifier
                        .constrainAs(dayRow) {
                            top.linkTo(
                                if (displayedText.isNotEmpty()) dayslist.bottom else habitDays.bottom,
                                margin = 16.dp
                            )
                            start.linkTo(parent.start, margin = 16.dp)
                            end.linkTo(parent.end)
                        }
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(daysOfWeek) { _, day ->
                        val isSelected = day in selectedDays

                        Card(
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    selectedDays = if (isSelected) {
                                        selectedDays - day
                                    } else {
                                        selectedDays + day
                                    }
                                },
                            shape = MaterialTheme.shapes.large,
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Color.Blue else Color.LightGray
                            ),
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = day,
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else Color.Black
                                    )
                                )
                            }
                        }
                    }
                }

            } else {

                DatePickerTextField(
                    selectedDate = selectedDatefortask,
                    onDateSelected = { newDate ->
                        selectedDatefortask = newDate
                    },
                    modifier = Modifier.constrainAs(dateselecter) {
                        top.linkTo(habitDays.bottom, margin = 32.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
                )


            }



            if (type == "Tracker") {
                TimePicker(
                    startTime = startTime,
                    endTime = endTime,
                    onStartTimeSelected = { time -> startTime = time },
                    onEndTimeSelected = { time -> endTime = time },
                    modifier = Modifier.constrainAs(selectTime) {
                        top.linkTo(dayRow.bottom, margin = 32.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
                )
            } else {
                TimePicker(
                    startTime = startTime,
                    endTime = endTime,
                    onStartTimeSelected = { time -> startTime = time },
                    onEndTimeSelected = { time -> endTime = time },
                    modifier = Modifier.constrainAs(selectTime) {
                        top.linkTo(dateselecter.bottom, margin = 32.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
                )
            }




            ColorRow(
                defaultColor = selectedColor,
                onColorSelected = { colorName ->
                    selectedColor = colorName
                },
                modifier = Modifier.constrainAs(colorRow) {
                    top.linkTo(selectTime.bottom, margin = 32.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
            )

            NotificationToggleWithTimePicker(
                isNotificationEnabled = pushenabled,
                onNotificationToggle = { pushenabled = it },
                modifier = Modifier.constrainAs(enablepush) {
                    top.linkTo(colorRow.bottom, margin = 32.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
            )


            TittleTextView(
                text = "Description",
                modifier = Modifier.constrainAs(descrptionTxt) {
                    top.linkTo(enablepush.bottom, margin = 32.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                },
                fontSize = 24
            )

            TextField(
                value = descrptiontxt,
                onValueChange = { descrptiontxt = it },
                textStyle = TextStyle(fontSize = 24.sp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .constrainAs(adddescription) {
                        top.linkTo(descrptionTxt.bottom, margin = 32.dp)
                    }

                    .fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Write your description",
                        color = Color.Gray
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.LightGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(6.dp)
            )

            Button(
                onClick = {
                    when {
                        habitName.isEmpty() -> {
                            Toast.makeText(context, "Enter habit name", Toast.LENGTH_SHORT).show()
                        }

                        selectedDays.isEmpty() -> {
                            Toast.makeText(
                                context,
                                "Please select days for habit",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        startTime.isEmpty() -> {
                            Toast.makeText(context, "Please select start time", Toast.LENGTH_SHORT)
                                .show()
                        }

                        endTime.isEmpty() -> {
                            Toast.makeText(context, "Please select end time", Toast.LENGTH_SHORT)
                                .show()
                        }

                        else -> {
                            val createdDateString = if (type == "Tracker") {
                                mainViewmodel.selectedDate?.format(DateTimeFormatter.ISO_DATE)
                                    ?: LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                            } else {
                                selectedDatefortask
                                selectedDays = setOf("")
                                selectedDatefortask
                            }

                            val task = AddTask(
                                title = habitName,
                                description = descrptiontxt,
                                days = ArrayList(selectedDays),
                                color = selectedColor,
                                isNotificationEnabled = pushenabled,
                                startTime = convertTo24HourFormat(startTime),
                                endTime = convertTo24HourFormat(endTime),
                                createdDate = createdDateString,
                                type = type
                            )


                            if (screentitle.contains("Edit", ignoreCase = true)) {
                                val updatedTask = task.copy(id = taskId.toInt())
                                taskViewModel.updateRecord(updatedTask)
                                Toast.makeText(
                                    context,
                                    "Your $type has been updated",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                taskViewModel.insertTask(task)
                                Toast.makeText(
                                    context,
                                    "Your $type has been added",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            navHostController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.constrainAs(savebutton) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(adddescription.bottom, margin = 32.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    width = Dimension.wrapContent
                }
            ) {
                Text(
                    text = if (screentitle.contains(
                            "Edit",
                            ignoreCase = true
                        )
                    ) "Update" else "Save"
                )
            }


        }
    }

}

fun convertTo24HourFormat(time: String): String {
    val inputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val date = inputFormat.parse(time)
    return outputFormat.format(date)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(
    startTime: String,
    endTime: String,
    onStartTimeSelected: (String) -> Unit,
    onEndTimeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showTimePicker by remember { mutableStateOf(false) }
    var isStartTimePicker by remember { mutableStateOf(true) } // To distinguish which picker is open

    val timePickerState = rememberTimePickerState()
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = startTime,
                onValueChange = {},
                label = { Text("Start Time") },
                readOnly = true,
                modifier = Modifier.weight(1f),
                trailingIcon = {
                    IconButton(onClick = {
                        isStartTimePicker = true
                        showTimePicker = true
                    }) {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Select Start Time")
                    }
                }
            )

            TextField(
                value = endTime,
                onValueChange = {},
                label = { Text("End Time") },
                readOnly = true,
                modifier = Modifier.weight(1f),
                trailingIcon = {
                    IconButton(onClick = {
                        isStartTimePicker = false
                        showTimePicker = true
                    }) {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Select End Time")
                    }
                }
            )
        }

        if (showTimePicker) {
            TimePickerDialog(
                onCancel = { showTimePicker = false },
                onConfirm = {
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    calendar.set(Calendar.MINUTE, timePickerState.minute)
                    val selectedTime = formatter.format(calendar.time)

                    if (isStartTimePicker) {
                        onStartTimeSelected(selectedTime)
                    } else {
                        onEndTimeSelected(selectedTime)
                    }

                    showTimePicker = false
                }
            ) {
                TimePicker(state = timePickerState)
            }
        }
    }
}


@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            toggle()
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = onCancel
                    ) { Text("Cancel") }
                    TextButton(
                        onClick = onConfirm
                    ) { Text("OK") }
                }
            }
        }
    }
}


@Composable
fun NotificationToggleWithTimePicker(
    isNotificationEnabled: Boolean,
    onNotificationToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Enable Push Notifications", style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = isNotificationEnabled,
                onCheckedChange = onNotificationToggle
            )
        }
    }
}


@Composable
fun ColorRow(
    modifier: Modifier = Modifier,
    defaultColor: String = "blue",
    onColorSelected: (String) -> Unit
) {
    val colorsMap = mapOf(
        "red" to Color.Red,
        "green" to Color.Green,
        "blue" to Color.Blue,
        "yellow" to Color.Yellow,
        "magenta" to Color.Magenta
    )

    var selectedColor by remember { mutableStateOf(defaultColor) }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            colorsMap.forEach { (colorName, colorValue) ->
                val isSelected = selectedColor == colorName
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = if (isSelected) colorValue else colorValue.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            selectedColor = colorName
                            onColorSelected(colorName)
                        }
                        .border(
                            width = if (isSelected) 2.dp else 0.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(50)
                        )
                )
            }
        }
    }
}

@Composable
fun DatePickerTextField(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val mContext = LocalContext.current
    if (showDatePicker) {
        // Get the current date
        val currentCalendar = Calendar.getInstance()
        val year = currentCalendar.get(Calendar.YEAR)
        val month = currentCalendar.get(Calendar.MONTH)
        val dayOfMonth = currentCalendar.get(Calendar.DAY_OF_MONTH)

        // Create DatePickerDialog
        val datePickerDialog = android.app.DatePickerDialog(
            mContext,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                // Format the selected date
                val date = "$selectedYear-${selectedMonth + 1}-$selectedDayOfMonth"
                onDateSelected(date)
                showDatePicker = false
            },
            year, month, dayOfMonth
        )

        // Set the date picker to disable past dates
        val datePicker = datePickerDialog.datePicker
        datePicker.minDate = currentCalendar.timeInMillis

        // Show the date picker dialog
        datePickerDialog.show()
    }

    // Date field UI
    TextField(
        value = selectedDate,
        onValueChange = {},
        label = { Text("Select Date") },
        readOnly = true,
        modifier = modifier
            .clickable { showDatePicker = true },
        trailingIcon = {
            IconButton(onClick = {
                showDatePicker = true
            }) {
                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Select Date")
            }
        }
    )
}



