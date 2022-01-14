package de.lucas.clockwork_android.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
internal fun TogglePlayer(
    issue: Issue,
    time: String,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onClose: () -> Unit,
    viewModel: TogglePlayerViewModel
) {
    val stateVisibility = if (viewModel.getIsPaused()) 1f else 0.6f
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
            Text(
                text = time,
                fontSize = 18.sp,
                modifier = Modifier.weight(1.5f)
            )
            Box(modifier = Modifier.weight(1f)) {
                if (viewModel.getIsPaused()) {
                    IconButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onResume()
                            viewModel.setIsPaused(false)
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
                            viewModel.setIsPaused(true)
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
                    if (viewModel.getIsPaused()) {
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
    TogglePlayer(
        issue = Issue(2, "Bug Fix", "Vinson", "", "", OPEN),
        time = "00:00:12",
        onPause = {},
        onResume = {},
        onClose = {},
        viewModel = TogglePlayerViewModel(LocalContext.current)
    )
}