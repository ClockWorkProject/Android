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
import androidx.compose.ui.platform.LocalContext
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
import de.lucas.clockwork_android.ui.BoardState.values
import de.lucas.clockwork_android.ui.theme.Gray200

@ExperimentalPagerApi
@Composable
internal fun IssueBoardScreen(
    viewModel: IssueBoardViewModel,
    onClickIssue: (Issue) -> Unit,
    onClickNewIssue: () -> Unit
) {
    var longPressIssueId by remember { mutableStateOf(-1) }
    if (viewModel.getShowBoardState()) {
        BoardStateList(
            viewModel = viewModel,
            onStateClicked = { boardState -> /* TODO change BoardState for Issue with provided number/id, Refresh IssueBoard?  */ },
            onClose = { viewModel.setShowBoardState(false) }
        )
    }
    // State for the HorizontalPager to remember state across composition
    val pagerState = rememberPagerState()
    Scaffold {
        Column(modifier = Modifier.background(Color.White)) {
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
                    projects = listOf(),
                    projectID = viewModel.getProjectId(),
                    onProjectChange = { id -> viewModel.changeProject(id) }
                )
            }
            BoardViewPager(
                pagerState = pagerState,
                project = Project("sad", "asdf", listOf()),
                viewModel = viewModel,
                onClickIssue = onClickIssue,
                onClickNewIssue = onClickNewIssue,
//                onLongPressIssue = { issue -> longPressIssueId = issue.number }
                onLongPressIssue = {}
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
    viewModel: IssueBoardViewModel,
    onClickIssue: (Issue) -> Unit,
    onClickNewIssue: () -> Unit,
    onLongPressIssue: (Issue) -> Unit
) {
    // Array of BoardState enums
    val items = values()
    HorizontalPager(state = pagerState, count = items.size) { page ->
        // Checks the BoardState and creates IssueBoardItems according to state
//        when (items[page]) {
//            OPEN -> {
//                // Filters issues from current project according to state and returns as list
//                val issues = project.issues.filter { issue -> issue.board_state == OPEN }
//                IssueBoardItem(
//                    boardTitle = R.string.open,
//                    issueList = issues,
//                    boardColor = Color.Black,
//                    currentPageIndex = page,
//                    issueSize = issues.size,
//                    viewModel = viewModel,
//                    onClickIssue = onClickIssue,
//                    onClickNewIssue = onClickNewIssue,
//                    onLongPressIssue = onLongPressIssue
//                )
//            }
//            TODO -> {
//                val issues = project.issues.filter { issue -> issue.board_state == TODO }
//                IssueBoardItem(
//                    boardTitle = R.string.todo,
//                    issueList = issues,
//                    boardColor = Green500,
//                    currentPageIndex = page,
//                    issueSize = issues.size,
//                    viewModel = viewModel,
//                    onClickIssue = onClickIssue,
//                    onClickNewIssue = onClickNewIssue,
//                    onLongPressIssue = onLongPressIssue
//                )
//            }
//            DOING -> {
//                val issues = project.issues.filter { issue -> issue.board_state == DOING }
//                IssueBoardItem(
//                    boardTitle = R.string.doing,
//                    issueList = issues,
//                    boardColor = Blue500,
//                    currentPageIndex = page,
//                    issueSize = issues.size,
//                    viewModel = viewModel,
//                    onClickIssue = onClickIssue,
//                    onClickNewIssue = onClickNewIssue,
//                    onLongPressIssue = onLongPressIssue
//                )
//            }
//            REVIEW -> {
//                val issues = project.issues.filter { issue -> issue.board_state == REVIEW }
//                IssueBoardItem(
//                    boardTitle = R.string.review,
//                    issueList = issues,
//                    boardColor = Yellow500,
//                    currentPageIndex = page,
//                    issueSize = issues.size,
//                    viewModel = viewModel,
//                    onClickIssue = onClickIssue,
//                    onClickNewIssue = onClickNewIssue,
//                    onLongPressIssue = onLongPressIssue
//                )
//            }
//            BLOCKER -> {
//                val issues = project.issues.filter { issue -> issue.board_state == BLOCKER }
//                IssueBoardItem(
//                    boardTitle = R.string.blocker,
//                    issueList = issues,
//                    boardColor = Red500,
//                    currentPageIndex = page,
//                    issueSize = issues.size,
//                    viewModel = viewModel,
//                    onClickIssue = onClickIssue,
//                    onClickNewIssue = onClickNewIssue,
//                    onLongPressIssue = onLongPressIssue
//                )
//            }
//            CLOSED -> {
//                val issues = project.issues.filter { issue -> issue.board_state == CLOSED }
//                IssueBoardItem(
//                    boardTitle = R.string.closed,
//                    issueList = issues,
//                    boardColor = Color.Black,
//                    currentPageIndex = page,
//                    issueSize = issues.size,
//                    viewModel = viewModel,
//                    onClickIssue = onClickIssue,
//                    onClickNewIssue = onClickNewIssue,
//                    onLongPressIssue = onLongPressIssue
//                )
//            }
//        }
    }
}

@Composable
fun BoardStateList(
    viewModel: IssueBoardViewModel,
    onStateClicked: (BoardState) -> Unit,
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
                    items(BoardState.values()) { boardState ->
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .height(48.dp)
                            .clickable {
                                onStateClicked(boardState)
                                viewModel.setShowBoardState(false)
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
        viewModel = IssueBoardViewModel(LocalContext.current),
        onClickIssue = {},
        onClickNewIssue = {}
    )
}