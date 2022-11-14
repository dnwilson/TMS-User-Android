package com.takemysigns.takemysigns.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.takemysigns.TakeMySigns.R
import com.takemysigns.takemysigns.base.Routes
import com.takemysigns.takemysigns.ui.helpers.PasswordFieldState
import com.takemysigns.takemysigns.ui.helpers.PhoneFieldState

@Composable
fun LoginView(navController: NavController, signIn: (phoneNumber: String, password: String) -> Unit) {
    val phoneNumber = remember { PhoneFieldState(label = "Phone number") }
    val password = remember { PasswordFieldState(label = "Password") }
    val canSubmit = password.isValid() && phoneNumber.isValid()
    val localFocusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "TakeMySigns Logo",
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Take My Signs",
                color = colorResource(id = R.color.color_primary),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Black,
                fontSize = 28.sp
            )
        }

        Column(
            Modifier.padding(40.dp, 0.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PhoneNumberField(
                label = phoneNumber.label,
                value = phoneNumber.text,
                error = phoneNumber.error,
                imeAction = ImeAction.Next,
                onImeAction = {
                    localFocusManager.moveFocus(FocusDirection.Down)
                },
                onTextChanged = {
                    if (phoneNumber.text.length <= 9) {
                        phoneNumber.text = it
                    }
                    phoneNumber.validate()
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            PasswordField(
                label = password.label,
                value = password.text,
                error = password.error,
                imeAction = ImeAction.Done,
                onTextChanged = {
                    password.text = it
                    password.validate()
                },
                onImeAction = {
                    localFocusManager.moveFocus(FocusDirection.Down)
                    if (password.isValid() && phoneNumber.isValid()) {
                        signIn(phoneNumber.text, password.text)
                    }
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box() {
                Button(
                    onClick = { navController.navigate(Routes.Login.name) },
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    enabled = canSubmit,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.color_primary),
                        contentColor = colorResource(id = R.color.white)
                    )
                ) {
                    Text(
                        "Login",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginViewPreview() {
    LoginView(navController = NavController(LocalContext.current), signIn = {
        phoneNumber, password -> {}
    })
}