package de.lucas.clockwork_android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
                "IT-Projekt",
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
                "IT-Projekt",
                "Beschreibungen......viiiieeelllll",
                "Vor 2 Tagen erstellt von Meatüs",
                OPEN
            ),
            Issue(
                4, "Redesign", "Vinson", "rwwersdSAddSA", "",
                TODO
            ),
            Issue(
                1, "UI", "Vinson", "rwweradsgfrgervasdSAddSA", "",
                CLOSED
            ),
            Issue(
                3, "Documentation", "Vinson", "jfkrzuioolhgjfc", "",
                CLOSED
            ),
            Issue(7, "API connection", "Vinson", "TQRHRZTWSER", "", BLOCKER),
        )
    ),
    Project(
        "Noch eins", listOf(
            Issue(
                1, "UI", "Noch eins", "rwweradsgfrgervasdSAddSA", "",
                CLOSED
            )
        )
    ), Project(
        "IT-Projekt", listOf(
            Issue(
                2,
                "Bug Fixes",
                "IT-Projekt",
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
                "IT-Projekt",
                "Beschreibungen......viiiieeelllll",
                "Vor 2 Tagen erstellt von Meatüs",
                OPEN
            ),
            Issue(
                4, "Redesign", "Vinson", "rwwersdSAddSA", "",
                TODO
            ),
            Issue(
                1, "UI", "Vinson", "rwweradsgfrgervasdSAddSA", "",
                CLOSED
            ),
            Issue(
                3, "Documentation", "Vinson", "jfkrzuioolhgjfc", "",
                CLOSED
            ),
            Issue(7, "API connection", "Vinson", "TQRHRZTWSER", "", BLOCKER),
        )
    ),
    Project(
        "Noch eins", listOf(
            Issue(
                1, "UI", "Noch eins", "rwweradsgfrgervasdSAddSA", "",
                CLOSED
            )
        )
    ),
    Project(
        "IT-Projekt", listOf(
            Issue(
                2,
                "Bug Fixes",
                "IT-Projekt",
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
                "IT-Projekt",
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
                "Vor 2 Tagen erstellt von Mattis Uphoff",
                OPEN
            ),
            Issue(
                4, "Redesign", "Vinson", "rwwersdSAddSA", "",
                TODO
            ),
            Issue(
                1, "UI", "Vinson", "rwweradsgfrgervasdSAddSA", "",
                CLOSED
            ),
            Issue(
                3, "Documentation", "Vinson", "jfkrzuioolhgjfc", "",
                CLOSED
            ),
            Issue(7, "API connection", "Vinson", "TQRHRZTWSER", "", BLOCKER),
        )
    ),
    Project(
        "Noch eins", listOf(
            Issue(
                1, "UI", "Noch eins", "rwweradsgfrgervasdSAddSA", "",
                CLOSED
            )
        )
    )
)

@ExperimentalPagerApi
@Composable
internal fun IssueBoardScreen(
    viewModel: IssueBoardViewModel,
    onClickIssue: (Issue) -> Unit,
    onClickNewIssue: () -> Unit
) {
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
                    projects = projectList,
                    projectID = viewModel.getProjectId(),
                    onProjectChange = { id -> viewModel.changeProject(id) }
                )
            }
            BoardViewPager(
                pagerState = pagerState,
                project = projectList[viewModel.getProjectId()],
                onClickIssue = onClickIssue,
                onClickNewIssue = onClickNewIssue
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
    onClickIssue: (Issue) -> Unit,
    onClickNewIssue: () -> Unit
) {
    // Array of BoardState enums
    val items = values()
    HorizontalPager(state = pagerState, count = items.size) { page ->
        // Checks the BoardState and creates IssueBoardItems according to state
        when (items[page]) {
            OPEN -> {
                // Filters issues from current project according to state and returns as list
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

@ExperimentalPagerApi
@Preview
@Composable
private fun PreviewIssueBoard() {
    IssueBoardScreen(
        viewModel = IssueBoardViewModel(LocalContext.current),
        onClickIssue = {}
    ) { }
}