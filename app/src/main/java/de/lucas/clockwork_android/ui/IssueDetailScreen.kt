package de.lucas.clockwork_android.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.lucas.clockwork_android.R
import de.lucas.clockwork_android.model.Issue
import de.lucas.clockwork_android.ui.BoardState.OPEN
import de.lucas.clockwork_android.ui.theme.Gray200

/**
 * Screen to show details of a clicked issue
 * Provides a button to navigate to EditIssueScreen, to edit this issue
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
internal fun IssueDetailScreen(issue: Issue, onClickEdit: () -> Unit) {
    Scaffold(backgroundColor = Color.White) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            shape = RoundedCornerShape(4.dp),
            backgroundColor = Gray200,
            border = BorderStroke(1.dp, Color.Black),
            elevation = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(text = "Issue #${issue.number}", fontSize = 14.sp)
                Text(
                    text = issue.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 32.dp)
                )
                Text(text = issue.description, modifier = Modifier.padding(top = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { onClickEdit() },
                        modifier = Modifier.padding(top = 32.dp)
                    ) {
                        Text(text = stringResource(id = R.string.edit))
                    }
                }

            }
        }
    }
}

@Preview
@Composable
private fun PreviewIssueDetailScreen() {
    IssueDetailScreen(
        issue = Issue(
            "2",
            "Bug Fixes",
            "Vinson",
            "Lot of Bugs. Should be Fixes asap!",
            OPEN
        )
    ) { }
}