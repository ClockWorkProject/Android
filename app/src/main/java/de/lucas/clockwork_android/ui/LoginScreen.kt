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
import androidx.compose.ui.draw.scale
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
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AppLogo()
            OutlinedStyledText(id = R.string.email, padding = 40)
            OutlinedStyledText(id = R.string.password, padding = 24)
            RoundedButton(id = R.string.login, padding = 40, onClickLogin)
            Text(
                text = stringResource(id = R.string.no_account),
                modifier = Modifier.padding(top = 32.dp)
            )
            RoundedButton(id = R.string.sign_up, padding = 4, onClickSignUp)
        }

    }
}

@Composable
fun AppLogo() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .background(MaterialTheme.colors.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "",
                Modifier.scale(1.2f)
            )
            Text(
                text = stringResource(id = R.string.app_name),
                Modifier.padding(top = 16.dp),
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun OutlinedStyledText(@StringRes id: Int, padding: Int) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(stringResource(id = id)) },
        modifier = Modifier.padding(top = padding.dp)
    )
}

@Composable
fun RoundedButton(@StringRes id: Int, padding: Int, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(30.dp),
        modifier = Modifier
            .padding(top = padding.dp)
            .scale(1.2f)
            .fillMaxWidth(0.4f)
    ) {
        Text(text = stringResource(id = id))
    }
}

@Preview
@Composable
fun LoginPreview() {
    LoginScreen({}, {})
}