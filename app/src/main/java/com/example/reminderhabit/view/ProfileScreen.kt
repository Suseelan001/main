package com.example.reminderhabit.view

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.reminderhabit.R
import com.example.reminderhabit.bottomnavigation.NavRote
import com.example.reminderhabit.model.UserDetail
import com.example.reminderhabit.viewmodel.MainViewmodel
import com.example.reminderhabit.viewmodel.UserViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(navController: NavController, mainViewmodel: MainViewmodel, userViewModel: UserViewModel) {
    var nametext by remember { mutableStateOf("") }
    var emailtext by remember { mutableStateOf("") }
    var profileImageUri by rememberSaveable { mutableStateOf<String?>(null) }
    val context= LocalContext.current

    var permissionsGranted by remember { mutableStateOf(false) }


    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { uri ->
        uri?.let {
            profileImageUri = it.toString()
        }
    }

    // Permissions request for camera
    val cameraPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(Manifest.permission.CAMERA)
    )

    // Request permissions if needed when profile image is clicked
    if (cameraPermissionState.shouldShowRationale) {
        // Show rationale if needed (optional)
    }

    GalleryAndCameraPermissionsRequest(
        onPermissionsGranted = {
            println("CHECK-TAG-PERMISSION_GRANTED TRUE")
            permissionsGranted = true
        },
        onPermissionsDenied = {
            println("CHECK-TAG-PERMISSION_GRANTED FALSE")
            permissionsGranted = false
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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

        // Profile Image Clickable
        AsyncImage(
            model = profileImageUri ?: R.drawable.profile,
            contentDescription = "Profile Image",
            modifier = Modifier
                .padding(32.dp)
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
                .align(Alignment.CenterHorizontally)
                .clickable {
                    // When profile image is clicked, request camera permission if not granted
                    if (permissionsGranted) {
                        val photoUri = createImageUri(context)
                        photoUri?.let {
                            cameraLauncher.launch(it)
                        }
                    } else {
                        // Request camera permission
                        cameraPermissionState.launchMultiplePermissionRequest()
                    }
                }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Name Input
        TextField(
            value = nametext,
            onValueChange = { nametext = it },
            textStyle = TextStyle(fontSize = 24.sp),
            placeholder = { Text("Enter your Name here", color = Color.Gray) },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.LightGray
            ),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Email Input
        TextField(
            value = emailtext,
            onValueChange = { emailtext = it },
            textStyle = TextStyle(fontSize = 24.sp),
            placeholder = { Text("Enter your Email here", color = Color.Gray) },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.LightGray
            ),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Save Button
        Button(
            onClick = {
                if (nametext.isEmpty() || emailtext.isEmpty()) {
                    Toast.makeText(context, "Please enter all fields", Toast.LENGTH_SHORT).show()
                } else {
                    if (isValidEmail(emailtext)) {
                        val user = UserDetail(name = nametext, email = emailtext, password = "", profileImage = profileImageUri)
                        //userViewModel.insertUser(user)
                    } else {
                        Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                    }
                }

            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Save")
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GalleryAndCameraPermissionsRequest(
    onPermissionsGranted: () -> Unit,
    onPermissionsDenied: () -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(Manifest.permission.CAMERA)
    )

    LaunchedEffect(permissionState.allPermissionsGranted) {
        if (permissionState.allPermissionsGranted) {
            onPermissionsGranted() // Trigger when permission is granted
        } else {
            onPermissionsDenied() // Trigger when permission is denied
        }
    }

    // Show rationale if needed (optional)
    if (permissionState.shouldShowRationale) {
        // Optionally show rationale to the user
    }
}


fun createImageUri(context: Context): Uri? {
    val contentResolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "profile_photo_${System.currentTimeMillis()}.jpg")
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }
    return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
}