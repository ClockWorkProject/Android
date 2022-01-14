package de.lucas.clockwork_android.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.lucas.clockwork_android.R

/**
 * Empty State Message which if shown if user is no member of a group
 */
@Composable
internal fun NoGroupScreen(
    onClickJoinGroup: () -> Unit,
    onClickCreateGroup: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(id = R.string.no_group_empty_state_message),
            textAlign = TextAlign.Center
        )
        Button(
            onClick = { onClickJoinGroup() },
            modifier = Modifier.padding(top = 32.dp)
        ) {
            Text(
                stringResource(id = R.string.join_group)
            )
        }
        Button(
            onClick = { onClickCreateGroup() },
            modifier = Modifier.padding(top = 32.dp)
        ) {
            Text(
                stringResource(id = R.string.create_group)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewNoGroupScreen() {
    NoGroupScreen({ }, { })
}