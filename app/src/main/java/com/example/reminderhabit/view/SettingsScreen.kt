package com.example.reminderhabit.view



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.reminderhabit.bottomnavigation.NavRote
import com.example.reminderhabit.viewmodel.MainViewmodel
import com.example.reminderhabit.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navHostController: NavHostController, userViewModel: UserViewModel
) {
    val settingsItems = listOf("Permissions", "Privacy", "About Us","Logout")
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        LogoutDialog(
            onLogout = {
                navHostController.navigate(NavRote.LoginScreen.path) {
                    popUpTo(NavRote.HomeScreen.path) {
                        inclusive = true
                    }
                }
                userViewModel.clearData()
                showDialog = false
            },
            onDismiss = {
                showDialog = false
            }
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black
            ),
            title = {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(settingsItems.size) { index ->
                SettingItem(
                    index = index,
                    text = settingsItems[index],
                    onClick = {
                        when (index) {
                            0 -> navHostController.navigate(NavRote.PermissionScreen.path)
                            1 -> navHostController.navigate(NavRote.PermissionScreen.path)
                            2 -> navHostController.navigate(NavRote.PermissionScreen.path)
                            3 ->  showDialog=true
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun SettingItem(
    index: Int,
    text: String,
    onClick: () -> Unit // Add an onClick parameter
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp) // Add padding to the sides for better separation
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .clickable { onClick() }, // Enable click functionality
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.weight(1f)
            )
            if (text!="Logout"){
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Arrow Forward",
                    modifier = Modifier.size(24.dp)
                )
            }

        }

        // Bottom shadow line (Divider) with extra padding for separation
        Spacer(modifier = Modifier.height(8.dp)) // Add space after the item
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Gray.copy(alpha = 0.3f))
        )
        Spacer(modifier = Modifier.height(8.dp)) // Add space after the divider
    }
}




@Composable
fun LogoutDialog(onLogout: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Confirm Logout")
        },
        text = {
            Text(text = "Are you sure you want to log out?")
        },
        confirmButton = {
            TextButton(onClick = {
                onLogout()
            }) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


