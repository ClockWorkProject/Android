package de.lucas.clockwork_android.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import de.lucas.clockwork_android.R

@Composable
internal fun ToggleScreen() {
    Scaffold(topBar = { TopAppBar(title = stringResource(id = R.string.time_record)) }) {
        val viewModel = ToggleViewModel()
        if (viewModel.groupID == -1) {
            var showJoinDialog by remember { mutableStateOf(false) }
            var showCreateDialog by remember { mutableStateOf(false) }

            if (showJoinDialog) {
                CustomDialog(
                    title_id = R.string.join_group,
                    message_id = R.string.enter_group_id,
                    button_text_id = R.string.join,
                    onClickDismiss = { showJoinDialog = false }
                ) { input ->
                    /* TODO send to backend */
                    showJoinDialog = false
                }
            }
            if (showCreateDialog) {
                CustomDialog(
                    title_id = R.string.create_group,
                    message_id = R.string.enter_group_name,
                    button_text_id = R.string.create,
                    onClickDismiss = { showCreateDialog = false }
                ) { input ->
                    /* TODO send to backend */
                    showCreateDialog = false
                }
            }
            NoGroupScreen({ showJoinDialog = true }, { showCreateDialog = true })
        }
    }
}

@Composable
fun CustomDialog(
    @StringRes title_id: Int,
    @StringRes message_id: Int,
    @StringRes button_text_id: Int,
    onClickDismiss: () -> Unit,
    onClickConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = { },
        title = {
            Column {
                Text(
                    stringResource(id = title_id),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(stringResource(id = message_id)) }
            )
        },
        confirmButton = {
            Button(onClick = { onClickConfirm(text) }) {
                Text(text = stringResource(id = button_text_id))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onClickDismiss() },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
            ) {
                Text(
                    text = stringResource(id = R.string.close),
                    color = MaterialTheme.colors.primary
                )
            }
        })
}

@Preview
@Composable
private fun PreviewToggleScreen() {
    ToggleScreen()
}