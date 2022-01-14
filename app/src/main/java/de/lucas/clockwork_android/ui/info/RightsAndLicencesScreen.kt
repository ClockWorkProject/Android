package de.lucas.clockwork_android.ui.info

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer

@Composable
internal fun RightsAndLicencesScreen() {
    Scaffold { innerPadding ->
        // Displays all used licences in a list
        LibrariesContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RightsAndLicencesPreview() {
    RightsAndLicencesScreen()
}