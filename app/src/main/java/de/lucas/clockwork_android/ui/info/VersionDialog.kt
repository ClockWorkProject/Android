package de.lucas.clockwork_android.ui.info

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import de.lucas.clockwork_android.R

@Composable
internal fun VersionDialog(
    title: String,
    version: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        },
        text = {
            Text(
                text = stringResource(R.string.version, version),
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.close))
            }
        }
    )
}

@Preview
@Composable
private fun PreviewVersionDialog() {
    VersionDialog(title = "Title", version = "1.0.0") { }
}
