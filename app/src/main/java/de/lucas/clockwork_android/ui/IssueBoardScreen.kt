package de.lucas.clockwork_android.ui

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import de.lucas.clockwork_android.R
import de.lucas.clockwork_android.model.Issue
import de.lucas.clockwork_android.ui.BoardState.*
import de.lucas.clockwork_android.ui.theme.Blue500
import de.lucas.clockwork_android.ui.theme.Green500
import de.lucas.clockwork_android.ui.theme.Red500
import de.lucas.clockwork_android.ui.theme.Yellow500

// For testing purpose
val issueList = listOf(
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

@ExperimentalPagerApi
@Composable
internal fun IssueBoardScreen(
    issueList: List<Issue>,
    onClickIssue: (Issue) -> Unit,
    onClickNewIssue: () -> Unit
) {
    val pagerState = rememberPagerState()
    LaunchedEffect(pagerState) {
        // Collect from the a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }
    }
    Scaffold {
        BoardViewPager(pagerState = pagerState, issueList, onClickIssue, onClickNewIssue)
    }
}

@ExperimentalPagerApi
@Composable
internal fun BoardViewPager(
    pagerState: PagerState,
    issueList: List<Issue>,
    onClickIssue: (Issue) -> Unit,
    onClickNewIssue: () -> Unit
) {
    val items = values()
    HorizontalPager(state = pagerState, count = items.size) { page ->
        when (items[page]) {
            OPEN -> {
                val issues = issueList.filter { issue -> issue.board_state == OPEN }
                IssueBoardItem(
                    boardTitle = R.string.open,
                    issueList = issues,
                    boardColor = Color.Black,
                    currentPageIndex = page,
                    onClickIssue = onClickIssue,
                    onClickNewIssue = onClickNewIssue
                )
            }
            TODO -> {
                val issues = issueList.filter { issue -> issue.board_state == TODO }
                IssueBoardItem(
                    boardTitle = R.string.todo,
                    issueList = issues,
                    boardColor = Green500,
                    currentPageIndex = page,
                    onClickIssue = onClickIssue,
                    onClickNewIssue = onClickNewIssue
                )
            }
            DOING -> {
                val issues = issueList.filter { issue -> issue.board_state == DOING }
                IssueBoardItem(
                    boardTitle = R.string.doing,
                    issueList = issues,
                    boardColor = Blue500,
                    currentPageIndex = page,
                    onClickIssue = onClickIssue,
                    onClickNewIssue = onClickNewIssue
                )
            }
            REVIEW -> {
                val issues = issueList.filter { issue -> issue.board_state == REVIEW }
                IssueBoardItem(
                    boardTitle = R.string.review,
                    issueList = issues,
                    boardColor = Yellow500,
                    currentPageIndex = page,
                    onClickIssue = onClickIssue,
                    onClickNewIssue = onClickNewIssue
                )
            }
            BLOCKER -> {
                val issues = issueList.filter { issue -> issue.board_state == BLOCKER }
                IssueBoardItem(
                    boardTitle = R.string.blocker,
                    issueList = issues,
                    boardColor = Red500,
                    currentPageIndex = page,
                    onClickIssue = onClickIssue,
                    onClickNewIssue = onClickNewIssue
                )
            }
            CLOSED -> {
                val issues = issueList.filter { issue -> issue.board_state == CLOSED }
                IssueBoardItem(
                    boardTitle = R.string.closed,
                    issueList = issues,
                    boardColor = Color.Black,
                    currentPageIndex = page,
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