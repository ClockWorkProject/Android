package de.lucas.clockwork_android.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.lucas.clockwork_android.R
import de.lucas.clockwork_android.model.Issue
import de.lucas.clockwork_android.ui.theme.Gray200

@Composable
internal fun IssueDetailScreen(issue: Issue, onClickBack: () -> Unit) {
    var editState by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.issue_details)) },
                navigationIcon = {
                    IconButton(onClick = { onClickBack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back_light),
                            contentDescription = ""
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = contentColorFor(MaterialTheme.colors.primarySurface),
                elevation = 0.dp
            )
        },
        backgroundColor = Color.White
    ) {
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
                    text = issue.created_by,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = issue.title,
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
                        onClick = { editState = true },
                        modifier = Modifier.padding(top = 32.dp)
                    ) {
                        Text(text = stringResource(id = R.string.edit))
                    }
                }

            }
        }
    }
    if (editState) {
        EditIssueScreen(
            issue = issue,
            topBarTitle = R.string.edit_issue,
            buttonText = R.string.save
        ) {
            editState = false
        }
    }

    BackHandler {
        onClickBack()
    }
}

@Preview
@Composable
private fun PreviewIssueDetailScreen() {
    IssueDetailScreen(
        issue = Issue(
            2,
            "Bug Fixes",
            "Vinson",
            "Lot of Bugs. Should be Fixes asap!",
            "Vor 2 Tagen erstellt von Mattis Uphoff"
        )
    ) { }
}