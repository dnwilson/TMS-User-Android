package com.takemysigns.takemysigns.features.native

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.takemysigns.takemysigns.R
import com.takemysigns.takemysigns.base.NavDestination
import com.takemysigns.takemysigns.base.Routes
import com.takemysigns.takemysigns.base.TakeMySignsApp
import com.takemysigns.takemysigns.main.MainActivity
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
import dev.hotwire.turbo.fragments.TurboFragment
import dev.hotwire.turbo.nav.TurboNavGraphDestination

@OptIn(ExperimentalPermissionsApi::class)
@TurboNavGraphDestination(uri = "turbo://fragment/start-app")
class StartFragment : TurboFragment(), NavDestination {
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(TakeMySignsRepository(TakeMySignsService.getInstance()))
    }
    private lateinit var navController: NavHostController
    private var loading = false
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivity = activity as MainActivity

        viewModel.errorMessage.observe(this) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.token.observe(this) {
            Log.d("StartFragment", "AuthToken is $it")

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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ComposeView>(R.id.start_view).setContent {
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
    }

    private fun startApp() {
        Log.d("StartFragment", "startApp: go to url - $")
        mainActivity.delegate.navigate(BASE_URL)
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
                        startApp()
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