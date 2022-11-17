package com.takemysigns.takemysigns.ui.helpers

import androidx.compose.ui.graphics.vector.ImageVector
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
class TakeMySignsPermission(
    private val showRationalText: String,
    private val unavailableRationalText: String,
    val grantedText: String,
    val requestText: String,
    val iconText: String,
    val title: String,
    val icon: ImageVector,
    val permissionState: PermissionState
) {

    fun textToShow(): String {
        return if (permissionState.shouldShowRationale) showRationalText else unavailableRationalText
    }
}