package com.takemysigns.takemysigns.ui

import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.takemysigns.takemysigns.R
import com.takemysigns.takemysigns.base.Routes
import com.takemysigns.takemysigns.base.TakeMySignsApp
import com.takemysigns.takemysigns.network.TakeMySignsRepository
import com.takemysigns.takemysigns.network.TakeMySignsService
import com.takemysigns.takemysigns.ui.helpers.PasswordFieldState
import com.takemysigns.takemysigns.ui.helpers.PhoneFieldState
import com.takemysigns.takemysigns.ui.theme.TakeMySignsTheme
import com.takemysigns.takemysigns.viewmodels.LoginViewModel
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun LoginView(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val phoneNumber = remember { PhoneFieldState(label = "Phone number") }
    val password = remember { PasswordFieldState(label = "Password") }
    val canSubmit = password.isValid() && phoneNumber.isValid()
    val localFocusManager = LocalFocusManager.current
    val composableScope = rememberCoroutineScope()

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
                text = "Temporary Sign\nPlacement Service",
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h1
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
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column() {
                Button(
                    onClick = {
                        viewModel.signIn(phoneNumber.text, password.text)
                        println("Goodbye World! -- ${TakeMySignsApp.getAuthToken()}")
                    },
                    modifier = Modifier.fillMaxWidth(),
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
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = {
                        navController.navigate(Routes.Register.name)
                    },
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.transparent),
                        contentColor = MaterialTheme.colors.primary
                    )
                ) {
                    Text(
                        "Register",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginViewPreview() {
    TakeMySignsTheme {
        LoginView(
            navController = NavController(LocalContext.current),
            viewModel = LoginViewModel(TakeMySignsRepository(TakeMySignsService.getInstance()))
        )
    }
}