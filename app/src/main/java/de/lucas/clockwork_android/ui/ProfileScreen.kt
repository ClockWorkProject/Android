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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import de.lucas.clockwork_android.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun ProfileScreen(
    onClickInfo: () -> Unit,
    onClickLogout: () -> Unit,
    onClickLeave: () -> Unit
) {
    var showLeaveDialogState by remember { mutableStateOf(false) }
    var showDeleteDialogState by remember { mutableStateOf(false) }
    var showEditDialogState by remember { mutableStateOf(false) }
    Scaffold {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (group, name, edit_icon, buttons) = createRefs()
                Text(
                    text = stringResource(id = R.string.current_group, "Müller & Wulff GmbH"),
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
                    text = stringResource(id = R.string.profile_username, "Mattis Uphoff"),
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
                IconButton(onClick = {
                    /* TODO edit name, get name from sharedPrefs (must be implemented) */
                    showEditDialogState = true
                }, modifier = Modifier.constrainAs(edit_icon) {
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
                        onClick = { showLeaveDialogState = true },
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
                        onClick = { showDeleteDialogState = true },
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
        if (showLeaveDialogState) {
            ProfileDialog(
                username = null,
                title_id = R.string.leave_group,
                message_id = R.string.leave_message,
                button_text_id = R.string.leave,
                onClickDismiss = { showLeaveDialogState = false },
                onClickConfirm = {
                    /* TODO leave group -> set group id to -1 */
                    onClickLeave()
                }
            )
        }

        if (showDeleteDialogState) {
            ProfileDialog(
                username = null,
                title_id = R.string.delete_profile,
                message_id = R.string.delete_message,
                button_text_id = R.string.delete,
                onClickDismiss = { showDeleteDialogState = false },
                onClickConfirm = { /* TODO leave group -> delete profile request to backend -> navigate to login  */ }
            )
        }
        if (showEditDialogState) {
            ProfileDialog(
                username = "Mattis Uphoff",
                title_id = R.string.edit_username,
                message_id = null,
                button_text_id = R.string.save,
                onClickDismiss = { showEditDialogState = false },
                onClickConfirm = {
                    /* TODO save new name and change in profile */
                    showEditDialogState = false
                }
            )
        }
    }
}

@Composable
fun ProfileDialog(
    username: String?,
    @StringRes title_id: Int,
    @StringRes message_id: Int?,
    @StringRes button_text_id: Int,
    onClickDismiss: () -> Unit,
    onClickConfirm: () -> Unit
) {
    var text by remember { mutableStateOf(username ?: "") }
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
        },
        confirmButton = {
            Button(onClick = { onClickConfirm() }) {
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
    ProfileScreen({ }, { }, { })
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