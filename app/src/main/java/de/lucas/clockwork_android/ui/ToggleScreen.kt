package de.lucas.clockwork_android.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.lucas.clockwork_android.R
import de.lucas.clockwork_android.model.Toggle
import de.lucas.clockwork_android.model.TotalToggle

// For testing purpose
val listOfToggles =
    listOf(
        TotalToggle(
            "Heute",
            "3 Std. 20 Min.",
            listOf(Toggle("Daily", "Meetings", "00:35:00"), Toggle("Bug Fix", "Vinson", "01:12:00"))
        ), TotalToggle(
            "Gestern",
            "7 Std. 55 Min.",
            listOf(Toggle("Daily", "Meetings", "00:35:00"), Toggle("Bug Fix", "Vinson", "01:12:00"))
        )
    )

@ExperimentalMaterialApi
@Composable
internal fun ToggleScreen() {
    Scaffold(topBar = { TopAppBar(title = stringResource(id = R.string.time_record)) }) {
        val viewModel = ToggleViewModel()
        var showToggleList by remember { mutableStateOf(false) }
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
                    showToggleList = true
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
        } else {
            ToggleList(listOfToggles)
        }
        if (showToggleList) ToggleList(listOfToggles)
    }
}

@ExperimentalMaterialApi
@Composable
internal fun ToggleList(list: List<TotalToggle>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(list) { toggle ->
            ToggleItem(toggle = toggle)
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun ToggleListPreview() {
    ToggleList(list = listOfToggles)
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

@ExperimentalMaterialApi
@Preview
@Composable
private fun PreviewToggleScreen() {
    ToggleScreen()
}