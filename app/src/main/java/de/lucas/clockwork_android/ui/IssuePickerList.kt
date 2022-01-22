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
import androidx.compose.ui.platform.LocalContext
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

/**
 * List with Project and it's Issues to chose from to start a Toggle
 * @param projectList List of Projects and it's Issues
 * @param onStartToggle callBack to retrieve clicked Issue to then start Toggle
 */
@ExperimentalMaterialApi
@Composable
internal fun IssuePickerList(
    projectList: List<Project>,
    viewModel: IssuePickerListViewModel,
    onStartToggle: (Issue, Project) -> Unit,
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
                    items(projectList) { project ->
                        IssueItem(
                            issues = project.issues,
                            project_name = project.project_name,
                            onStartToggle = { issue ->
                                onStartToggle(issue, project)
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
                        modifier = Modifier.padding(bottom = 8.dp),
                        enabled = viewModel.getGroupID() != ""
                    ) {
                        Text(text = stringResource(id = R.string.create_project))
                    }
                }
            }
        }
    }
    // check state, if true -> show Dialog to create new project
    if (showNewProjectDialog) {
        CustomDialog(
            title_id = R.string.create_project_title,
            message_id = R.string.enter_group_name,
            button_text_id = R.string.create,
            onClickDismiss = { showNewProjectDialog = false }
        ) { input ->
            viewModel.createProject(input)
            showNewProjectDialog = false
        }
    }
}

/**
 * Expandable Item for a Project, that contains all it's Issues
 * @param issues Issues of the Project
 * @param project_name Name of the Project
 * @param onStartToggle callBack to retrieve clicked Issue to then start Toggle
 */
@ExperimentalMaterialApi
@Composable
private fun IssueItem(
    issues: List<Issue>,
    project_name: String,
    onStartToggle: (Issue) -> Unit
) {
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
    // If true -> show all Issues of this Project
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
                            text = "#${issue.number} ${issue.name}",
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
        projectList = listOf(
            Project(
                "testid",
                "Vinson",
                listOf(Issue("qwe", "Titel 1", "", "", BoardState.OPEN))
            )
        ),
        viewModel = IssuePickerListViewModel(LocalContext.current),
        onStartToggle = { _, _ -> }) {
    }
}