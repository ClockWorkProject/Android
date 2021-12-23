package de.lucas.clockwork_android.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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

@ExperimentalMaterialApi
@Composable
internal fun ToggleItem(toggle: TotalToggle) {
    var expandableState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (expandableState) 180f else 0f)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    delayMillis = 100,
                    easing = LinearOutSlowInEasing
                )
            )
            .padding(top = 8.dp),
        shape = RoundedCornerShape(12.dp),
        indication = null,
        onClick = {
            expandableState = !expandableState
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = toggle.date,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(4f)
                )
                Text(text = toggle.total_time, fontSize = 18.sp, modifier = Modifier.weight(3f))
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
            if (expandableState) {
                toggle.toggle_list.forEach { item -> ToggleEntryItem(toggle = item) }
            }
        }
    }
}

@Composable
private fun ToggleEntryItem(toggle: Toggle) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(3f)) {
            Text(text = toggle.issue, fontSize = 20.sp)
            Text(text = toggle.project)
        }
        Text(text = toggle.time, fontSize = 18.sp, modifier = Modifier.weight(1f))
    }
}