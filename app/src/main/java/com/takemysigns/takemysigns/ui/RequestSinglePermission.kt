package com.takemysigns.takemysigns.ui

import android.Manifest
import android.graphics.drawable.Icon
import android.util.Log
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.material.composethemeadapter.MdcTheme
import com.takemysigns.takemysigns.R
import com.takemysigns.takemysigns.base.Routes
import com.takemysigns.takemysigns.ui.helpers.TakeMySignsPermission

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestSinglePermission(
    permission: TakeMySignsPermission,
    onNext: () -> Unit
) {
    val permissionState = permission.permissionState
    if (permissionState.hasPermission) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = permission.icon,
                    contentDescription = permission.iconText,
                    modifier = Modifier.size(72.dp),
                    tint = colorResource(id = R.color.color_primary)
                )
                Text(
                    text = permission.title,
                    color = colorResource(id = R.color.color_primary),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(
                        start = 32.dp,
                        end = 32.dp,
                        bottom = 0.dp,
                        top = 16.dp
                    ),
                    style = MaterialTheme.typography.h1
                )
                Text(permission.grantedText)
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 32.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {

                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.color_primary),
                        contentColor = colorResource(id = R.color.color_on_primary)
                    ),
                    onClick = { onNext() }
                ) {
                    Text("Continue", style = MaterialTheme.typography.h6)
                }
            }
        }
    } else {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = permission.icon,
                    contentDescription = permission.iconText,
                    modifier = Modifier.size(72.dp),
                    tint = colorResource(id = R.color.color_primary)
                )
                Text(
                    text = permission.title,
                    color = colorResource(id = R.color.color_primary),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 32.dp, end = 32.dp, bottom = 0.dp, top = 16.dp),
                    style = MaterialTheme.typography.h1
                )
                Text(
                    text = permission.textToShow(),
                    color = colorResource(id = R.color.color_primary),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 32.dp, end = 32.dp, bottom = 16.dp)
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
                        onNext()
                    }
                ) {
                    Text(text = "Skip")
                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.color_primary),
                        contentColor = colorResource(id = R.color.color_on_primary)
                    ),
                    onClick = { permissionState.launchPermissionRequest() }
                ) {
                    Text(permission.requestText, style = MaterialTheme.typography.h6)
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
fun RequestSinglePermissionPreview() {
    val permission = TakeMySignsPermission(
        showRationalText = "The camera is important for this app. Please grant the permission.",
        unavailableRationalText = "Camera not available",
        grantedText = "Camera permission Granted",
        requestText = "Grant Access",
        iconText = "camera permission",
        title = "Camera Access",
        icon = Icons.Default.Camera,
        permissionState= rememberPermissionState(Manifest.permission.CAMERA)
    )
    MdcTheme() {
        RequestSinglePermission(
            permission = permission,
            onNext = { Log.d("RequestSinglePermission", "Next...") }
        )
    }
}