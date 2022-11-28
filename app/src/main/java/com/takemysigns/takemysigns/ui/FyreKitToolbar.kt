package com.takemysigns.takemysigns.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.takemysigns.takemysigns.R
import com.takemysigns.takemysigns.features.web.WebFragment
import com.takemysigns.takemysigns.models.FyreKitMenuItem
import com.takemysigns.takemysigns.ui.theme.TakeMySignsTheme
import dev.hotwire.turbo.nav.TurboNavDestination

@Composable
fun FyreKitToolbar(
    title: MutableState<String>,
    actions: MutableState<List<FyreKitMenuItem>>,
    delegate: TurboNavDestination
) {
    TakeMySignsTheme {
        TopAppBar(
            backgroundColor = colorResource(id = R.color.color_primary),
            title = {
                Text(
                    text = title.value,
                    color = colorResource(id = R.color.color_on_primary),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            actions = {
                ActionMenu(actions, delegate = delegate)
            }
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun FyreKitToolbarPreview() {
    FyreKitToolbar(
        title = mutableStateOf("TakeMySigns"),
        actions = mutableStateOf(emptyList()),
        delegate = WebFragment()
    )
}