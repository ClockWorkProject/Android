package de.lucas.clockwork_android.ui

import androidx.annotation.StringRes
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
        "Vor 2 Tagen erstellt von Meatüs"
    ),
    Issue(4, "Redesign", "", "", ""),
    Issue(7, "API connection", "", "", "")
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
    val items = BoardItem.values()
    HorizontalPager(state = pagerState, count = items.size) { page ->
        when (items[page]) {
            BoardItem.OPEN -> {
                IssueBoardItem(
                    boardTitle = R.string.open,
                    issueList = issueList,
                    boardColor = Color.Black,
                    currentPageIndex = page,
                    onClickIssue = onClickIssue,
                    onClickNewIssue = onClickNewIssue
                )
            }
            BoardItem.TODO -> {
                IssueBoardItem(
                    boardTitle = R.string.todo,
                    issueList = issueList,
                    boardColor = Green500,
                    currentPageIndex = page,
                    onClickIssue = onClickIssue,
                    onClickNewIssue = onClickNewIssue
                )
            }
            BoardItem.DOING -> {
                IssueBoardItem(
                    boardTitle = R.string.doing,
                    issueList = issueList,
                    boardColor = Blue500,
                    currentPageIndex = page,
                    onClickIssue = onClickIssue,
                    onClickNewIssue = onClickNewIssue
                )
            }
            BoardItem.REVIEW -> {
                IssueBoardItem(
                    boardTitle = R.string.review,
                    issueList = issueList,
                    boardColor = Yellow500,
                    currentPageIndex = page,
                    onClickIssue = onClickIssue,
                    onClickNewIssue = onClickNewIssue
                )
            }
            BoardItem.BLOCKER -> {
                IssueBoardItem(
                    boardTitle = R.string.blocker,
                    issueList = issueList,
                    boardColor = Red500,
                    currentPageIndex = page,
                    onClickIssue = onClickIssue,
                    onClickNewIssue = onClickNewIssue
                )
            }
            BoardItem.CLOSED -> {
                IssueBoardItem(
                    boardTitle = R.string.closed,
                    issueList = issueList,
                    boardColor = Color.Black,
                    currentPageIndex = page,
                    onClickIssue = onClickIssue,
                    onClickNewIssue = onClickNewIssue
                )
            }
        }
    }
}

internal enum class BoardItem(@StringRes val title: Int) {
    OPEN(R.string.open),
    TODO(R.string.todo),
    DOING(R.string.doing),
    REVIEW(R.string.review),
    BLOCKER(R.string.blocker),
    CLOSED(R.string.closed)
}