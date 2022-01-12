package de.lucas.clockwork_android.ui

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.lucas.clockwork_android.R
import de.lucas.clockwork_android.model.Issue
import de.lucas.clockwork_android.ui.BoardState.OPEN
import de.lucas.clockwork_android.ui.theme.Gray200

@Composable
internal fun EditIssueScreen(
    issue: Issue?,
    @StringRes buttonText: Int,
    onClickBack: () -> Unit
) {
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
                if (issue != null) {
                    val title by remember { mutableStateOf(issue.title) }
                    val description by remember { mutableStateOf(issue.description) }
                    Text(text = "Issue #${issue.number}", fontSize = 14.sp)
                    Text(
                        text = issue.created_by,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    OutlinedStyledText(
                        id = R.string.title,
                        optText = title,
                        padding = 32,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        isSingleLine = true
                    )
                    OutlinedStyledText(
                        id = R.string.description,
                        optText = description,
                        padding = 16,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 8,
                        isSingleLine = false
                    )
                } else {
                    /* TODO get highest issue number +1 for creating new issue */
                    Text(text = "Issue #12 erstellen", fontSize = 14.sp)
                    OutlinedStyledText(
                        id = R.string.title,
                        optText = null,
                        padding = 32,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        isSingleLine = true
                    )
                    OutlinedStyledText(
                        id = R.string.description,
                        optText = null,
                        padding = 16,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 8,
                        isSingleLine = false
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            /*TODO Save */
                            onClickBack()
                        },
                        modifier = Modifier.padding(top = 32.dp)
                    ) {
                        Text(text = stringResource(id = buttonText))
                    }
                }
            }
        }
    }
    BackHandler {
        onClickBack()
    }
}

@Composable
fun OutlinedStyledText(
    @StringRes id: Int,
    optText: String?,
    padding: Int,
    modifier: Modifier,
    maxLines: Int,
    isSingleLine: Boolean
) {
    var text by remember { mutableStateOf(optText ?: "") }
    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(stringResource(id = id)) },
        modifier = modifier.padding(top = padding.dp),
        maxLines = maxLines,
        singleLine = isSingleLine
    )
}

@Preview
@Composable
private fun PreviewIssueDetailScreen() {
    EditIssueScreen(
        issue = Issue(
            2,
            "Bug Fixes",
            "Vinson",
            "Lot of Bugs. Should be Fixes asap!",
            "Vor 2 Tagen erstellt von Mattis Uphoff",
            OPEN
        ),
        buttonText = R.string.save
    ) { }
}