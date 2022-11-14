package com.takemysigns.takemysigns.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.material.composethemeadapter.MdcTheme
import com.takemysigns.takemysigns.base.Routes
import com.takemysigns.takemysigns.ui.LoginView
import com.takemysigns.takemysigns.ui.StartView
import com.takemysigns.takemysigns.viewmodels.LoginViewModel
import com.takemysigns.takemysigns.viewmodels.LoginViewModelFactory

class StartActivity  : ComponentActivity() {
    private val viewModel: LoginViewModel by viewModels { LoginViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MdcTheme() {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Navigation()
                }
            }
        }
    }


    @Composable
    fun Navigation() {
        val navController = rememberNavController()
        val startRoute = Routes.Start.name

        NavHost(navController = navController,
            startDestination = startRoute) {
            composable(startRoute) {
                StartView(navController = navController)
            }
            // Login Screen
            composable(Routes.Login.name) {
                LoginView(navController = navController) {
                    phoneNumber: String, password: String -> viewModel.signIn(phoneNumber, password)
                }
            }
            // Register Screen
            composable(Routes.Register.name) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Register Screen", color = Color.Black, fontSize = 24.sp)
                }
            }
        }
    }
}
