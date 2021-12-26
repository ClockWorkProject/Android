package de.lucas.clockwork_android.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import de.lucas.clockwork_android.model.Issue

@Composable
internal fun IssueBoardScreen(onClickIssue: () -> Unit) {
    val viewModel = IssueViewModel()
    viewModel.setIssue(
        Issue(
            2,
            "Bug Fixes",
            "Vinson",
            "Lot of Bugs. Should be fixed asap!",
            "Vor 2 Tagen erstellt von Mattis Uphoff"
        )
    )

    Scaffold {
        Column(verticalArrangement = Arrangement.Center) {
            Button(onClick = { onClickIssue() }) {
                Text("To Details")
            }
        }
    }
}