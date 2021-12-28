package de.lucas.clockwork_android.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.lucas.clockwork_android.R
import de.lucas.clockwork_android.model.Issue
import de.lucas.clockwork_android.model.Project
import de.lucas.clockwork_android.ui.BoardState.OPEN
import de.lucas.clockwork_android.ui.BoardState.TODO
import de.lucas.clockwork_android.ui.theme.Gray200
import de.lucas.clockwork_android.ui.theme.Gray500
import de.lucas.clockwork_android.ui.theme.Gray700

@Composable
internal fun IssueBoardItem(
    @StringRes boardTitle: Int,
    issueList: List<Issue>,
    boardColor: Color,
    currentPageIndex: Int,
    onClickIssue: (Issue) -> Unit,
    onClickNewIssue: () -> Unit
) {
    val swipeLeftVisible = if (currentPageIndex == 0) 0f else 1f
    val swipeRightVisible = if (currentPageIndex == 5) 0f else 1f
    // For testing purpose
    val projectList = listOf(
        Project("IT-Projekt", listOf()),
        Project(
            "Vinson",
            listOf(
                Issue(2, "", "", "", "", OPEN),
                Issue(2, "", "", "", "", TODO),
                Issue(2, "", "", "", "", TODO)
            )
        ),
        Project("Noch eins", listOf())
    )
    var currentProject = remember {
        mutableStateOf(
            Project(
                "Vinson",
                listOf(
                    Issue(2, "", "", "", "", OPEN),
                    Issue(2, "", "", "", "", TODO),
                    Issue(2, "", "", "", "", TODO)
                )
            )
        )
    }

    Scaffold(backgroundColor = Color.White) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.project),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                CustomDropDownMenu(
                    projects = projectList,
                    currentProject = currentProject.value,
                    onClickProject = { currentProject.value = it }
                )
            }
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                shape = RoundedCornerShape(4.dp),
                backgroundColor = Gray200,
                border = BorderStroke(1.dp, boardColor),
                elevation = 0.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            stringResource(id = boardTitle),
                            fontSize = 24.sp,
                            modifier = Modifier.weight(2f),
                            color = boardColor
                        )
                        Text(
                            text = currentProject.value.issues.size.toString(),
                            fontSize = 24.sp,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cards),
                            contentDescription = "",
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        IconButton(
                            onClick = { onClickNewIssue() },
                            modifier = Modifier
                                .then(Modifier.size(40.dp))
                                .background(Gray200)
                                .border(border = BorderStroke(width = 1.dp, Gray500)),
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add_gray),
                                contentDescription = "",
                                tint = Gray500
                            )
                        }
                    }
                    Column(modifier = Modifier.fillMaxHeight()) {
                        LazyColumn(
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(1f)
                        ) {
                            items(issueList) { issue ->
                                IssueItem(issue = issue, onClickIssue = { onClickIssue(issue) })
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, start = 4.dp, end = 4.dp, bottom = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_swipe_left),
                                contentDescription = "",
                                modifier = Modifier
                                    .alpha(swipeLeftVisible),
                                tint = Gray700
                            )

                            Icon(
                                painter = painterResource(id = R.drawable.ic_swipe_right),
                                contentDescription = "",
                                modifier = Modifier.alpha(swipeRightVisible),
                                tint = Gray700
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomDropDownMenu(
    projects: List<Project>,
    currentProject: Project,
    onClickProject: (Project) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(projects.indexOf(currentProject)) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(border = BorderStroke(width = 1.dp, Color.Black))
            .clickable(onClick = { expanded = true })
    ) {
        Text(
            projects[selectedIndex].project_name,
            modifier = Modifier
                .background(Color.White)
                .padding(start = 8.dp)
                .weight(4f),
            fontSize = 20.sp
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_down_dark),
            contentDescription = "",
            modifier = Modifier.weight(1f)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
        ) {
            projects.forEachIndexed { index, project ->
                DropdownMenuItem(onClick = {
                    onClickProject(project)
                    selectedIndex = index
                    expanded = false
                }) {
                    Text(text = project.project_name)
                }
            }
        }
    }
}

@Composable
private fun IssueItem(issue: Issue, onClickIssue: (Issue) -> Unit) {
    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .clickable { onClickIssue(issue) },
        backgroundColor = Color.White,
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Column {
            Text(
                text = issue.title,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                fontSize = 18.sp
            )
            Text(
                text = "#${issue.number}",
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                fontSize = 18.sp
            )
        }
    }
}

@Preview
@Composable
private fun PreviewIssueBoard() {
    IssueBoardItem(
        boardTitle = R.string.open,
        issueList = listOf(),
        boardColor = Color.Black,
        currentPageIndex = 1,
        onClickIssue = { }
    ) { }
}

@Preview
@Composable
private fun PreviewDropDown() {
    CustomDropDownMenu(
        projects = listOf(Project("Projekt", listOf())),
        currentProject = Project("Projekt", listOf())
    ) { }
}