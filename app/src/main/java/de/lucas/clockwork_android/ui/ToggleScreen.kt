package de.lucas.clockwork_android.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.lucas.clockwork_android.R
import de.lucas.clockwork_android.model.TotalToggle

/**
 * If user is member of a group -> show list of all his previous toggles and his current toggle
 * If user is not a member of a group -> show empty state message
 * @param toggleList list of all toggles from the user
 * @param onJoinGroup callBack with String, to provide groupID when group joined
 */
@ExperimentalMaterialApi
@Composable
internal fun ToggleScreen(
    toggleList: List<TotalToggle>,
    viewModel: ToggleViewModel,
    onJoinGroup: (String) -> Unit
) {
    Scaffold {
        // Check if user is member of a group (-1 -> no member, else -> member of a group)
        if (viewModel.getGroupId() == "" && viewModel.showEmptyState.value) {
            // 2 states for button to show join or create group dialog -> gets set true if button clicked
            var showJoinDialog by remember { mutableStateOf(false) }
            var showCreateDialog by remember { mutableStateOf(false) }

            // if true show join group dialog
            if (showJoinDialog) {
                CustomDialog(
                    title_id = R.string.join_group,
                    message_id = R.string.enter_group_id,
                    button_text_id = R.string.join,
                    onClickDismiss = { showJoinDialog = false }
                ) { input ->
                    viewModel.joinGroup("-Mtnv0WvAajXbKU0Wh5I")
                    showJoinDialog = false
                    onJoinGroup("-Mtnv0WvAajXbKU0Wh5I") // Change to input later!!!!!
                }
            }
            // if true show create group dialog
            if (showCreateDialog) {
                CustomDialog(
                    title_id = R.string.create_group,
                    message_id = R.string.enter_group_name,
                    button_text_id = R.string.create,
                    onClickDismiss = { showCreateDialog = false }
                ) { input ->
                    viewModel.createGroup(input)
                    viewModel.showToggleList.value = true
                    showCreateDialog = false
                    viewModel.showEmptyState.value = false
                    onJoinGroup(viewModel.getGroupId()!!)
                }
            }
            // Empty state message
            NoGroupScreen({ showJoinDialog = true }, { showCreateDialog = true })

            if (viewModel.noGroupFoundState.value) {
                CustomSnackBar(id = R.string.no_group_found) {
                    viewModel.setNoGroupFound(false)
                }
            }
        } else {
            ToggleList(toggleList, false)
        }
        // Check state if user creates or joins a group -> show list instead of empty state
        if (viewModel.showToggleList.value) ToggleList(toggleList, false)
    }
}

// List with all toggles of user
@ExperimentalMaterialApi
@Composable
internal fun ToggleList(list: List<TotalToggle>, smallText: Boolean) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(list) { toggle ->
            ToggleItem(toggle = toggle, smallText)
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
    var errorState by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = { },
        title = {
            Text(
                stringResource(id = title_id),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(stringResource(id = message_id)) },
                    singleLine = true,
                    maxLines = 1
                )
                if (errorState) {
                    Text(
                        text = stringResource(id = R.string.error_message_text_empty),
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (text.isNotEmpty()) {
                    errorState = false
                    onClickConfirm(text)
                } else {
                    errorState = true
                }
            }) {
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

/**
 * SnackBar to show, when joining group failed
 */
@Composable
fun CustomSnackBar(@StringRes id: Int, onClick: () -> Unit) {
    Snackbar(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = id),
                modifier = Modifier.weight(8f)
            )
            IconButton(
                onClick = { onClick() },
                modifier = Modifier.weight(1f)
            ) {
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
fun ToggleListPreview() {
    ToggleList(list = listOf(), smallText = false)
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun PreviewToggleScreen() {
    ToggleScreen(listOf(), ToggleViewModel(LocalContext.current)) { }
}

@Preview
@Composable
private fun PreviewCustomDialog() {
    CustomDialog(
        title_id = R.string.join_group,
        message_id = R.string.enter_group_id,
        button_text_id = R.string.join,
        onClickDismiss = { },
        onClickConfirm = { }
    )
}