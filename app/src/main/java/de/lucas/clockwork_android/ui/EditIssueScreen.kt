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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.lucas.clockwork_android.R
import de.lucas.clockwork_android.model.Issue
import de.lucas.clockwork_android.model.Project
import de.lucas.clockwork_android.ui.BoardState.OPEN
import de.lucas.clockwork_android.ui.theme.Gray200

@Composable
internal fun EditIssueScreen(
    issue: Issue?,
    project: Project?,
    projectID: String,
    @StringRes buttonText: Int,
    viewModel: EditIssueViewModel,
    state: BoardState,
    onClickBack: () -> Unit
) {
    var title by remember { mutableStateOf(issue?.name ?: "") }
    var description by remember { mutableStateOf(issue?.description ?: "") }
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
                    Text(text = "Issue #${issue.number}", fontSize = 14.sp)
                    OutlinedStyledText(
                        id = R.string.title,
                        text = title,
                        padding = 32,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        isSingleLine = true,
                        isTitle = true,
                        isError = viewModel.getIsError()
                    ) { title = it }
                    OutlinedStyledText(
                        id = R.string.description,
                        text = description,
                        padding = 16,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 8,
                        isSingleLine = false,
                        isTitle = false,
                        isError = viewModel.getIsError()
                    ) { description = it }
                } else if (project != null) {
                    Text(text = "Issue #${project.issues.size + 1} erstellen", fontSize = 14.sp)
                    OutlinedStyledText(
                        id = R.string.title,
                        text = title,
                        padding = 32,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        isSingleLine = true,
                        isTitle = true,
                        isError = viewModel.getIsError()
                    ) { title = it }
                    OutlinedStyledText(
                        id = R.string.description,
                        text = description,
                        padding = 16,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 8,
                        isSingleLine = false,
                        isTitle = false,
                        isError = viewModel.getIsError()
                    ) { description = it }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            if (issue != null) {
                                viewModel.updateIssue(
                                    projectID,
                                    issue.id,
                                    title,
                                    description
                                )
                            }
                            if (project != null) {
                                viewModel.createIssue(
                                    project.id,
                                    "${project.issues.size + 1}",
                                    title,
                                    description,
                                    state
                                )
                            }
                            if (!viewModel.getIsError()) onClickBack()
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
    text: String,
    padding: Int,
    modifier: Modifier,
    maxLines: Int,
    isSingleLine: Boolean,
    isTitle: Boolean,
    isError: Boolean,
    setOnChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            value = text,
            onValueChange = { setOnChange(it) },
            label = { Text(stringResource(id = id)) },
            modifier = modifier.padding(top = padding.dp),
            maxLines = maxLines,
            singleLine = isSingleLine,
            trailingIcon = {
                if (isTitle && isError) {
                    Icon(
                        painterResource(id = R.drawable.ic_error),
                        "",
                        tint = MaterialTheme.colors.error
                    )
                }
            },
        )

        if (isTitle && isError) {
            Text(
                text = stringResource(id = R.string.error_message_text_empty),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewIssueDetailScreen() {
    EditIssueScreen(
        issue = Issue(
            "wqe",
            "Bug Fixes",
            "Vinson",
            "Lot of Bugs. Should be Fixes asap!",
            OPEN
        ),
        Project("", "", listOf()),
        projectID = "",
        viewModel = EditIssueViewModel(LocalContext.current),
        buttonText = R.string.save,
        state = OPEN
    ) { }
}