package de.lucas.clockwork_android.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.lucas.clockwork_android.R
import de.lucas.clockwork_android.model.Issue
import de.lucas.clockwork_android.ui.BoardState.OPEN
import de.lucas.clockwork_android.ui.theme.Purple200

@ExperimentalMaterialApi
@Composable
internal fun TogglePlayer(issue: Issue, onClose: () -> Unit) {
    var pauseState by remember { mutableStateOf(false) }
    val stateVisibility = if (pauseState) 1f else 0.6f
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        backgroundColor = Purple200,
        contentColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(3f)
            ) {
                Text(
                    text = issue.title,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = issue.project_name,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            /* TODO count timer */
            Text(text = "00:23:45", fontSize = 18.sp, modifier = Modifier.weight(1.5f))
            Box(modifier = Modifier.weight(1f)) {
                if (pauseState) {
                    IconButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            /* TODO continue time counter */
                            pauseState = false
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_play_light),
                            contentDescription = "",
                            modifier = Modifier
                        )
                    }
                } else {
                    IconButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            /* TODO pause time counter */
                            pauseState = true
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_pause_light),
                            contentDescription = ""
                        )
                    }
                }
            }
            IconButton(modifier = Modifier
                .weight(1f)
                .alpha(stateVisibility),
                onClick = {
                    if (pauseState) {
                        /* TODO enable clickable to stop toggle */
                        onClose()
                    }
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close_light),
                    contentDescription = ""
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun PreviewTogglePlayer() {
    TogglePlayer(issue = Issue(2, "Bug Fix", "Vinson", "", "", OPEN)) { }
}