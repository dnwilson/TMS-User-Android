package com.takemysigns.takemysigns.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Forward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Start
import androidx.compose.material.icons.filled.TransferWithinAStation
import androidx.compose.ui.graphics.vector.ImageVector
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.takemysigns.takemysigns.R

interface ActionButton {
    val title: String
}

@JsonClass(generateAdapter = true)
data class FyreKitMenu(
    override val title: String,
    val name: String,
    val icon: String,
    val options: List<FyreKitMenuItem>
) : ActionButton {
    fun image(): Int {
        return R.drawable::class.java.getId("R.drawable.$icon")
    }
}

@JsonClass(generateAdapter = true)
data class FyreKitMenuItem(
    override val title: String,
    val path: String?,
    val script: String?,
    val alertMessage: String?,
    val alertTitle: String?,
    val type: String?,
    val method: String?,
    @Json(ignore = true)
    var isDanger: () -> Boolean = { type == "danger" },
    @Json(ignore = true)
    var isGet: () -> Boolean = { path?.isNotBlank() == true && script?.isBlank() == true },
    @Json(ignore = true)
    var isScript: () -> Boolean = { path.isNullOrBlank() || script?.isNotBlank() ?: false }
) : ActionButton {
    fun icon(): ImageVector {
        val icon : ImageVector =
            when(title) {
            "Duplicate" -> {
                Icons.Default.ContentCopy
            }
            "Save" -> {
                Icons.Default.Save
            }
            "Cancel" -> {
                Icons.Default.Cancel
            }
            "Unassign" -> {
                Icons.Default.PersonRemove
            }
            "Reassign" -> {
                Icons.Default.TransferWithinAStation
            }
            "Release" -> {
                Icons.Default.PersonRemove
            }
            "Start" -> {
                Icons.Default.Start
            }
            "Accept" -> {
                Icons.Default.Check
            }
            "Resume" -> {
                Icons.Default.Forward
            }
            else -> {
                Icons.Default.MoreVert
            }
        }
        return icon
    }
}