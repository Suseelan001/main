package com.example.reminderhabit.view


import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.reminderhabit.R
import com.example.reminderhabit.viewmodel.MainViewmodel
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.reminderhabit.model.AddTask
import com.example.reminderhabit.ui.theme.HEX787878
import com.example.reminderhabit.ui.theme.HEX7981ff
import com.example.reminderhabit.ui.theme.RobotoRegularWithHEX45454518sp
import com.example.reminderhabit.viewmodel.CompletedTaskViewModel
import com.example.reminderhabit.viewmodel.SkippedTaskViewModel
import com.example.reminderhabit.viewmodel.TaskViewModel
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    recordType: String,
    edit: String,
    taskId: String,
    type: String,
    navHostController: NavHostController,
    taskViewModel: TaskViewModel,
    mainViewModel: MainViewmodel,
    backLogViewModel: SkippedTaskViewModel,
    completedTaskViewModel: CompletedTaskViewModel
)  {
    var isChecked by remember { mutableStateOf(true) }
    var screentitle by remember { mutableStateOf("") }
    var recordTypeName by remember { mutableStateOf("") }
    var habitName by remember { mutableStateOf("") }
    var descrptiontxt by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    val daysOfWeek = listOf( "Sun","Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    var selectedDays by rememberSaveable { mutableStateOf(daysOfWeek.toSet()) }
    val pushenabled by remember { mutableStateOf(true) }
    var showDatePicker by remember { mutableStateOf(false) }
    val mContext = LocalContext.current
    var selectedColor by remember { mutableStateOf("blue") }
    var selectedDatefortask by remember { mutableStateOf("") }
    var showTimePicker by remember { mutableStateOf(false) }
    var isStartTimePicker by remember { mutableStateOf(true) }
    val timePickerState = rememberTimePickerState()
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    BackHandler {
        navHostController.popBackStack()
    }

    screentitle = if (edit == "Edit"){
        "Edit $type"
    }else{
        "Add $type"

    }

    recordTypeName=recordType

    val taskLoaded = remember { mutableStateOf(false) }

    if (recordType == "Completed"){
        val getCompletedRecord by completedTaskViewModel.getSingleRecord(taskId.toInt()).observeAsState()
        if (getCompletedRecord != null && !taskLoaded.value) {
            if (type == "Tracker" || type == "Task") {
                habitName = getCompletedRecord?.title ?: ""
                descrptiontxt = getCompletedRecord?.description ?: ""
                startTime = getCompletedRecord?.startTime?.let { convertTo12HourFormat(it) } ?: ""
                selectedDays = getCompletedRecord?.days?.toSet() ?: daysOfWeek.toSet()
                selectedColor = getCompletedRecord?.color ?: "blue"
                selectedDatefortask = getCompletedRecord?.createdDate ?: ""
            }
            taskLoaded.value = true
        }

    }else
        if(recordType == "Backlog"){
            val getBackLogRecords by backLogViewModel.getSingleRecord(taskId.toInt()).observeAsState()
            if (getBackLogRecords != null && !taskLoaded.value) {
                if (type == "Tracker" || type == "Task") {
                    habitName = getBackLogRecords?.title ?: ""
                    descrptiontxt = getBackLogRecords?.description ?: ""
                    startTime = getBackLogRecords?.startTime?.let { convertTo12HourFormat(it) } ?: ""
                    selectedDays = getBackLogRecords?.days?.toSet() ?: daysOfWeek.toSet()
                    selectedColor = getBackLogRecords?.color ?: "blue"
                    selectedDatefortask = getBackLogRecords?.createdDate ?: ""
                }
                taskLoaded.value = true
            }

        }else if (recordType == "ToDo"){
            val allTasks by taskViewModel.getSingleRecord(taskId.toInt()).observeAsState()
            if (allTasks != null && !taskLoaded.value) {
                if (type == "Tracker" || type == "Task") {
                    habitName = allTasks?.title ?: ""
                    descrptiontxt = allTasks?.description ?: ""
                    startTime = allTasks?.startTime?.let { convertTo12HourFormat(it) } ?: ""
                    selectedDays = allTasks?.days?.toSet() ?: daysOfWeek.toSet()
                    selectedColor = allTasks?.color ?: "blue"
                    selectedDatefortask = allTasks?.createdDate ?: ""
                }
                taskLoaded.value = true
            }

        }


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
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    navHostController.popBackStack()
                }) {
                    AsyncImage(
                        model = R.drawable.baseline_chevron_left_24,
                        contentDescription = "Back Icon",
                    )
                }
            },
           /* actions = {
                IconButton(onClick = {
                    when (recordTypeName) {
                        "Backlog" -> {
                            taskId.toIntOrNull()?.let {
                                backLogViewModel.deleteSingleRecord(it)
                            }
                        }
                        "ToDo" -> {
                            taskId.toIntOrNull()?.let {
                                taskViewModel.deleteSingleRecord(it)
                            }
                        }
                        "Completed" -> {
                            taskId.toIntOrNull()?.let {
                                completedTaskViewModel.deleteSingleRecord(it)
                            }
                        }
                        else -> {
                            println("Unknown record type")
                        }
                    }

                }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_delete_24),
                        contentDescription = "Delete Icon"
                    )
                }
            },*/
            modifier = Modifier
                .fillMaxWidth(),
        )



        Text(
            text = "Add Habit Name",
            style = RobotoRegularWithHEX45454518sp,
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp)
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .border(1.dp, HEX787878, RoundedCornerShape(8.dp)),
            value = habitName,
            onValueChange = { habitName = it },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                cursorColor = Color.Black,
                disabledLabelColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )


        Text(
            text = "Habit Description",
            style = RobotoRegularWithHEX45454518sp,
            modifier = Modifier
                .padding(top = 32.dp, start = 16.dp)
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .border(1.dp, HEX787878, RoundedCornerShape(8.dp)),
            value = descrptiontxt,
            onValueChange = { descrptiontxt = it },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                cursorColor = Color.Black,
                disabledLabelColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = "Add Date",
                    style = RobotoRegularWithHEX45454518sp,
                    modifier = Modifier.padding(top = 32.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .border(1.dp, Color(0xFF787878), RoundedCornerShape(8.dp))
                        .clickable { showDatePicker = true }
                ) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = selectedDatefortask,
                        onValueChange = {},
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,
                            cursorColor = Color.Black,
                            disabledLabelColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(8.dp),
                        readOnly = true,
                        singleLine = true,
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                                    contentDescription = "Calendar"
                                )
                            }
                        }
                    )
                }


            }

            if (showDatePicker) {
                val currentCalendar = Calendar.getInstance()
                val year = currentCalendar.get(Calendar.YEAR)
                val month = currentCalendar.get(Calendar.MONTH)
                val dayOfMonth = currentCalendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = android.app.DatePickerDialog(
                    mContext,
                    { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                        val date = "$selectedYear-${selectedMonth + 1}-$selectedDayOfMonth"
                        selectedDatefortask=date
                        showDatePicker = false
                    },
                    year, month, dayOfMonth
                )


                val datePicker = datePickerDialog.datePicker
                datePicker.minDate = currentCalendar.timeInMillis

                datePickerDialog.setOnCancelListener {
                    showDatePicker = false
                }

                datePickerDialog.show()
            }

            Spacer(modifier = Modifier.width(25.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = "Add Time",
                    style = RobotoRegularWithHEX45454518sp,
                    modifier = Modifier.padding(top = 32.dp)
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .border(1.dp, Color(0xFF787878), RoundedCornerShape(8.dp)),
                    value = startTime,
                    onValueChange = {},
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        cursorColor = Color.Black,
                        disabledLabelColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    readOnly = true,
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            isStartTimePicker = true
                            showTimePicker = true
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.clock_24),
                                contentDescription = "Select Start Time"
                            )
                        }
                    }
                )

                if (showTimePicker) {
                    TimePickerDialog(
                        onCancel = { showTimePicker = false },
                        onConfirm = {
                            val calendar = Calendar.getInstance()
                            calendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                            calendar.set(Calendar.MINUTE, timePickerState.minute)
                            val selectedTime = formatter.format(calendar.time)

                            if (isStartTimePicker) {
                                startTime=selectedTime
                            }

                            showTimePicker = false
                        }
                    ) {
                        TimePicker(state = timePickerState)
                    }
                }


            }
        }

        if (screentitle.contains("Tracker")){


        Text(
            text = "Days Of week",
            style = RobotoRegularWithHEX45454518sp,
            modifier = Modifier.padding(top = 32.dp, start = 16.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            items(daysOfWeek) { date ->
                val isSelected = date in selectedDays

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            color = if (isSelected) HEX7981ff else Color.Transparent
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) Color.Transparent else Color.Gray,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            selectedDays = if (isSelected) {
                                selectedDays - date
                            } else {
                                selectedDays + date
                            }
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)

                    ) {
                        Text(
                            text = date,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                            color = if (isSelected) Color.White else Color.Black
                        )
                    }
                }

            }
        }

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 32.dp)
        ) {
            Text(text = "Enable Push Notifications", style = MaterialTheme.typography.bodyLarge)

            Switch(
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    when {
                        habitName.isEmpty() -> {
                            Toast.makeText(mContext, "Enter habit name", Toast.LENGTH_SHORT).show()
                        }

                        selectedDays.isEmpty() -> {
                            Toast.makeText(
                                mContext,
                                "Please select days for habit",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        startTime.isEmpty() -> {
                            Toast.makeText(mContext, "Please select start time", Toast.LENGTH_SHORT)
                                .show()
                        }


                        else -> {
                            val createdDateString = if (type == "Tracker") {
                                if (screentitle.contains("Edit")){
                                    selectedDatefortask.toString()
                                }else{
                                    mainViewModel.selectedDate?.format(DateTimeFormatter.ISO_DATE)
                                        ?: LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                                }

                            } else {
                                selectedDays = setOf("")
                                selectedDatefortask
                            }
                            println("CHECK_TAG_selectedDatefortask   " + selectedDatefortask)
                            println("CHECK_TAG_createdDateString_   " + createdDateString)


                            val task = convertTo24HourFormat(startTime)?.let {
                                    AddTask(
                                        title = habitName,
                                        description = descrptiontxt,
                                        days = ArrayList(selectedDays),
                                        color = selectedColor,
                                        isNotificationEnabled = pushenabled,
                                        startTime = it,
                                        createdDate = createdDateString.toString(),
                                        type = type
                                    )
                            }

                            if (screentitle.contains("Edit", ignoreCase = true)) {

                                println("CHECK_TAG_TASK__ " + Gson().toJson(task))

                                if (task != null) {
                                    taskViewModel.insertTask(task)
                                }


                                when (recordTypeName) {
                                    "Backlog" -> {
                                        taskId.toIntOrNull()?.let {
                                            backLogViewModel.deleteSingleRecord(it)
                                        }
                                    }
                                    "ToDo" -> {
                                        taskId.toIntOrNull()?.let {
                                            taskViewModel.deleteSingleRecord(it)
                                        }
                                    }
                                    "Completed" -> {
                                        taskId.toIntOrNull()?.let {
                                            completedTaskViewModel.deleteSingleRecord(it)
                                        }
                                    }
                                    else -> println("CHECK_TAG_ Unknown Record Type")
                                }


                                Toast.makeText(
                                    mContext,
                                    "Your $type has been updated",
                                    Toast.LENGTH_SHORT
                                ).show()


                            } else {


                                if (task != null) {
                                    taskViewModel.insertTask(task)
                                }
                                Toast.makeText(
                                    mContext,
                                    "Your $type has been added",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            navHostController.popBackStack()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(HEX7981ff)
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






fun convertTo24HourFormat(time: String): String? {
    val inputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val date = inputFormat.parse(time)
    return date?.let { outputFormat.format(it) }
}