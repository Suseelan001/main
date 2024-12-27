package com.example.reminderhabit.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathSegment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.reminderhabit.R
import com.example.reminderhabit.model.UserDetail
import com.example.reminderhabit.viewmodel.UserViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import androidx.core.content.FileProvider
import com.example.reminderhabit.bottomnavigation.NavRote
import com.example.reminderhabit.ui.theme.HEX787878
import com.example.reminderhabit.ui.theme.HEX7981ff
import com.example.reminderhabit.viewmodel.SharedPreferenceViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, sharedPreferenceViewModel: SharedPreferenceViewModel, userViewModel: UserViewModel) {
    var firstNametext by remember { mutableStateOf("") }
    var emailtext by remember { mutableStateOf("") }
    var phoneNotext by remember { mutableStateOf("") }
    var passwordtext by remember { mutableStateOf("") }
    val taskLoaded = remember { mutableStateOf(false) }
    var loginedEmail by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf(0) }


    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )


    var capturedImageUri by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        val userEmail = sharedPreferenceViewModel.getUserMailId()
        if (!userEmail.isNullOrEmpty()) {
            userViewModel.getUserDetail(userEmail)
        }
    }
    LaunchedEffect(Unit) {
        loginedEmail = sharedPreferenceViewModel.getUserMailId().toString()
    }

    if (loginedEmail.isNotEmpty()){
        val getUserDetail by userViewModel.getUser(loginedEmail).observeAsState()
        if (getUserDetail != null && !taskLoaded.value) {
            firstNametext = getUserDetail?.name ?: ""
            emailtext = getUserDetail?.email ?: ""
            phoneNotext = getUserDetail?.phoneNumber ?: ""
            passwordtext = getUserDetail?.password ?: ""
            userId = getUserDetail?.id ?: 0
            capturedImageUri = getUserDetail?.profileImage ?: ""


            taskLoaded.value = true
        }

    }




    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri.toString()
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)

                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        AsyncImage(
            model =if(capturedImageUri.isNotEmpty() && capturedImageUri!="null") capturedImageUri else R.drawable.profile,
            contentDescription = "Profile Image",
            modifier = Modifier
                .padding(32.dp)
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
                .align(Alignment.CenterHorizontally)
                .clickable {
                    val permissionCheckResult =
                        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(uri)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
            contentScale = ContentScale.Crop
        )




        Spacer(modifier = Modifier.height(32.dp))



        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .border(1.dp, HEX787878, RoundedCornerShape(8.dp)),
            value = firstNametext,
            onValueChange = { firstNametext = it },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                cursorColor = Color.Black,
                disabledLabelColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            placeholder = { Text("Name") }

        )


        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .border(1.dp, HEX787878, RoundedCornerShape(8.dp)),
            value = emailtext,
            readOnly = true,
            onValueChange = {  },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                cursorColor = Color.Black,
                disabledLabelColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            placeholder = { Text("Email") }

        )


        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .border(1.dp, HEX787878, RoundedCornerShape(8.dp)),
            value = phoneNotext,
            onValueChange = { phoneNotext = it },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                cursorColor = Color.Black,
                disabledLabelColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            placeholder = { Text("Mobile Number") }

        )


        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = "Change Password",
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .border(1.dp, Color(0xFF787878), RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                cursorColor = Color.Black,
                disabledLabelColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            placeholder = { Text("Change Password") },
            trailingIcon = {
                IconButton(onClick = {
                    navController.navigate("${NavRote.ChangePasswordScreen.path}/$userId")
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_keyboard_arrow_right_24),
                        contentDescription = "Select Start Time"
                    )
                }
            }
        )



        Spacer(modifier = Modifier.height(32.dp))





        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
        ) {
            Button(
                onClick = {
                    if (firstNametext.isEmpty() || emailtext.isEmpty()) {
                        Toast.makeText(context, "Please enter all fields", Toast.LENGTH_SHORT).show()
                    } else if (passwordtext.isNullOrEmpty()){
                        Toast.makeText(context, "Please enter the password", Toast.LENGTH_SHORT).show()

                    }   else {
                        if (isValidEmail(emailtext)) {
                            val user = UserDetail(id = userId, name = firstNametext, email = emailtext, profileImage = capturedImageUri,phoneNumber=phoneNotext, password = passwordtext)
                            userViewModel.updateUser(user)
                            navController.popBackStack()
                            Toast.makeText(context, "Your profile has been updated", Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                        }
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

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image
}