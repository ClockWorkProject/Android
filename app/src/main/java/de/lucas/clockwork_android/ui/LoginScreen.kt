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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.lucas.clockwork_android.R

@Composable
internal fun LoginScreen(
    viewModel: LoginViewModel,
    onClickLogin: () -> Unit,
    onClickSignUp: () -> Unit
) {
    Scaffold {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppLogo()
            Column(
                modifier = Modifier.padding(top = 40.dp, start = 32.dp, end = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedStyledErrorText(
                    id = R.string.email,
                    optText = null,
                    maxLines = 1,
                    loginState = viewModel.getLogin(),
                    signUpState = viewModel.getSignUp(),
                    errorState = viewModel.getIsError(),
                    viewModel = viewModel,
                    login = onClickLogin,
                    signUp = { onClickSignUp() }
                )
                OutlinedStyledText(
                    id = R.string.password,
                    optText = null,
                    padding = 24,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    isSingleLine = true
                )
                RoundedButton(
                    id = R.string.login,
                    padding = 40,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.setLogin(true) }
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
                    onClick = { viewModel.setSignUp(true) }
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
    maxLines: Int,
    isSingleLine: Boolean
) {
    var text by remember { mutableStateOf(optText ?: "") }
    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(stringResource(id = id)) },
        modifier = modifier.padding(top = padding.dp),
        maxLines = maxLines,
        singleLine = isSingleLine
    )
}

@Composable
fun OutlinedStyledErrorText(
    @StringRes id: Int,
    optText: String?,
    maxLines: Int,
    loginState: Boolean,
    signUpState: Boolean,
    errorState: Boolean,
    viewModel: LoginViewModel,
    login: () -> Unit,
    signUp: () -> Unit
) {
    var text by remember { mutableStateOf(optText ?: "") }
    val regexEmail = Regex("[A-Za-z]+[a-zA-Z0-9]*([@])(.+)(\\.)([a-zA-Z]+)")

    fun validateLogin(text: String) {
        /* TODO login -> if error from server show errorMessage */
        if (text.isEmpty()) {
            viewModel.setError(true)
        } else {
            login()
        }
    }

    fun validateSignUp(text: String) {
        if (text.matches(regexEmail)) {
            viewModel.setUsername(text)
            signUp()
        } else {
            viewModel.setError(true)
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
            },
            label = { Text(stringResource(id = id)) },
            trailingIcon = {
                if (errorState)
                    Icon(
                        painterResource(id = R.drawable.ic_error),
                        "",
                        tint = MaterialTheme.colors.error
                    )
            },
            isError = errorState,
            maxLines = maxLines,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (errorState) {
            Text(
                text = stringResource(id = R.string.error_message_login),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        if (loginState) {
            validateLogin(text)
            viewModel.setLogin(false)
        }
        if (signUpState) {
            validateSignUp(text)
            viewModel.setSignUp(false)
        }
    }
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
    LoginScreen(LoginViewModel(LocalContext.current), {}, {})
}