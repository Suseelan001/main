package com.example.reminderhabit.view

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.reminderhabit.R
import com.example.reminderhabit.ui.theme.HEX787878
import com.example.reminderhabit.ui.theme.HEX7981ff
import com.example.reminderhabit.viewmodel.SharedPreferenceViewModel
import com.example.reminderhabit.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    userId:String,
    navHostController: NavHostController,
    userViewModel: UserViewModel

) {
    var newPassword by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    BackHandler {
        navHostController.popBackStack()
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
                    text = "Change password",
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

            modifier = Modifier
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(32.dp))




        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .border(1.dp, HEX787878, RoundedCornerShape(8.dp)),
            value = newPassword,
            onValueChange = {newPassword=it},
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                cursorColor = Color.Black,
                disabledLabelColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            placeholder = { Text("New Password") }

        )



        Spacer(modifier = Modifier.height(32.dp))







        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
        ) {
            Button(
                onClick = {
                    if (newPassword.isEmpty()) {
                        Toast.makeText(context, "Please enter Password", Toast.LENGTH_SHORT).show()
                    }  else {

                        userViewModel.updatePassword(userId.toInt(),newPassword)
                        navHostController.popBackStack()
                        Toast.makeText(context, "Your password has been changed", Toast.LENGTH_SHORT).show()

                    }

                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(HEX7981ff)
            ) {
                Text(
                    text = "Save"
                )
            }
        }


    }
}




