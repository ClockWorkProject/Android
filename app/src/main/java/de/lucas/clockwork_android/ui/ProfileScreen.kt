package de.lucas.clockwork_android.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import de.lucas.clockwork_android.R

/**
 * Screen of Profile -> shows information of user/current group and provides different actions
 * @param onClickInfo on "info"-button click -> navigate to InfoScreen where information about the app is being found
 * @param onClickLogout on "logout"-button click -> logout user -> navigate back to LoginScreen
 * @param onClickLeave on "leave"-click -> leave group -> navigate to ToggleScreen and show empty state message
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun ProfileScreen(
    viewModel: ProfileViewModel,
    onClickInfo: () -> Unit,
    onClickLogout: () -> Unit,
    onClickLeave: () -> Unit
) {
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (group, name, edit_icon, buttons) = createRefs()
                Text(
                    text = stringResource(id = R.string.current_group, viewModel.getGroupName()!!),
                    fontSize = 18.sp,
                    lineHeight = 32.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .constrainAs(ref = group) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
                Text(
                    text = stringResource(
                        id = R.string.profile_username,
                        viewModel.getUsername()!!
                    ),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp,
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .constrainAs(ref = name) {
                            top.linkTo(group.bottom)
                            start.linkTo(group.start)
                            end.linkTo(group.end)
                        }
                )
                IconButton(
                    onClick = { viewModel.setEditDialog(true) },
                    modifier = Modifier.constrainAs(edit_icon) {
                        start.linkTo(name.end)
                        bottom.linkTo(name.bottom)
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit_dark),
                        contentDescription = ""
                    )
                }
                Column(
                    modifier = Modifier
                        .constrainAs(buttons) {
                            top.linkTo(name.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(top = 48.dp)
                        .width(IntrinsicSize.Max),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = { onClickInfo() },
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.info))
                    }
                    Button(
                        onClick = { viewModel.setLeaveDialog(true) },
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.leave_group))
                    }
                    Button(
                        onClick = { onClickLogout() },
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.sign_out))
                    }
                    Button(
                        onClick = { viewModel.setDeleteDialog(true) },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.delete_profile),
                            color = Color.White
                        )
                    }
                }
            }
        }
        /**
         * Check if states are true to show according dialog
         */
        if (viewModel.showLeaveDialogState.value) {
            ProfileDialog(
                username = null,
                title_id = R.string.leave_group,
                message_id = R.string.leave_message,
                button_text_id = R.string.leave,
                onClickDismiss = { viewModel.setLeaveDialog(false) },
                onClickConfirm = {
                    /* TODO leave group -> set group id to -1 */
                    onClickLeave()
                }
            )
        }
        if (viewModel.showDeleteDialogState.value) {
            ProfileDialog(
                username = null,
                title_id = R.string.delete_profile,
                message_id = R.string.delete_message,
                button_text_id = R.string.delete,
                onClickDismiss = { viewModel.setDeleteDialog(false) },
                onClickConfirm = { /* TODO leave group -> delete profile request to backend -> navigate to login  */ }
            )
        }
        if (viewModel.showEditDialogState.value) {
            ProfileDialog(
                username = viewModel.getUsername(),
                title_id = R.string.edit_username,
                message_id = null,
                button_text_id = R.string.save,
                onClickDismiss = { viewModel.setEditDialog(false) },
                onClickConfirm = { name ->
                    viewModel.updateUsername(name)
                    viewModel.setEditDialog(false)
                }
            )
        }
    }
}

// Custom Dialog for ProfileScreen actions
@Composable
fun ProfileDialog(
    username: String?,
    @StringRes title_id: Int,
    @StringRes message_id: Int?,
    @StringRes button_text_id: Int,
    onClickDismiss: () -> Unit,
    onClickConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf(username ?: "") }
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
            if (message_id != null) {
                Text(stringResource(id = message_id))
            } else {
                TextField(
                    value = text,
                    onValueChange = { text = it }
                )
            }
            if (errorState) {
                Text(
                    text = stringResource(id = R.string.error_message_edit_name),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
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

@Preview
@Composable
private fun PreviewProfileScreen() {
    ProfileScreen(ProfileViewModel(LocalContext.current), { }, { }, { })
}

@Preview
@Composable
private fun PreviewProfileDialog() {
    ProfileDialog(
        username = null,
        title_id = R.string.leave_group,
        message_id = R.string.leave_message,
        button_text_id = R.string.leave,
        onClickDismiss = { },
        onClickConfirm = { }
    )
}