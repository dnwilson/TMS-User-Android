package com.takemysigns.takemysigns.ui

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.material.composethemeadapter.MdcTheme
import com.takemysigns.takemysigns.R
import com.takemysigns.takemysigns.base.Routes
import okhttp3.Route

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissions(navController: NavController) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    if (cameraPermissionState.hasPermission) {
        Text("Camera permission Granted")
    } else {
        Column {
            val textToShow = if (cameraPermissionState.shouldShowRationale) {
                "The camera is important for this app. Please grant the permission."
            } else {
                "Camera not available"
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PinDrop,
                    contentDescription = "notifications",
                    modifier = Modifier.size(64.dp),
                    tint = colorResource(id = R.color.color_primary)
                )
                Text(
                    text = textToShow,
                    color = colorResource(id = R.color.color_primary),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)
                )
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 32.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                TextButton(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.transparent),
                        contentColor = colorResource(id = R.color.color_primary)
                    ),
                    onClick = {
                        navController.popBackStack()
                        navController.navigate(Routes.AddPushNotificationAccess.name)
                    }
                ) {
                    Text(text = "Skip")
                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.color_primary),
                        contentColor = colorResource(id = R.color.color_on_primary)
                    ),
                    onClick = { cameraPermissionState.launchPermissionRequest() }
                ) {
                    Text("Request permission")
                }
            }
        }
    }
}

@Preview
@Composable
fun CameraAccessPreview() {
    MdcTheme() {
        CameraPermissions(navController = NavController(LocalContext.current))
    }
}