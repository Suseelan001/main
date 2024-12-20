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
import androidx.compose.material3.MaterialTheme
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

@AndroidEntryPoint
class BaseActivity : ComponentActivity() {
    private var isHomeScreenLaunchedFirstTime = true
    private val mainViewModel = MainViewmodel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeBaseFunction()
        }
    }



    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RestrictedApi")
    private fun ComposeBaseFunction() {
        val navController: NavHostController = rememberNavController()
        val buttonsVisible = remember { mutableStateOf(false) }
        var isSheetVisible by remember { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState()
        var isBottomBarVisible by remember { mutableStateOf(false) }


        navController.addOnDestinationChangedListener { _, destination, _ ->
            buttonsVisible.value =
                destination.hasRoute(NavRote.HomeScreen.path, null) ||
                        destination.hasRoute(NavRote.ChartScreen.path, null) ||
                        destination.hasRoute(NavRote.ProfileScreen.path, null) ||
                        destination.hasRoute(NavRote.SettingsScreen.path, null)

            if (destination.hasRoute(NavRote.HomeScreen.path, null)) {
                if (isHomeScreenLaunchedFirstTime) {
                    isHomeScreenLaunchedFirstTime = false
                }
            }


        }
        LaunchedEffect(Unit) {
          //  delay(1200L)
            isBottomBarVisible = true
        }
        Scaffold(
            bottomBar = {
                Column {

                    if (isBottomBarVisible) {
                        BottomBar(
                            navController = navController,
                            isBottomBarVisible = buttonsVisible,
                            onFabClick = {
                                isSheetVisible=true
                            }
                        )


                    }
                }
            }
        ) {
            NavigationGraph(navController, mainViewModel)
        }
        if (isSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = { isSheetVisible = false },
                sheetState = sheetState
            ) {
                // Bottom Sheet content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center


                ) {
                    Text(
                        text = "Add Activity",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        isSheetVisible = false
                        navController.navigate("${NavRote.AddTaskScreen.path}/${"Task"}/${"0"}/${"add"}")

                    }) {
                        Text("Add Task")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        isSheetVisible = false
                        navController.navigate("${NavRote.AddTaskScreen.path}/${"Tracker"}/${"0"}/${"add"}")

                    }) {
                        Text("Add Tracker")
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                }
            }
        }




    }



}
