package de.lucas.clockwork_android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import de.lucas.clockwork_android.R
import de.lucas.clockwork_android.model.Issue
import de.lucas.clockwork_android.model.Project
import de.lucas.clockwork_android.ui.BoardState.*
import de.lucas.clockwork_android.ui.theme.*

/**
 * Screen to show all issues, in its state lists, of selected project
 * @param projectList list of all projects of the group
 */
@ExperimentalPagerApi
@Composable
internal fun IssueBoardScreen(
    projectList: List<Project>,
    showBoardState: Boolean,
    projectId: Int,
    setShowBoardState: (Boolean) -> Unit,
    updateIssueState: (String, String, BoardState) -> Unit,
    onClickIssue: (Issue, String) -> Unit,
    onClickNewIssue: (Project, BoardState) -> Unit,
    changeProject: (Int) -> Unit
) {
    // variable to hold the id of the long pressed issue to update its state
    var longPressIssueId by remember { mutableStateOf("") }
    if (showBoardState) {
        // List of all states to click -> updates state of long pressed issue
        BoardStateList(
            onStateClicked = { boardState ->
                updateIssueState(
                    projectList[projectId].id,
                    longPressIssueId,
                    boardState
                )
            },
            setShowBoardState = { state ->
                setShowBoardState(state)
            },
            onClose = { setShowBoardState(false) }
        )
    }
    // State for the HorizontalPager to remember state across composition
    val pagerState = rememberPagerState()
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(padding)
        ) {
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
                    projectID = projectId,
                    onProjectChange = { id -> changeProject(id) }
                )
            }
            /**
             * ViewPager to swipe between lists of states (e.b. "open", "doing" etc.)
             * Check if projectID is -1 (in "project =" and "onClickNewIssue")
             * -> User has no group or group has no projects
             * -> Sets empty projects to prevent index error and disables "create issue"-button
             */
            BoardViewPager(
                pagerState = pagerState,
                project = if (projectId == -1) {
                    Project("", "", listOf())
                } else {
                    projectList[projectId]
                },
                setShowBoardState = { state ->
                    setShowBoardState(state)
                },
                onClickIssue = { issue ->
                    onClickIssue(issue, projectList[projectId].id)
                },
                onClickNewIssue = { boardState ->
                    if (projectId != -1) {
                        onClickNewIssue(projectList[projectId], boardState)
                    }
                },
                onLongPressIssue = { issue -> longPressIssueId = issue.id }
            )
        }
    }
}

/**
 * The whole swipable Pager
 * @param pagerState HorizonalPager needs a pagerState to remember its state across composition
 * @param project takes the currently selected project
 * @param onClickIssue callBack with clicked Issue to populate EditIssueScreen
 * @param onClickNewIssue callBack to navigate to empty EditIssueScreen to create new Issue
 */
@ExperimentalPagerApi
@Composable
internal fun BoardViewPager(
    pagerState: PagerState,
    project: Project,
    setShowBoardState: (Boolean) -> Unit,
    onClickIssue: (Issue) -> Unit,
    onClickNewIssue: (BoardState) -> Unit,
    onLongPressIssue: (Issue) -> Unit
) {
    // Array of BoardState enums
    val items = values()
    HorizontalPager(state = pagerState, count = items.size) { page ->
        // Checks the BoardState and creates IssueBoardItems according to state
        when (items[page]) {
            OPEN -> {
                // Filters issues from current project according to state and returns as list
                val issues = project.issues.filter { issue -> issue.issueState == OPEN }
                IssueBoardItem(
                    boardTitle = R.string.open,
                    issueList = issues,
                    boardColor = Color.Black,
                    currentPageIndex = page,
                    issueSize = issues.size,
                    setShowBoardState = { state ->
                        setShowBoardState(state)
                    },
                    onClickIssue = onClickIssue,
                    onClickNewIssue = {
                        onClickNewIssue(OPEN)
                    },
                    onLongPressIssue = onLongPressIssue
                )
            }
            TODO -> {
                val issues = project.issues.filter { issue -> issue.issueState == TODO }
                IssueBoardItem(
                    boardTitle = R.string.todo,
                    issueList = issues,
                    boardColor = Green500,
                    currentPageIndex = page,
                    issueSize = issues.size,
                    setShowBoardState = { state ->
                        setShowBoardState(state)
                    },
                    onClickIssue = onClickIssue,
                    onClickNewIssue = {
                        onClickNewIssue(TODO)
                    },
                    onLongPressIssue = onLongPressIssue
                )
            }
            DOING -> {
                val issues = project.issues.filter { issue -> issue.issueState == DOING }
                IssueBoardItem(
                    boardTitle = R.string.doing,
                    issueList = issues,
                    boardColor = Blue500,
                    currentPageIndex = page,
                    issueSize = issues.size,
                    setShowBoardState = { state ->
                        setShowBoardState(state)
                    },
                    onClickIssue = onClickIssue,
                    onClickNewIssue = {
                        onClickNewIssue(DOING)
                    },
                    onLongPressIssue = onLongPressIssue
                )
            }
            REVIEW -> {
                val issues = project.issues.filter { issue -> issue.issueState == REVIEW }
                IssueBoardItem(
                    boardTitle = R.string.review,
                    issueList = issues,
                    boardColor = Yellow500,
                    currentPageIndex = page,
                    issueSize = issues.size,
                    setShowBoardState = { state ->
                        setShowBoardState(state)
                    },
                    onClickIssue = onClickIssue,
                    onClickNewIssue = {
                        onClickNewIssue(REVIEW)
                    },
                    onLongPressIssue = onLongPressIssue
                )
            }
            BLOCKER -> {
                val issues = project.issues.filter { issue -> issue.issueState == BLOCKER }
                IssueBoardItem(
                    boardTitle = R.string.blocker,
                    issueList = issues,
                    boardColor = Red500,
                    currentPageIndex = page,
                    issueSize = issues.size,
                    setShowBoardState = { state ->
                        setShowBoardState(state)
                    },
                    onClickIssue = onClickIssue,
                    onClickNewIssue = {
                        onClickNewIssue(BLOCKER)
                    },
                    onLongPressIssue = onLongPressIssue
                )
            }
            CLOSED -> {
                val issues = project.issues.filter { issue -> issue.issueState == CLOSED }
                IssueBoardItem(
                    boardTitle = R.string.closed,
                    issueList = issues,
                    boardColor = Color.Black,
                    currentPageIndex = page,
                    issueSize = issues.size,
                    setShowBoardState = { state ->
                        setShowBoardState(state)
                    },
                    onClickIssue = onClickIssue,
                    onClickNewIssue = {
                        onClickNewIssue(CLOSED)
                    },
                    onLongPressIssue = onLongPressIssue
                )
            }
        }
    }
}

/**
 * Dialog with a list of all states to "move" selected issue
 */
@Composable
fun BoardStateList(
    onStateClicked: (BoardState) -> Unit,
    setShowBoardState: (Boolean) -> Unit,
    onClose: () -> Unit
) {
    Dialog(onDismissRequest = { onClose() }) {
        Surface(
            modifier = Modifier.padding(16.dp),
            color = Gray200
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onClose() }, modifier = Modifier.padding(end = 8.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = ""
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.change_board_state),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                LazyColumn(
                    modifier = Modifier.padding(8.dp)
                ) {
                    items(values()) { boardState ->
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .height(48.dp)
                            .clickable {
                                onStateClicked(boardState)
                                setShowBoardState(false)
                            }) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = boardState.name,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class BoardState {
    OPEN,
    TODO,
    DOING,
    REVIEW,
    BLOCKER,
    CLOSED
}

@ExperimentalPagerApi
@Preview
@Composable
private fun PreviewIssueBoard() {
    IssueBoardScreen(
        projectList = listOf(),
        showBoardState = false,
        projectId = -1,
        setShowBoardState = { },
        updateIssueState = { _, _, _ -> },
        onClickIssue = { _, _ -> },
        onClickNewIssue = { _, _ -> },
        changeProject = {}
    )
}