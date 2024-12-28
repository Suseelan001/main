package com.example.reminderhabit.view

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.example.reminderhabit.TittleTextView
import com.example.reminderhabit.model.UserDetail
import com.example.reminderhabit.ui.theme.HEX787878
import com.example.reminderhabit.viewmodel.MainViewmodel
import com.example.reminderhabit.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    navHostController: NavHostController,
    mainViewmodel: MainViewmodel,
    userViewModel: UserViewModel
) {
    var userName by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val isMobileNumberUsed by userViewModel.isMobileNumberUsed(phoneNumber).observeAsState(false)


    ConstraintLayout(

        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        val (title, userNameField, mobileNumberField, passwordField, signupButton, loginText) = createRefs()

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
            placeholder = {Text("Name") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                cursorColor = Color.Black,
                disabledLabelColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            modifier = Modifier.constrainAs(userNameField) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(mobileNumberField.top, margin = 16.dp)
                width = Dimension.fillToConstraints
            }
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .border(1.dp, HEX787878, RoundedCornerShape(8.dp))
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
            modifier = Modifier.constrainAs(mobileNumberField) {
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
                bottom.linkTo(signupButton.top, margin = 32.dp)
                width = Dimension.fillToConstraints
            }
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .border(1.dp, HEX787878, RoundedCornerShape(8.dp))
        )






        Button(
            onClick = {
                if (userName.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Please enter all fields", Toast.LENGTH_SHORT).show()
                } else {

                        if (isMobileNumberUsed) {
                            Toast.makeText(context, "Mobile Number is already in use. Please use a different Mobile Number.", Toast.LENGTH_SHORT).show()
                        } else {
                            val user = UserDetail(name = userName, phoneNumber = phoneNumber, password = password)
                            userViewModel.insertUser(user)
                            navHostController.popBackStack()
                            Toast.makeText(context, "Your account has been created", Toast.LENGTH_SHORT).show()

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







