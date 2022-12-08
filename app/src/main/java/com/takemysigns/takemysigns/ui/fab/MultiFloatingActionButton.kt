package com.takemysigns.takemysigns.ui.fab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.takemysigns.takemysigns.R

@Composable
fun MultiFloatingActionButton(
    fabIcon: FabIcon,
    fabTitle: String?,
    showFabTitle: Boolean,
    modifier: Modifier = Modifier,
    itemsMultiFab: List<MultiFabItem>,
    fabState: MutableState<MultiFabState> = rememberMultiFabState(),
    fabOption: FabOption = FabOption(),
    onFabItemClicked: (fabItem: MultiFabItem) -> Unit,
    stateChanged: (fabState: MultiFabState) -> Unit = {}
) {
    val rotation by animateFloatAsState(
        if (fabState.value == MultiFabState.Expanded) fabIcon.iconRotate ?: 0f else 0f
    )

    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.End,
    ) {
        AnimatedVisibility(
            visible = fabState.value == MultiFabState.Expanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut()
        ) {
            LazyColumn(
                modifier = Modifier.wrapContentSize(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {

                itemsIndexed(itemsMultiFab) { _, item ->
                    MiniFabItem(
                        item = item,
                        showLabel = fabOption.showLabels,
                        miniFabColor = Color.Blue,
                        onFabItemClicked = { onFabItemClicked(item) })
                }

                item {}
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (fabState.value.isExpanded() && showFabTitle)
                Text(text = fabTitle!!, modifier = Modifier.padding(end = 16.dp), fontSize = 12.sp)
            FloatingActionButton(
                onClick = {
                    fabState.value = fabState.value.toggleValue()
                    stateChanged(fabState.value)
                },
                backgroundColor = fabOption.backgroundTint,
                contentColor = fabOption.iconTint
            ) {
                Icon(
                    painter =
                    if (fabState.value.isExpanded()) painterResource(fabIcon.iconResAfterRotate)
                    else painterResource(fabIcon.iconRes),
                    modifier = Modifier.rotate(rotation),
                    contentDescription = null,
                    tint = fabOption.iconTint
                )
            }
        }
    }
}

@Preview
@Composable
fun MultiFloatingActionButtonPreview() {
    MultiFloatingActionButton(
        fabIcon = FabIcon(R.drawable.plus, R.drawable.plus, 90f),
        fabOption = FabOption(
            iconTint = MaterialTheme.colors.onPrimary,
            showLabels = true,
            backgroundTint = MaterialTheme.colors.primary,
        ),
        itemsMultiFab = listOf(
            MultiFabItem(
                icon = R.drawable.ic_directional,
                label = "Directional Order",
                labelColor = MaterialTheme.colors.onPrimary
            ),
            MultiFabItem(
                icon = R.drawable.ic_promotional,
                label = "Promotional Order",
                labelColor = MaterialTheme.colors.onPrimary
            ),
        ),
        onFabItemClicked = { println(it) },
        fabTitle = "MultiFloatActionButton",
        showFabTitle = false
    )
}

