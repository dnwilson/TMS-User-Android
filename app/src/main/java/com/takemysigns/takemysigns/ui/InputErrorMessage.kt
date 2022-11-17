package com.takemysigns.takemysigns.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.takemysigns.takemysigns.R

@Composable
fun InputErrorMessage(error: String) {
    Text(error, color = colorResource(id = R.color.color_error), modifier = Modifier.fillMaxWidth())
}