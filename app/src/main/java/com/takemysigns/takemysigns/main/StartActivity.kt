@file:OptIn(ExperimentalPermissionsApi::class)

package com.takemysigns.takemysigns.main

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.takemysigns.takemysigns.base.Routes
import com.takemysigns.takemysigns.base.TakeMySignsApp
import com.takemysigns.takemysigns.network.TakeMySignsRepository
import com.takemysigns.takemysigns.network.TakeMySignsService
import com.takemysigns.takemysigns.ui.LocationPermissions
import com.takemysigns.takemysigns.ui.LoginView
import com.takemysigns.takemysigns.ui.ProgressWheel
import com.takemysigns.takemysigns.ui.RequestSinglePermission
import com.takemysigns.takemysigns.ui.StartView
import com.takemysigns.takemysigns.ui.helpers.TakeMySignsPermission
import com.takemysigns.takemysigns.ui.theme.TakeMySignsTheme
import com.takemysigns.takemysigns.util.BASE_URL
import com.takemysigns.takemysigns.viewmodels.LoginViewModel
import com.takemysigns.takemysigns.viewmodels.LoginViewModelFactory

@OptIn(ExperimentalPermissionsApi::class)

class StartActivity  : ComponentActivity() {
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(TakeMySignsRepository(TakeMySignsService.getInstance()))
    }
    private lateinit var navController: NavHostController
    private var loading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        TakeMySignsApp.resetFirstRun()

        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.token.observe(this) {
            Log.d("StartActivity", "AuthToken is $it")

            if (TakeMySignsApp.getAuthToken()?.isNotBlank() == true) {
                navController.popBackStack()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    navController.navigate(Routes.AddPushNotificationAccess.name)
                } else {
                    navController.navigate(Routes.AddLocationAccess.name)
                }
            }
        }

        viewModel.loading.observe(this, Observer {
            loading = it
        })

        if (TakeMySignsApp.isFirstRun()) {
            setContent {
                if (loading) {
                    ProgressWheel()
                } else {
                    TakeMySignsTheme() {
                        Surface(modifier = Modifier.fillMaxSize()) {
                            Navigation()
                        }
                    }
                }
            }
        } else {
            Log.d("StartActivity", "goToMainActivity")
            goToMainActivity()
        }
    }

    private fun goToMainActivity() {
        val mainActivity = Intent(baseContext, MainActivity::class.java)
        mainActivity.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        startActivity(mainActivity)
        this.finish()
    }


    @Composable
    fun Navigation() {
        navController = rememberNavController()
        val startRoute = Routes.Login.name
        NavHost(navController = navController,
            startDestination = startRoute) {
            composable(startRoute) {
                StartView(navController = navController)
            }
            // Login Screen
            composable(Routes.Login.name) {
                LoginView(navController = navController, viewModel = viewModel)
            }

            composable(Routes.AddCameraAccess.name) {
                val pushPermission = TakeMySignsPermission(
                    showRationalText = "We need camera access so you can add photos to your orders. Please grant the permission.",
                    unavailableRationalText = "We need camera access so you can add photos to your orders",
                    grantedText = "Thank you for enabling photo access!",
                    requestText = "Allow Photos",
                    icon = Icons.Default.Camera,
                    iconText = "camera",
                    title = "Photo Sharing",
                    permissionState= rememberPermissionState(Manifest.permission.CAMERA)
                )
                RequestSinglePermission(
                    permission = pushPermission,
                    onNext = {
                        CookieManager.getInstance().setCookie(BASE_URL, "auth_token=${TakeMySignsApp.getAuthToken()}")
                        TakeMySignsApp.disableFirstRun()
                        navController.popBackStack()
                        goToMainActivity()
                    },
                )
            }

            composable(Routes.AddLocationAccess.name) {
                LocationPermissions(onNext = {
                    navController.popBackStack()
                    navController.navigate(Routes.AddCameraAccess.name)
                })
            }

            composable(Routes.AddPushNotificationAccess.name) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val pushPermission = TakeMySignsPermission(
                        showRationalText = "Please grant us push notification access so we can update you on the latest with your orders.",
                        unavailableRationalText = "Stay up-to-date with the latest information about your orders by enabling push notifications",
                        grantedText = "Thank you for enabling push notifications!",
                        requestText = "Notify Me!",
                        icon = Icons.Default.Notifications,
                        iconText = "push notifications",
                        title = "Push Notifications",
                        permissionState= rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
                    )
                    RequestSinglePermission(
                        permission = pushPermission,
                        onNext = {
                            navController.popBackStack()
                            navController.navigate(Routes.AddLocationAccess.name)
                        },
                    )
                } else {
                    navController.popBackStack()
                    navController.navigate(Routes.AddLocationAccess.name)
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
