package de.lucas.clockwork_android.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
internal fun ProfileScreen(
    groupName: String,
    groupId: String,
    userName: String,
    showEditDialog: Boolean,
    showDeleteDialog: Boolean,
    showLeaveDialog: Boolean,
    setEditDialog: (Boolean) -> Unit,
    setDeleteDialog: (Boolean) -> Unit,
    setLeaveDialog: (Boolean) -> Unit,
    setGroupName: () -> Unit,
    setUserName: (String) -> Unit,
    logout: () -> Unit,
    leaveGroup: () -> Unit,
    onClickInfo: () -> Unit,
    onClickLogout: () -> Unit,
    onClickLeave: () -> Unit
) {
    val context = LocalContext.current
    // Get name of current group from database
    setGroupName()
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (group, name, info_icon, edit_icon, buttons) = createRefs()
                Text(
                    text = stringResource(
                        id = R.string.current_group,
                        if (groupName == "") "-" else groupName
                    ),
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
                if (groupId != "") {
                    IconButton(
                        onClick = { shareGroup(groupId, context) },
                        modifier = Modifier.constrainAs(info_icon) {
                            start.linkTo(group.end)
                            bottom.linkTo(group.bottom)
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_share),
                            contentDescription = ""
                        )
                    }
                }
                Text(
                    text = stringResource(
                        id = R.string.profile_username,
                        userName
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
                    onClick = { setEditDialog(true) },
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
                        onClick = { setLeaveDialog(true) },
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.leave_group))
                    }
                    Button(
                        onClick = {
                            onClickLogout()
                            logout()
                        },
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.sign_out))
                    }
                    Button(
                        onClick = { setDeleteDialog(true) },
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
        if (showLeaveDialog) {
            ProfileDialog(
                username = "-",
                title_id = R.string.leave_group,
                message_id = R.string.leave_message,
                button_text_id = R.string.leave,
                onClickDismiss = { setLeaveDialog(false) },
                onClickConfirm = {
                    // Reset all necessary preference data and navigate to ToggleScreen
                    leaveGroup()
                    onClickLeave()
                }
            )
        }
        if (showDeleteDialog) {
            ProfileDialog(
                username = "-",
                title_id = R.string.delete_profile,
                message_id = R.string.delete_message,
                button_text_id = R.string.delete,
                onClickDismiss = { setDeleteDialog(false) },
                onClickConfirm = { /* TODO leave group -> delete profile request to backend -> navigate to login  */ }
            )
        }
        if (showEditDialog) {
            ProfileDialog(
                username = userName,
                title_id = R.string.edit_username,
                message_id = null,
                button_text_id = R.string.save,
                onClickDismiss = { setEditDialog(false) },
                onClickConfirm = { name ->
                    setUserName(name)
                    setEditDialog(false)
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
                    onValueChange = { text = it },
                    singleLine = true,
                    maxLines = 1
                )
            }
            if (errorState) {
                Text(
                    text = stringResource(id = R.string.error_message_text_empty),
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

/**
 * Sharesheet for sharing the groupId of current group, so others can join the group
 */
private fun shareGroup(groupId: String, context: Context) {
    val share = Intent.createChooser(Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, groupId)
        putExtra(Intent.EXTRA_TITLE, context.getString(R.string.invite_group))
        type = "text/plain"
    }, null)
    context.startActivity(share)
}

@Preview
@Composable
private fun PreviewProfileScreen() {
    ProfileScreen(
        groupName = "",
        groupId = "",
        userName = "",
        showEditDialog = false,
        showDeleteDialog = false,
        showLeaveDialog = false,
        setEditDialog = {},
        setDeleteDialog = {},
        setLeaveDialog = {},
        setGroupName = {},
        setUserName = {},
        logout = {},
        leaveGroup = {},
        onClickInfo = {},
        onClickLogout = {}) {}
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