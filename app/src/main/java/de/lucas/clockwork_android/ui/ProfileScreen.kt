package de.lucas.clockwork_android.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
internal fun ProfileScreen() {
    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    }
}

@Composable
internal fun ProfileDialog() {

}

@Preview
@Composable
private fun PreviewProfileScreen() {
    ProfileScreen()
}

@Preview
@Composable
private fun PreviewProfileDialog() {
    ProfileDialog()
}