package com.example.reminderhabit.bottomnavigation


import com.example.reminderhabit.R

data class BottomNavigationItem(
    val label: String,
    val icon: Int,
    val route: String
)

fun getBottomNavigationItems(): List<BottomNavigationItem> {
    return listOf(
        BottomNavigationItem(
            label = "",
            icon = R.drawable.home,
            route = NavRote.HomeScreen.path
        ),
        BottomNavigationItem(
            label = "",
            icon =R.drawable.chart,
            route = NavRote.ChartScreen.path
        ),
        BottomNavigationItem(
            label = "",
            icon = R.drawable.profile,
            route = NavRote.ProfileScreen.path
        ),
        BottomNavigationItem(
            label = "",
            icon =R.drawable.settings,
            route = NavRote.SettingsScreen.path
        ),
    )
}
