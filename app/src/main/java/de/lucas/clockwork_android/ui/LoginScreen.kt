package de.lucas.clockwork_android.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.lucas.clockwork_android.R

@Composable
internal fun LoginScreen(onClickLogin: () -> Unit, onClickSignUp: () -> Unit) {
    Scaffold {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppLogo()
            Column(
                modifier = Modifier.padding(top = 40.dp, start = 32.dp, end = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedStyledText(
                    id = R.string.email,
                    optText = null,
                    padding = 0,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )
                OutlinedStyledText(
                    id = R.string.password,
                    optText = null,
                    padding = 24,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )
                RoundedButton(
                    id = R.string.login,
                    padding = 40,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onClickLogin
                )
                Text(
                    text = stringResource(id = R.string.no_account),
                    modifier = Modifier.padding(top = 32.dp)
                )
                RoundedButton(
                    id = R.string.sign_up,
                    padding = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    onClick = onClickSignUp
                )
            }
        }
    }
}

@Composable
fun AppLogo() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "",
                modifier = Modifier.size(124.dp)
            )
            Text(
                text = stringResource(id = R.string.app_name),
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
fun OutlinedStyledText(
    @StringRes id: Int,
    optText: String?,
    padding: Int,
    modifier: Modifier,
    maxLines: Int
) {
    var text by remember { mutableStateOf("") }
    if (optText != null) {
        text = optText
    }
    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(stringResource(id = id)) },
        modifier = modifier.padding(top = padding.dp),
        maxLines = maxLines
    )
}

@Composable
fun RoundedButton(@StringRes id: Int, padding: Int, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(30.dp),
        modifier = modifier.padding(top = padding.dp)
    ) {
        Text(text = stringResource(id = id), fontSize = 18.sp)
    }
}

@Preview
@Composable
private fun LoginPreview() {
    LoginScreen({}, {})
}