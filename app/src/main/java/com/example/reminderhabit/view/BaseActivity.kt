package com.example.reminderhabit.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.reminderhabit.bottomnavigation.BottomBar
import com.example.reminderhabit.bottomnavigation.NavRote
import com.example.reminderhabit.bottomnavigation.NavigationGraph
import com.example.reminderhabit.viewmodel.MainViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


@AndroidEntryPoint
class BaseActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeBaseFunction()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    private fun ComposeBaseFunction() {
        val navController = rememberNavController()
        val buttonsVisible = remember { mutableStateOf(false) }
        var isSheetVisible by remember { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState()

        val isHomeScreenLaunchedFirstTime = remember { mutableStateOf(true) }

        LaunchedEffect(navController) {
            navController.currentBackStackEntryFlow.collect { backStackEntry ->
                val route = backStackEntry.destination.route
                buttonsVisible.value = route in listOf(
                    NavRote.HomeScreen.path,
                    NavRote.ChartScreen.path,
                    NavRote.ProfileScreen.path,
                    NavRote.SettingsScreen.path
                )
                if (route == NavRote.HomeScreen.path && isHomeScreenLaunchedFirstTime.value) {
                    isHomeScreenLaunchedFirstTime.value = false
                }
            }
        }

        Scaffold(
            bottomBar = {
                if (buttonsVisible.value) {
                    BottomBar(
                        navController = navController,
                        isBottomBarVisible = buttonsVisible,
                        onFabClick = { isSheetVisible = true }
                    )
                }
            }
        ) {
            NavigationGraph(navController,MainViewmodel())
        }

        if (isSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = { isSheetVisible = false },
                sheetState = sheetState
            ) {
                BottomSheetContent(
                    onTaskClick = {
                        isSheetVisible = false
                        navController.navigate("${NavRote.AddTaskScreen.path}/Task/0/add/add")
                    },
                    onTrackerClick = {
                        isSheetVisible = false
                        navController.navigate("${NavRote.AddTaskScreen.path}/Tracker/0/add/add")
                    }
                )
            }
        }
    }

    @Composable
    fun BottomSheetContent(onTaskClick: () -> Unit, onTrackerClick: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onTaskClick) { Text("Add Task") }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onTrackerClick) { Text("Add Tracker") }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
