package de.lucas.clockwork_android.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
import de.lucas.clockwork_android.model.Project
import de.lucas.clockwork_android.ui.BoardState.OPEN
import de.lucas.clockwork_android.ui.theme.Purple200

/**
 * Player, which is shown when toggles is active
 * Lets user stop, start and close (finish) toggle
 * @param issue and project to get info to show in player
 * @param time current toggle time that is shown in der player
 */
@ExperimentalMaterialApi
@Composable
internal fun TogglePlayer(
    issue: Issue,
    project: Project,
    time: String,
    isPaused: Boolean,
    setIsPaused: (Boolean) -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onClose: () -> Unit
) {
    val stateVisibility = if (isPaused) 1f else 0.6f
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                    text = "#${issue.number} ${issue.name}",
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = project.project_name,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = time,
                fontSize = 18.sp,
                modifier = Modifier.weight(1.5f)
            )
            Box(modifier = Modifier.weight(1f)) {
                if (isPaused) {
                    IconButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onResume()
                            setIsPaused(false)
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
                            onPause()
                            setIsPaused(true)
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
                    if (isPaused) {
                        onClose()
                        setIsPaused(false)
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
    TogglePlayer(
        issue = Issue("w", "Bug Fix", "Vinson", "", OPEN),
        project = Project("", "Project", listOf()),
        time = "00:00:12",
        isPaused = false,
        setIsPaused = {},
        onPause = {},
        onResume = {},
        onClose = {}
    )
}