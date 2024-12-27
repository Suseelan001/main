package com.example.reminderhabit.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.example.reminderhabit.TittleTextView
import com.example.reminderhabit.bottomnavigation.NavRote
import com.example.reminderhabit.model.UserDetail
import com.example.reminderhabit.viewmodel.MainViewmodel
import com.example.reminderhabit.viewmodel.UserViewModel

@Composable
fun SignupScreen(
    navHostController: NavHostController,
    mainViewmodel: MainViewmodel,
    userViewModel: UserViewModel
) {
    var userName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val isEmailUsed by userViewModel.isEmailUsed(email).observeAsState(false)


    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        val (title, userNameField, emailField, passwordField, signupButton, loginText) = createRefs()

        TittleTextView(
            text = "Sign Up",
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, margin = 32.dp)
                start.linkTo(parent.start)
                bottom.linkTo(userNameField.top, margin = 32.dp)
                end.linkTo(parent.end)
            }, fontSize = 24
        )

        TextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.constrainAs(userNameField) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(emailField.top, margin = 16.dp)
                width = Dimension.fillToConstraints
            }
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.constrainAs(emailField) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(passwordField.top, margin = 16.dp)
                width = Dimension.fillToConstraints
            }
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.constrainAs(passwordField) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(signupButton.top, margin = 32.dp)
                width = Dimension.fillToConstraints
            }
        )
        Button(
            onClick = {
                if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Please enter all fields", Toast.LENGTH_SHORT).show()
                } else {
                    if (isValidEmail(email)) {
                        if (isEmailUsed) {
                            Toast.makeText(context, "Email is already in use. Please use a different email.", Toast.LENGTH_SHORT).show()
                        } else {
                            val user = UserDetail(name = userName, email = email, password = password)
                            userViewModel.insertUser(user)
                            navHostController.popBackStack()
                            Toast.makeText(context, "Your account has been created", Toast.LENGTH_SHORT).show()

                        }
                    } else {
                        Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.constrainAs(signupButton) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(loginText.top, margin = 32.dp)
                width = Dimension.fillToConstraints
            }
        ) {
            Text(text = "Sign Up")
        }


        Box(
            modifier = Modifier.constrainAs(loginText) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, margin = 32.dp)
                width = Dimension.fillToConstraints
            }
        ) {
            val annotatedText = buildAnnotatedString {
                append("Already have an account? Please ")
                pushStringAnnotation(tag = "login", annotation = "login_action")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                    append("login")
                }
                pop()
            }

            ClickableText(
                text = annotatedText,
                style = TextStyle(fontSize = 16.sp, color = Color.Black),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                onClick = { offset ->
                    annotatedText.getStringAnnotations(tag = "login", start = offset, end = offset)
                        .firstOrNull()?.let { annotation ->
                            navHostController.popBackStack()
                        }
                }
            )
        }
    }
}







