package com.example.reminderhabit.view

import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.LaunchedEffect
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
fun LoginScreenWithConstraintLayout(
    navHostController: NavHostController,
    mainViewmodel: MainViewmodel,    userViewModel: UserViewModel

) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    val userDetail by userViewModel.userDetail.observeAsState()

    if (userDetail != null) {
        if (email == userDetail?.email) {
            if (password == userDetail?.password) {
                navHostController.navigate(NavRote.HomeScreen.path)
            } else {
                Toast.makeText(context, "Enter Correct password", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "User not found for this Mail", Toast.LENGTH_SHORT).show()
        }
    }


    Column(Modifier.fillMaxSize()
        , verticalArrangement = Arrangement.Center) {

    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        val (skipbutton,title, emailField, passwordField, loginButton,signuptext) = createRefs()







        TittleTextView(
            text = "Login",
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, margin = 32.dp)
                start.linkTo(parent.start)
                bottom.linkTo(emailField.top, margin = 32.dp)
                end.linkTo(parent.end)
            },fontSize=24
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
                bottom.linkTo(loginButton.top, margin = 32.dp)
                width = Dimension.fillToConstraints
            }
        )

        Button(
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Please enter login credentials", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if(isValidEmail(email)){
                        userViewModel.getUserDetail(email)


                    }else{
                        Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT)
                            .show()
                    }

                }
            },
            modifier = Modifier.constrainAs(loginButton) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(signuptext.top, margin = 32.dp)
                width = Dimension.fillToConstraints
            }
        ) {
            Text(text = "Login")
        }

        Box(
            modifier = Modifier.constrainAs(signuptext) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(skipbutton.top, margin = 20.dp)
                width = Dimension.fillToConstraints
            }
        ) {
            val annotatedText = buildAnnotatedString {
                append("Did not have an account? Please ")
                pushStringAnnotation(tag = "SIGN_UP", annotation = "sign_up_action")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                    append("sign up")
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
                    annotatedText.getStringAnnotations(tag = "SIGN_UP", start = offset, end = offset)
                        .firstOrNull()?.let { annotation ->
                            navHostController.navigate(NavRote.SignupScreen.path)
                        }
                }
            )
        }
        Button(
            onClick = {
                navHostController.navigate(NavRote.HomeScreen.path)

            },

            modifier = Modifier.constrainAs(skipbutton) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.bottom, margin = 32.dp)
                width = Dimension.wrapContent
            }
        ) {
            Text(text = "Skip")
        }

    }
}

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}


