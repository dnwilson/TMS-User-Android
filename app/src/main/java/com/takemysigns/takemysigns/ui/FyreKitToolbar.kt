package com.takemysigns.takemysigns.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    val webFragment = delegate as WebFragment

    TakeMySignsTheme {
        TopAppBar(
            backgroundColor = colorResource(id = R.color.white)
        ) {
            if (webFragment.hasBackStack()) {
                IconButton(onClick = { delegate.navigateBack()}) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colors.primary
                    )
                }
            }

            Row(
                Modifier.fillMaxHeight().weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProvideTextStyle(value = MaterialTheme.typography.h6) {
                    CompositionLocalProvider(
                        LocalContentAlpha provides ContentAlpha.high,
                        content = {
                            if (actions.value.isEmpty() && title.value.isBlank()) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_logo),
                                    contentDescription = "logo",
                                    alignment = Alignment.Center,
                                    modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(vertical = 4.dp)
                                )
                            } else {
                                Text(
                                    text = title.value,
                                    color = colorResource(id = R.color.color_primary),
                                    modifier = Modifier.fillMaxWidth().padding(
                                        start = if (webFragment.hasBackStack()) 0.dp else { 16.dp }
                                    )
                                )
                            }
                        }
                    )
                }
            }

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Row(
                    Modifier.fillMaxHeight(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    content = {
                        ActionMenu(actions, delegate = delegate)
                    }
                )
            }
        }
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

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun FyreKitToolbarWithoutTitlePreview() {
    FyreKitToolbar(
        title = mutableStateOf(""),
        actions = mutableStateOf(emptyList()),
        delegate = WebFragment()
    )
}