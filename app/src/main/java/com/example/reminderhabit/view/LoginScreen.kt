package com.example.reminderhabit.view

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.reminderhabit.ui.theme.HEX787878
import com.example.reminderhabit.viewmodel.SharedPreferenceViewModel
import com.example.reminderhabit.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenWithConstraintLayout(
    navHostController: NavHostController,
    sharedPreferenceViewModel: SharedPreferenceViewModel,
    userViewModel: UserViewModel

) {
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    val isMobileNumberUsed by userViewModel.isMobileNumberUsed(phoneNumber).observeAsState(false)



    val userDetail by userViewModel.userDetail.observeAsState()
    var isHandled by remember { mutableStateOf(false) }
        if (userDetail != null && !isHandled) {
            isHandled = true
            if (phoneNumber == userDetail?.phoneNumber) {
                if (password == userDetail?.password) {
                    userViewModel.clearUserDetail()
                    sharedPreferenceViewModel.setLoggedIn(true)
                    sharedPreferenceViewModel.setUserMailId(phoneNumber)
                    navHostController.navigate(NavRote.HomeScreen.path) {
                        popUpTo(NavRote.LoginScreen.path) {
                            inclusive = true
                        }
                    }
                } else {
                    Toast.makeText(context, "Enter Correct password", Toast.LENGTH_SHORT).show()
                }
            }
        }



        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {

    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        val (title, phoneNumberField, passwordField, loginButton,signuptext) = createRefs()


        TittleTextView(
            text = "Login",
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, margin = 32.dp)
                start.linkTo(parent.start)
                bottom.linkTo(phoneNumberField.top, margin = 32.dp)
                end.linkTo(parent.end)
            },fontSize=24
        )

        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            placeholder = {Text("Mobile Number") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                cursorColor = Color.Black,
                disabledLabelColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            modifier = Modifier.constrainAs(phoneNumberField) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(passwordField.top, margin = 16.dp)
                width = Dimension.fillToConstraints
            }
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .border(1.dp, HEX787878, RoundedCornerShape(8.dp))
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = {Text("Password") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                cursorColor = Color.Black,
                disabledLabelColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            modifier = Modifier.constrainAs(passwordField) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(loginButton.top, margin = 32.dp)
                width = Dimension.fillToConstraints
            }
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .border(1.dp, HEX787878, RoundedCornerShape(8.dp))
        )




        Button(
            onClick = {
                if (phoneNumber.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Please enter login credentials", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (isMobileNumberUsed) {
                        userViewModel.getUserDetail(phoneNumber)
                    }else{
                        Toast.makeText(context, "User not found for this Mail", Toast.LENGTH_SHORT).show()

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
                bottom.linkTo(parent.bottom, margin = 20.dp)
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


    }
}

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}


