package com.example.reminderhabit.bottomnavigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.reminderhabit.view.AddTaskScreen
import com.example.reminderhabit.view.ChartScreen
import com.example.reminderhabit.view.HomeScreen
import com.example.reminderhabit.view.LoginScreenWithConstraintLayout
import com.example.reminderhabit.view.PermissionScreen
import com.example.reminderhabit.view.ProfileScreen
import com.example.reminderhabit.view.SettingsScreen
import com.example.reminderhabit.view.SignupScreen
import com.example.reminderhabit.view.SplashScreen
import com.example.reminderhabit.view.TaskListScreen
import com.example.reminderhabit.viewmodel.CompletedTaskViewModel
import com.example.reminderhabit.viewmodel.MainViewmodel
import com.example.reminderhabit.viewmodel.SkippedTaskViewModel
import com.example.reminderhabit.viewmodel.TaskViewModel
import com.example.reminderhabit.viewmodel.UserViewModel
import com.example.reminderhabit.viewmodel.SharedPreferenceViewModel


@Composable
fun NavigationGraph(navHostController:NavHostController,mainViewModel: MainViewmodel) {
    NavHost(navController = navHostController, startDestination = NavRote.SplashScreen.path, Modifier.fillMaxSize(),
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(200)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(200)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(200)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(200)
            )
        }) {

        composable(route = NavRote.SplashScreen.path) {

            val parentEntry = remember(it) {
                navHostController.getBackStackEntry(NavRote.SplashScreen.path)
            }
            val viewModelRefUserProfileVM = hiltViewModel<SharedPreferenceViewModel>(parentEntry)

            SplashScreen(viewModelRefUserProfileVM,navHostController)
        }

        composable(route = NavRote.LoginScreen.path) {

            val parentEntry = remember(it) {
                navHostController.getBackStackEntry(NavRote.LoginScreen.path)
            }
            val viewModelRefUserProfileVM = hiltViewModel<UserViewModel>(parentEntry)
            val sharedPreferenceViewModel = hiltViewModel<SharedPreferenceViewModel>(parentEntry)

            LoginScreenWithConstraintLayout(navHostController, sharedPreferenceViewModel,viewModelRefUserProfileVM)
        }

        composable(route = NavRote.SignupScreen.path) {

            val viewModelRefUserProfileVM = hiltViewModel<UserViewModel>()

            SignupScreen(navHostController,mainViewModel,viewModelRefUserProfileVM)
        }
        composable(route = NavRote.HomeScreen.path) {
            val taskViewmodel = hiltViewModel<TaskViewModel>()
            val skippedtaskViewmodel = hiltViewModel<SkippedTaskViewModel>()
            val completedTaskViewModel = hiltViewModel<CompletedTaskViewModel>()
            val userViewModel = hiltViewModel<UserViewModel>()
            val sharedPreferenceViewModel = hiltViewModel<SharedPreferenceViewModel>()

            HomeScreen(navHostController,mainViewModel,taskViewmodel,skippedtaskViewmodel,completedTaskViewModel,userViewModel,sharedPreferenceViewModel)
        }
        composable(route = NavRote.ChartScreen.path) {
            ChartScreen(navHostController,mainViewModel)
        }

        composable(route = NavRote.ProfileScreen.path) {
            val userViewModel = hiltViewModel<UserViewModel>()
            val sharedPreferenceViewModel = hiltViewModel<SharedPreferenceViewModel>()

            ProfileScreen(navHostController,sharedPreferenceViewModel,userViewModel)
        }

        composable(route = NavRote.SettingsScreen.path) {
            val userViewModel = hiltViewModel<UserViewModel>()
            SettingsScreen(navHostController,userViewModel)
        }
 /*       composable(route = NavRote.TaskListScreen.path) {
            val taskViewmodel = hiltViewModel<TaskViewModel>()

            TaskListScreen(navHostController,taskViewmodel)
        }*/

        composable(route = NavRote.TaskListScreen.path+"/{type}",
            arguments = listOf(navArgument("type"){type = NavType.StringType})
        ) {
            val taskViewmodel = hiltViewModel<TaskViewModel>()
            val type = it.arguments?.getString("type")
            if (type != null) {
                TaskListScreen(type,navHostController,taskViewmodel,mainViewModel)
            }
        }


        composable(
            route = NavRote.AddTaskScreen.path + "/{type}/{taskId}/{edit}/{recordType}",
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("taskId") { type = NavType.StringType },
                navArgument("edit") { type = NavType.StringType },
                navArgument("recordType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val taskViewModel = hiltViewModel<TaskViewModel>()
            val completedViewModel = hiltViewModel<CompletedTaskViewModel>()
            val backLogViewModel = hiltViewModel<SkippedTaskViewModel>()

            val type = backStackEntry.arguments?.getString("type") ?: ""
            val edit = backStackEntry.arguments?.getString("edit") ?: ""
            val taskId = backStackEntry.arguments?.getString("taskId") ?: "0"
            val recordType = backStackEntry.arguments?.getString("recordType") ?: ""

            AddTaskScreen(
                recordType = recordType,
                edit = edit,
                taskId = taskId,
                type = type,
                navHostController = navHostController,
                taskViewModel = taskViewModel,
                mainViewModel = mainViewModel,
                backLogViewModel = backLogViewModel,
                completedTaskViewModel = completedViewModel
            )
        }


        composable(route = NavRote.PermissionScreen.path) {
            PermissionScreen(navHostController,mainViewModel)
        }





    }
}