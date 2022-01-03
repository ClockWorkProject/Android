package de.lucas.clockwork_android.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import de.lucas.clockwork_android.R
import de.lucas.clockwork_android.model.Issue
import de.lucas.clockwork_android.model.Project
import de.lucas.clockwork_android.ui.theme.Gray200

@ExperimentalMaterialApi
@Composable
internal fun IssuePickerList(
    issueList: List<Project>,
    onStartToggle: (Issue) -> Unit,
    onClose: () -> Unit
) {
    var showNewProjectDialog by remember { mutableStateOf(false) }
    Dialog(onDismissRequest = { onClose() }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            color = Gray200
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onClose() }, modifier = Modifier.padding(end = 8.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = ""
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.start_toggle),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    items(issueList) { project ->
                        IssueItem(
                            issues = project.issues,
                            project_name = project.project_name,
                            onStartToggle = { issue ->
                                onStartToggle(issue)
                                onClose()
                            }
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { showNewProjectDialog = true },
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(text = stringResource(id = R.string.create_project))
                    }
                }
            }
        }
    }
    if (showNewProjectDialog) {
        CustomDialog(
            title_id = R.string.create_project_title,
            message_id = R.string.enter_group_name,
            button_text_id = R.string.create,
            onClickDismiss = { showNewProjectDialog = false }
        ) { input ->
            /* TODO send to backend */
            showNewProjectDialog = false
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun IssueItem(issues: List<Issue>, project_name: String, onStartToggle: (Issue) -> Unit) {
    var expandableState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (expandableState) 180f else 0f)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 8.dp)
            .background(MaterialTheme.colors.primary)
            .clickable { expandableState = !expandableState }
    ) {
        Text(
            text = project_name,
            fontSize = 20.sp,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .weight(3f)
                .padding(start = 8.dp)
        )
        IconButton(modifier = Modifier
            .weight(1f)
            .rotate(rotationState),
            onClick = { expandableState = !expandableState }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_down_light),
                contentDescription = "",
                tint = Color.Unspecified
            )
        }
    }
    if (expandableState) {
        Column {
            issues.forEach { issue ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .height(48.dp),
                    shape = RoundedCornerShape(0.dp),
                    onClick = {
                        onStartToggle(issue)
                    }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "#${issue.number} ${issue.title}",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun PreviewIssuePicker() {
    IssuePickerList(
        issueList = listOf(
            Project(
                "Vinson",
                listOf(Issue(1, "Titel 1", "", "", "", BoardState.OPEN))
            )
        ), onStartToggle = {}) {
    }
}