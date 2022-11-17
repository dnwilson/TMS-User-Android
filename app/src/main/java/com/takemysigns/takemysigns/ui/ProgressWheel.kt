package com.takemysigns.takemysigns.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.takemysigns.takemysigns.ui.theme.TakeMySignsTheme

@Composable
fun ProgressWheel() {
    Column(
        modifier = Modifier.fillMaxHeight().fillMaxWidth().background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = MaterialTheme.colors.primary,
            strokeWidth = 4.dp)
    }
}

@Preview
@Composable
fun ProgressWheelPreview() {
    TakeMySignsTheme {
        ProgressWheel()
    }
}