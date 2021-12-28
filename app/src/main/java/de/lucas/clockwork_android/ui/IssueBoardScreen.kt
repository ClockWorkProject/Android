package de.lucas.clockwork_android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import de.lucas.clockwork_android.R
import de.lucas.clockwork_android.model.Issue
import de.lucas.clockwork_android.model.Project
import de.lucas.clockwork_android.ui.BoardState.*
import de.lucas.clockwork_android.ui.theme.Blue500
import de.lucas.clockwork_android.ui.theme.Green500
import de.lucas.clockwork_android.ui.theme.Red500
import de.lucas.clockwork_android.ui.theme.Yellow500

// For testing purpose
val projectList = listOf(
    Project(
        "IT-Projekt", listOf(
            Issue(
                2,
                "Bug Fixes",
                "",
                "Beschreibungen......viiiieeelllll",
                "Vor 2 Tagen erstellt von Meatüs",
                BLOCKER
            ),
            Issue(
                4, "Redesign", "", "rwwersdSAddSA", "",
                BLOCKER
            ),
        )
    ),
    Project(
        "Vinson",
        listOf(
            Issue(
                2,
                "Bug Fixes",
                "",
                "Beschreibungen......viiiieeelllll",
                "Vor 2 Tagen erstellt von Meatüs",
                OPEN
            ),
            Issue(
                4, "Redesign", "", "rwwersdSAddSA", "",
                TODO
            ),
            Issue(
                1, "UI", "", "rwweradsgfrgervasdSAddSA", "",
                CLOSED
            ),
            Issue(
                3, "Documentation", "", "jfkrzuioolhgjfc", "",
                CLOSED
            ),
            Issue(7, "API connection", "", "TQRHRZTWSER", "", BLOCKER)
        )
    ),
    Project("Noch eins", listOf())
)

@ExperimentalPagerApi
@Composable
internal fun IssueBoardScreen(
    projectID: Int,
    onClickIssue: (Issue) -> Unit,
    onClickNewIssue: () -> Unit
) {
    val viewModel = IssueBoardViewModel()
    val pagerState = rememberPagerState()
    var currentProjectID by remember { mutableStateOf(viewModel.projectID.value) }
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
                    projects = projectList,
                    projectID = projectID,
                    onProjectChange = { id -> currentProjectID = id }
                )
            }
            BoardViewPager(
                pagerState = pagerState,
                project = projectList[currentProjectID!!],
                onClickIssue = onClickIssue,
                onClickNewIssue = onClickNewIssue
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
internal fun BoardViewPager(
    pagerState: PagerState,
    project: Project,
    onClickIssue: (Issue) -> Unit,
    onClickNewIssue: () -> Unit
) {
    val items = values()
    HorizontalPager(state = pagerState, count = items.size) { page ->
        when (items[page]) {
            OPEN -> {
                val issues = project.issues.filter { issue -> issue.board_state == OPEN }
                IssueBoardItem(
                    boardTitle = R.string.open,
                    issueList = issues,
                    boardColor = Color.Black,
                    currentPageIndex = page,
                    issueSize = issues.size,
                    onClickIssue = onClickIssue,
                    onClickNewIssue = onClickNewIssue
                )
            }
            TODO -> {
                val issues = project.issues.filter { issue -> issue.board_state == TODO }
                IssueBoardItem(
                    boardTitle = R.string.todo,
                    issueList = issues,
                    boardColor = Green500,
                    currentPageIndex = page,
                    issueSize = issues.size,
                    onClickIssue = onClickIssue,
                    onClickNewIssue = onClickNewIssue
                )
            }
            DOING -> {
                val issues = project.issues.filter { issue -> issue.board_state == DOING }
                IssueBoardItem(
                    boardTitle = R.string.doing,
                    issueList = issues,
                    boardColor = Blue500,
                    currentPageIndex = page,
                    issueSize = issues.size,
                    onClickIssue = onClickIssue,
                    onClickNewIssue = onClickNewIssue
                )
            }
            REVIEW -> {
                val issues = project.issues.filter { issue -> issue.board_state == REVIEW }
                IssueBoardItem(
                    boardTitle = R.string.review,
                    issueList = issues,
                    boardColor = Yellow500,
                    currentPageIndex = page,
                    issueSize = issues.size,
                    onClickIssue = onClickIssue,
                    onClickNewIssue = onClickNewIssue
                )
            }
            BLOCKER -> {
                val issues = project.issues.filter { issue -> issue.board_state == BLOCKER }
                IssueBoardItem(
                    boardTitle = R.string.blocker,
                    issueList = issues,
                    boardColor = Red500,
                    currentPageIndex = page,
                    issueSize = issues.size,
                    onClickIssue = onClickIssue,
                    onClickNewIssue = onClickNewIssue
                )
            }
            CLOSED -> {
                val issues = project.issues.filter { issue -> issue.board_state == CLOSED }
                IssueBoardItem(
                    boardTitle = R.string.closed,
                    issueList = issues,
                    boardColor = Color.Black,
                    currentPageIndex = page,
                    issueSize = issues.size,
                    onClickIssue = onClickIssue,
                    onClickNewIssue = onClickNewIssue
                )
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