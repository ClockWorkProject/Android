package de.lucas.clockwork_android.ui

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.firebase.auth.ktx.auth
import de.lucas.clockwork_android.R
import de.lucas.clockwork_android.viewmodel.State

/**
 * Screen for the user to login or sign up
 * @param auth authenticator to use firebase calls
 * @param onClickLogin callBack with 2 Strings -> groupId (if user is in a group) and role of the user, to call functions, that are only for admins
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
internal fun LoginScreen(
    isLoading: Boolean,
    currentState: State,
    password: String,
    email: String,
    setEmail: (String) -> Unit,
    setState: (State) -> Unit,
    setPassword: (String) -> Unit,
    loginUser: () -> Unit,
    signUpUser: () -> Unit
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
                    maxLines = 1,
                    currentState = currentState,
                    email = email,
                    setEmail = { email -> setEmail(email) },
                    setState = { state -> setState(state) },
                    loginUser = { loginUser() },
                    signUpUser = { signUpUser() }
                )
                OutlinedStyledTextPassword(
                    id = R.string.password,
                    padding = 24,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    isSingleLine = true,
                    password = password,
                    setPassword = setPassword
                )
                RoundedButton(
                    id = R.string.login,
                    padding = 40,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { setState(State.LOGIN) }
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
                    onClick = { setState(State.SIGNUP) }
                )
            }
        }
        // Show loading indicator if "isLoading" is true
        if (isLoading) {
            LoadingIndicator(id = R.string.login_loading)
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

/**
 * Custom OutlinedTextField for password
 * Provides an icon to toggle visibility of input
 *
 */
@Composable
fun OutlinedStyledTextPassword(
    @StringRes id: Int,
    padding: Int,
    modifier: Modifier,
    maxLines: Int,
    isSingleLine: Boolean,
    password: String,
    setPassword: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val icon = if (passwordVisible) {
        painterResource(id = R.drawable.ic_pw_visible)
    } else {
        painterResource(id = R.drawable.ic_pw_invisible)
    }
    OutlinedTextField(
        value = password,
        onValueChange = { setPassword(it) },
        label = { Text(stringResource(id = id)) },
        modifier = modifier.padding(top = padding.dp),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(painter = icon, contentDescription = "")
            }
        },
        maxLines = maxLines,
        singleLine = isSingleLine,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}

/**
 * Custom OutlinedTextField with validations of user input, to react to inputs (error, login, signup)
 */
@Composable
fun OutlinedStyledErrorText(
    @StringRes id: Int,
    maxLines: Int,
    currentState: State,
    email: String,
    setEmail: (String) -> Unit,
    setState: (State) -> Unit,
    loginUser: () -> Unit,
    signUpUser: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = email,
            onValueChange = {
                setEmail(it)
            },
            label = { Text(stringResource(id = id)) },
            trailingIcon = {
                if (currentState == State.ERROR)
                    Icon(
                        painterResource(id = R.drawable.ic_error),
                        "",
                        tint = MaterialTheme.colors.error
                    )
            },
            isError = currentState == State.ERROR,
            maxLines = maxLines,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (currentState == State.ERROR) {
            Text(
                text = stringResource(id = R.string.error_message_login),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        if (currentState == State.LOGIN) {
            loginUser()
            setState(State.DEFAULT)
        }
        if (currentState == State.SIGNUP) {
            signUpUser()
            setState(State.DEFAULT)
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

@Composable
internal fun LoadingIndicator(@StringRes id: Int) {
    Dialog(
        onDismissRequest = { },
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Box(
            contentAlignment = Center,
            modifier = Modifier.background(White, shape = RoundedCornerShape(8.dp))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Text(text = stringResource(id = id))
            }
        }
    }
}

@Preview
@Composable
private fun LoginPreview() {
    LoginScreen(
        isLoading = false,
        currentState = State.DEFAULT,
        password = "",
        email = "",
        setEmail = {},
        setState = {},
        setPassword = {},
        loginUser = {},
        signUpUser = {}
    )
}