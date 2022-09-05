package de.lucas.clockwork_android.ui.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.lucas.clockwork_android.R

@Composable
internal fun ImprintScreen() {
    Scaffold { padding ->
        InfoText(text = stringResource(id = R.string.imprint_text), padding = padding)
    }
}

/**
 * Simple Text composable for Imprint and DataProtection
 */
@Composable
internal fun InfoText(text: String, padding: PaddingValues) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(padding)
    ) {
        Text(text = text, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 32.dp))
    }
}