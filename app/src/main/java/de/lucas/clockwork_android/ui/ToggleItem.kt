package de.lucas.clockwork_android.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.lucas.clockwork_android.R
import de.lucas.clockwork_android.model.Toggle
import de.lucas.clockwork_android.model.TotalToggle
import de.lucas.clockwork_android.ui.utility.toCurrentDate

/**
 * Item to populate the ToggleList
 * Shows date and total toggled time of the date
 * Can get expanded to show all individual toggles
 */
@ExperimentalMaterialApi
@Composable
internal fun ToggleItem(toggle: TotalToggle, smallText: Boolean) {
    var expandableState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (expandableState) 180f else 0f)
    Card(
        onClick = { expandableState = !expandableState },
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    delayMillis = 100,
                    easing = LinearOutSlowInEasing
                )
            )
            .padding(top = 8.dp),
        true,
        shape = RoundedCornerShape(12.dp),
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = contentColorFor(backgroundColor),
        border = null,
        elevation = 1.dp,
        interactionSource = remember { MutableInteractionSource() }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = toggle.date.toCurrentDate(),
                    fontSize = if (smallText) 18.sp else 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(4f)
                )
                Text(
                    text = toggle.totalTime,
                    fontSize = if (smallText) 16.sp else 18.sp,
                    modifier = Modifier.weight(3f)
                )
                IconButton(modifier = Modifier
                    .weight(1f)
                    .rotate(rotationState),
                    onClick = {
                        expandableState = !expandableState
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_down_dark),
                        contentDescription = ""
                    )
                }
            }
            // Expand and show all individual toggles
            if (expandableState) {
                toggle.toggleList.forEach { item ->
                    ToggleEntryItem(
                        toggle = item,
                        smallText = smallText
                    )
                }
            }
        }
    }
}

/**
 * Item for the expandable List
 */
@Composable
private fun ToggleEntryItem(toggle: Toggle, smallText: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(3f)) {
            Text(text = toggle.issueName, fontSize = if (smallText) 18.sp else 20.sp)
            Text(text = toggle.projectName)
        }
        Text(
            text = toggle.issueTime,
            fontSize = if (smallText) 16.sp else 18.sp,
            modifier = Modifier.weight(1f)
        )
    }
}