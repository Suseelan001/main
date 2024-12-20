package com.example.reminderhabit.bottomnavigation

sealed class NavRote(var path:String) {


    data object LoginScreen: NavRote("LOGIN_SCREEN")
    data object SignupScreen: NavRote("SIGNUP_SCREEN")

    data object HomeScreen: NavRote("HOME_SCREEN")
    data object ChartScreen: NavRote("CHART_SCREEN")
    data object ProfileScreen: NavRote("PROFILE_SCREEN")
    data object SettingsScreen: NavRote("SETTINGS_SCREEN")

    data object TaskListScreen: NavRote("TASK_SCREEN")

    data object AddTaskScreen: NavRote("ADD_TASK_SCREEN")
    data object PermissionScreen: NavRote("PERMISSION_SCREEN")

/*    fun withArgs(vararg args: String?): String {
        return buildString {
            append(path)
            args.forEach{ arg ->
                append("/$arg")
            }
        }
    }*/

}