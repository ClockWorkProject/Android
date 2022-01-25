package de.lucas.clockwork_android.ui

import androidx.activity.ComponentActivity
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
import androidx.compose.ui.platform.LocalContext
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.lucas.clockwork_android.R

/**
 * Screen for the user to login or sign up
 * @param auth authenticator to use firebase calls
 * @param onClickLogin callBack with 2 Strings -> groupId (if user is in a group) and role of the user, to call functions, that are only for admins
 */
@Composable
internal fun LoginScreen(
    viewModel: LoginViewModel,
    auth: FirebaseAuth,
    onClickLogin: (String, String) -> Unit,
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
                    maxLines = 1,
                    loginState = viewModel.getLogin(),
                    signUpState = viewModel.getSignUp(),
                    errorState = viewModel.getIsError(),
                    viewModel = viewModel,
                    auth = auth,
                    login = { groupID, role -> onClickLogin(groupID, role) },
                    signUp = { onClickSignUp() }
                )
                OutlinedStyledTextPassword(
                    id = R.string.password,
                    padding = 24,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    isSingleLine = true,
                    viewModel = viewModel
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
        // Show loading indicator if "isLoading" is true
        if (viewModel.getIsLoading()) {
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
    viewModel: LoginViewModel
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val icon = if (passwordVisible) {
        painterResource(id = R.drawable.ic_pw_visible)
    } else {
        painterResource(id = R.drawable.ic_pw_invisible)
    }
    OutlinedTextField(
        value = viewModel.getPassword(),
        onValueChange = { viewModel.setPassword(it) },
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
    loginState: Boolean,
    signUpState: Boolean,
    errorState: Boolean,
    viewModel: LoginViewModel,
    auth: FirebaseAuth,
    login: (String, String) -> Unit,
    signUp: () -> Unit
) {
    fun validateLogin() {
        viewModel.loginUser(auth, ComponentActivity(), login)
    }

    fun validateSignUp() {
        viewModel.signUpUser(auth, ComponentActivity(), signUp)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = viewModel.getEmail(),
            onValueChange = {
                viewModel.setEmail(it)
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
            validateLogin()
            viewModel.setLogin(false)
        }
        if (signUpState) {
            validateSignUp()
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
        LoginViewModel(LocalContext.current),
        Firebase.auth,
        { _, _ -> },
        {}
    )
}