package com.takemysigns.takemysigns.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.takemysigns.takemysigns.features.web.WebFragment
import com.takemysigns.takemysigns.models.FyreKitMenuItem
import dev.hotwire.turbo.nav.TurboNavDestination
import dev.hotwire.turbo.visit.TurboVisitAction
import dev.hotwire.turbo.visit.TurboVisitOptions
import kotlinx.coroutines.launch

// Whether action items are allowed to overflow into a dropdown menu - or NOT SHOWN to hide
//enum class OverflowMode {
//    NEVER_OVERFLOW, IF_NECESSARY, ALWAYS_OVERFLOW, NOT_SHOWN
//}

// Note: should be used in a RowScope
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ActionMenu(
    items: MutableState<List<FyreKitMenuItem>>,
    numIcons: Int = 3, // includes overflow menu icon; may be overridden by NEVER_OVERFLOW
    menuVisible: MutableState<Boolean> = remember { mutableStateOf(false) },
    delegate: TurboNavDestination
) {
//    val sheetstate = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
//    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetstate)
//    val scope = rememberCoroutineScope()
//    val interactionSource = remember { MutableInteractionSource() }
//    var enabled = false

    if (items.value.isEmpty()) {
        return
    }
    // decide how many action items to show as icons
    val (appbarActions, overflowActions) = remember(items, numIcons) {
        separateIntoIconAndOverflow(items.value, numIcons)
    }

    for (item in appbarActions) {
        key(item.hashCode()) {
            val name = item.title
            if (item.icon() != null) {
                IconButton(onClick = {
//                    item.doAction
                    Log.d("ActionMenu", "path -- ${item.path}")
                }) {
                    Icon(item.icon(), name)
                }
            } else {
                TextButton(onClick = {
                    Log.d("ActionMenu", "path -- ${item.path}")
                }) {
                    Text(
                        text = name,
                        color = MaterialTheme.colors.onPrimary.copy(alpha = LocalContentAlpha.current),
                    )
                }
            }
        }
    }

    if (overflowActions.isNotEmpty()) {
        IconButton(onClick = { menuVisible.value = true }) {
            Icon(Icons.Default.MoreVert, "More actions")
        }
        // Creating a Bottom Sheet
//        BottomSheetScaffold(
//            scaffoldState = scaffoldState,
//            sheetContent =  {
//                Box(Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
//                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//                        for (item in overflowActions) {
//                            key(item.hashCode()) {
//                                Row(
//                                    modifier = Modifier
//                                        .clickable(
//                                            enabled = enabled,
//                                            onClick = {
//                                                scope.launch {
//                                                    if(sheetstate.isCollapsed) {
//                                                        sheetstate.expand()
//                                                    } else {
//                                                        sheetstate.collapse()
//                                                    }
//                                                    enabled = !enabled
//                                                    Log.d("ActionMenu", "clicked -- ${item.path}")
//                                                }
//                                            },
//                                            interactionSource = interactionSource,
//                                            indication = rememberRipple(true)
//                                        )
//                                        .fillMaxWidth(),
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    val typography = MaterialTheme.typography
//                                    ProvideTextStyle(typography.subtitle1) {
//                                        val contentAlpha = if (enabled) ContentAlpha.high else ContentAlpha.disabled
//                                        CompositionLocalProvider(LocalContentAlpha provides contentAlpha) {
//                                            Text(item.title)
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            },
//            sheetPeekHeight = 0.dp
//        ) {}
        DropdownMenu(
            expanded = menuVisible.value,
            onDismissRequest = { menuVisible.value = false },
        ) {
            for (item in overflowActions) {
                key(item.hashCode()) {
                    DropdownMenuItem(onClick = {
                        menuVisible.value = false

                        if (item.isScript()) {
                            Log.d("ActionMenu", item.toString())
                            delegate.session.webView.evaluateJavascript(item.script.toString(), null)
                        } else {
                            delegate.navigate(item.path.toString())
                        }
                        Log.d("ActionMenu", "isGet -- ${item.isGet()}")
                        Log.d("ActionMenu", "isDanger -- ${item.isDanger()}")
                        Log.d("ActionMenu", "clicked -- ${item}")
                    }) {
                        //Icon(item.icon, item.name) just have text in the overflow menu
                        val color = if (item.isDanger()) MaterialTheme.colors.error else { MaterialTheme.colors.onBackground }
                        Text(item.title, color = color)
                    }
                }
            }
        }
    }
}

private fun separateIntoIconAndOverflow(
    items: List<FyreKitMenuItem>,
    numIcons: Int
): Pair<List<FyreKitMenuItem>, List<FyreKitMenuItem>> {
    var (iconCount, overflowCount, preferIconCount) = Triple(0, 0, 0)
    for (item in items) {
        overflowCount++
    }

    val needsOverflow = iconCount + preferIconCount > numIcons || overflowCount > 0
    val actionIconSpace = numIcons - (if (needsOverflow) 1 else 0)

    val iconActions = ArrayList<FyreKitMenuItem>()
    val overflowActions = ArrayList<FyreKitMenuItem>()

    var iconsAvailableBeforeOverflow = actionIconSpace - iconCount
    for (item in items) {
        overflowActions.add(item)
    }
    return Pair(iconActions, overflowActions)
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun ActionMenuPreview() {
    val list: List<FyreKitMenuItem> = listOf(
        FyreKitMenuItem(title = "Show", path = "orders/1", type = "default", method = "get", alertMessage = null, alertTitle = null, script = null),
        FyreKitMenuItem(title = "Edit", path = "orders/1/edit", type = "default", method = "get", alertMessage = null, alertTitle = null, script = null),
        FyreKitMenuItem(title = "Delete", path = "orders/1", type = "danger", method = "delete", alertMessage = "Are you sure you want to delete this order?", alertTitle = null, script = "document.querySelector('#orders-1').click()"),
        FyreKitMenuItem(title = "Show", path = "orders/1", type = "default", method = "get", alertMessage = null, alertTitle = null, script = null)
    )
    val items = mutableStateOf(list)

    MaterialTheme {
        ActionMenu(items = items, delegate = WebFragment())
    }
}