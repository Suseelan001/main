package com.example.reminderhabit.bottomnavigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.reminderhabit.R



@Composable
fun BottomBar(
    navController: NavHostController,
    isBottomBarVisible: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    onFabClick: () -> Unit
) {
    val context = LocalContext.current // Get context for finishing the activity
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    BackHandler {
        if (currentDestination != NavRote.HomeScreen.path) {
            navController.navigate(NavRote.HomeScreen.path) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
            }
        } else {
            (context as? Activity)?.finish() // Safely finish the activity
        }
    }

    Box(modifier = modifier.fillMaxWidth()) {
        if (isBottomBarVisible.value) {
            CurvedBottomBar(navController = navController)

            FloatingActionButton(
                onClick = onFabClick,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-40).dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun CurvedBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val navigationItems = getBottomNavigationItems()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                clip = false
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navigationItems.forEachIndexed { index, navigationItem ->
                val isSelected = currentRoute == navigationItem.route
                if (index == navigationItems.size / 2) {
                    Spacer(modifier = Modifier.width(60.dp)) // Space for the FAB
                }

                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(navigationItem.icon),
                            contentDescription = navigationItem.label,
                            modifier = Modifier.size(24.dp) // Adjust icon size
                        )
                    },
                    label = { Text(text = navigationItem.label, style = MaterialTheme.typography.bodySmall) },
                    selected = isSelected,
                    onClick = {
                        navController.navigate(navigationItem.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true // Save the state of the stack
                            }
                            launchSingleTop = true // Prevent duplicate destinations
                            restoreState = true // Restore the saved state
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = Color.Gray
                    )
                )
            }
        }
    }
}




