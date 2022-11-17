package com.takemysigns.takemysigns.ui

import android.Manifest
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
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
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
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.material.composethemeadapter.MdcTheme
import com.takemysigns.takemysigns.R
import com.takemysigns.takemysigns.base.Routes
import com.takemysigns.takemysigns.ui.theme.TakeMySignsTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissions(
    onNext: () -> Unit
) {
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )
    if (locationPermissionsState.allPermissionsGranted) {
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
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "location on",
                    modifier = Modifier.size(72.dp),
                    tint = colorResource(id = R.color.color_primary)
                )
                Text(
                    text = "Location Access",
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
                Text("Location access enabled")
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
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val allPermissionsRevoked =
                locationPermissionsState.permissions.size ==
                        locationPermissionsState.revokedPermissions.size

            val textToShow = if (!allPermissionsRevoked) {
                // If not all the permissions are revoked, it's because the user accepted the COARSE
                // location permission, but not the FINE one.
                "Yay! Thanks for letting me access your approximate location. " +
                        "But you know what would be great? If you allow me to know where you " +
                        "exactly are. Thank you!"
            } else if (locationPermissionsState.shouldShowRationale) {
                // Both location permissions have been denied
                "Getting your exact location is important for this app. " +
                        "Please grant us fine location. Thank you :D"
            } else {
                // First time the user sees this feature or the user doesn't want to be asked again
                "The app uses your location to accurately create your orders"
            }

            val buttonText = if (!allPermissionsRevoked) {
                "Allow precise location"
            } else {
                "Share Location"
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOff,
                    contentDescription = "notifications",
                    modifier = Modifier.size(64.dp),
                    tint = colorResource(id = R.color.color_primary)
                )
                Text(
                    text = "Location Access",
                    color = colorResource(id = R.color.color_primary),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 32.dp, end = 32.dp, bottom = 0.dp, top = 16.dp),
                    style = MaterialTheme.typography.h1
                )
                Text(
                    text = textToShow,
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

                    }
                ) {
                    Text(text = "Skip")
                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.color_primary),
                        contentColor = colorResource(id = R.color.color_on_primary)
                    ),
                    onClick = { locationPermissionsState.launchMultiplePermissionRequest() }
                ) {
                    Text(buttonText, style = MaterialTheme.typography.h6)
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
fun LocationPermissionsPreview() {
    val multiplePermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    TakeMySignsTheme() {
        LocationPermissions(onNext = { Log.d("LocationPermissions", "onNext...") })
    }
}