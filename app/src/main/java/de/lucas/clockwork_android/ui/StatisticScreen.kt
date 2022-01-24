package de.lucas.clockwork_android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import de.lucas.clockwork_android.R
import de.lucas.clockwork_android.model.TotalToggle
import de.lucas.clockwork_android.model.UserStatistic
import de.lucas.clockwork_android.ui.theme.Gray200

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
internal fun StatisticScreen(role: String, userList: List<UserStatistic>) {
    var showUserStatistic by remember { mutableStateOf(false) }
    var clickedUser by remember { mutableStateOf(UserStatistic("", listOf())) }
    Scaffold {
        if (role == "admin") {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(userList) { user ->
                    UserItem(user = user) {
                        clickedUser = user
                        showUserStatistic = true
                    }
                }
            }
            if (showUserStatistic) {
                UserStatisticScreen(
                    username = clickedUser.username,
                    toggleList = clickedUser.totalToggles
                ) {
                    showUserStatistic = false
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.empty_state_statistic),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun UserItem(user: UserStatistic, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Text(
            text = user.username,
            modifier = Modifier
                .padding(16.dp)
                .weight(1f),
            style = TextStyle(fontSize = 18.sp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_right_dark),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.Black),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 16.dp)
        )
    }
    Divider(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
        thickness = 1.dp
    )
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
private fun UserStatisticScreen(
    username: String,
    toggleList: List<TotalToggle>,
    onClose: () -> Unit
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { onClose() }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            color = Gray200
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onClose() }, modifier = Modifier.padding(end = 8.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = ""
                        )
                    }
                    Text(
                        text = username,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                }
                ToggleList(list = toggleList, smallText = true)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterialApi
@Preview
@Composable
internal fun PreviewStatisticScreen() {
    StatisticScreen("member", listOf())
}