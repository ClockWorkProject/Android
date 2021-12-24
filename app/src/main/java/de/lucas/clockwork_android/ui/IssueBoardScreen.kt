package de.lucas.clockwork_android.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import de.lucas.clockwork_android.R
import de.lucas.clockwork_android.model.Issue

@Composable
internal fun IssueBoardScreen() {
    var state by remember { mutableStateOf(false) }
    Scaffold(topBar = { TopAppBar(title = R.string.issue_board) }) {
        Column(verticalArrangement = Arrangement.Center) {
            Button(onClick = { state = true }) {
                Text("To Details")
            }
        }
    }
    if (state) IssueDetailScreen(
        Issue(
            2,
            "Bug Fixes",
            "Vinson",
            "Lot of Bugs. Should be fixed asap!",
            "Vor 2 Tagen erstellt von Mattis Uphoff"
        )
    ) { state = false }
}