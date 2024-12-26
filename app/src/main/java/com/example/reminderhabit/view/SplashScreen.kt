package com.example.reminderhabit.view


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.reminderhabit.bottomnavigation.NavRote
import com.example.reminderhabit.viewmodel.SharedPreferenceViewModel


@Composable
fun SplashScreen(
    sharedPreferenceViewModel: SharedPreferenceViewModel,
    navHostController: NavHostController?
) {
    LaunchedEffect(Unit) {
        println("CHECK_TAG__sharedPreferenceViewModel " + sharedPreferenceViewModel.getUserLoggedIn())
        if (sharedPreferenceViewModel.getUserLoggedIn()) {
            callHomeScreen(navHostController)
        } else {
            callLoginScreen(navHostController)
        }
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
    }
}

fun callHomeScreen(navHostController: NavHostController?) {
    navHostController?.navigate(NavRote.HomeScreen.path) {
        popUpTo(NavRote.SplashScreen.path) {
            inclusive = true
        }
    }
}

fun callLoginScreen(navHostController: NavHostController?) {
    navHostController?.navigate(NavRote.LoginScreen.path) {
        popUpTo(NavRote.SplashScreen.path) {
            inclusive = true
        }
    }
}