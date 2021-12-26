package de.lucas.clockwork_android.ui.info

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import de.lucas.clockwork_android.R

@Composable
internal fun DataProtectionScreen() {
    Scaffold {
        InfoText(text = stringResource(id = R.string.data_protection_text))
    }
}