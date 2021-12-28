package de.lucas.clockwork_android.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.gson.Gson
import de.lucas.clockwork_android.R
import de.lucas.clockwork_android.model.InfoCategory
import de.lucas.clockwork_android.model.Issue
import de.lucas.clockwork_android.model.NavigationItem.*
import de.lucas.clockwork_android.model.NavigationItem.Companion.DATA_PROTECTION
import de.lucas.clockwork_android.model.NavigationItem.Companion.IMPRINT
import de.lucas.clockwork_android.model.NavigationItem.Companion.INFO
import de.lucas.clockwork_android.model.NavigationItem.Companion.ISSUE_DETAIL
import de.lucas.clockwork_android.model.NavigationItem.Companion.ISSUE_EDIT
import de.lucas.clockwork_android.model.NavigationItem.Companion.LICENSES
import de.lucas.clockwork_android.model.NavigationItem.Companion.LOGIN
import de.lucas.clockwork_android.model.NavigationItem.Companion.VERSION
import de.lucas.clockwork_android.model.Project
import de.lucas.clockwork_android.ui.info.*
import de.lucas.clockwork_android.ui.theme.roundedShape

// For testing purpose
val listOfIssues = listOf(
    Project(
        "IT-Projekt",
        listOf(
            Issue(2, "Bug Fix", "IT-Projekt", "", ""),
            Issue(4, "Andere Fixes", "IT-Projekt", "", "")
        )
    ),
    Project(
        "Vinson",
        listOf(Issue(2, "Redesign", "Vinson", "", ""), Issue(4, "Irgendwas", "Vinson", "", ""))
    )
)

@ExperimentalPagerApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Root() {
    val navController = rememberNavController()
    var showBottomNavigation by remember { mutableStateOf(false) }
    var showIssuePickerList by remember { mutableStateOf(false) }
    var appTitle by remember { mutableStateOf("") }
    var showNavigationIcon by remember { mutableStateOf(false) }
    var showTogglePlayer by remember { mutableStateOf(false) }
    var toggleIssue by remember { mutableStateOf(Issue(-1, "", "", "", "")) }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (showBottomNavigation) {
                    CustomTopBar(
                        title = appTitle,
                        onClickBack = { navController.popBackStack() },
                        showNavigationIcon = showNavigationIcon
                    )
                    if (showTogglePlayer) {
                        TogglePlayer(issue = toggleIssue) { showTogglePlayer = false }
                    }
                }
            }
        },
        bottomBar = {
            if (showBottomNavigation) BottomNavigationBar(navController = navController)
        },
        floatingActionButton = {
            if (showBottomNavigation) {
                FloatingActionButton(
                    onClick = { showIssuePickerList = true },
                    backgroundColor = MaterialTheme.colors.primary,
                    shape = roundedShape,
                    modifier = Modifier.border(
                        width = 2.dp,
                        color = Color.White,
                        shape = roundedShape
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_plus),
                        contentDescription = ""
                    )
                }
                if (showIssuePickerList) {
                    IssuePickerList(
                        issueList = listOfIssues,
                        onStartToggle = { issue ->
                            toggleIssue = issue
                            showTogglePlayer = true
                        },
                        onClose = {
                            /* TODO save time and refresh list */
                            showIssuePickerList = false
                        }
                    )
                }
            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = LOGIN,
            Modifier.padding(innerPadding)
        ) {
            composable(LOGIN) {
                LoginScreen(
                    onClickLogin = {
                        navController.navigate(TOGGLE.route) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                        showBottomNavigation = true
                    },
                    onClickSignUp = {
                        navController.navigate(TOGGLE.route) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                        showBottomNavigation = true
                    },
                )
            }
            composable(TOGGLE.route) {
                appTitle = stringResource(id = R.string.time_record)
                showNavigationIcon = false
                ToggleScreen()
            }
            composable(BOARD.route) {
                appTitle = stringResource(id = R.string.issue_board)
                showNavigationIcon = false
                IssueBoardScreen(
                    issueList,
                    { issue ->
                        val jsonIssue = Gson().toJson(issue)
                        navController.navigate("$ISSUE_DETAIL/$jsonIssue")
                    },
                    { navController.navigate(ISSUE_EDIT) }
                )
            }
            composable(STATISTIC.route) {
                appTitle = stringResource(id = R.string.statistic)
                showNavigationIcon = false
                StatisticScreen()
            }
            composable(PROFILE.route) {
                appTitle = stringResource(id = R.string.profile)
                showNavigationIcon = false
                ProfileScreen(
                    { navController.navigate(INFO) },
                    {
                        navController.navigate(LOGIN) {
                            showBottomNavigation = false
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    },
                    { navController.navigate(TOGGLE.route) }
                )
            }
            composable(
                route = "$ISSUE_DETAIL/{issue}",
                arguments = listOf(navArgument("issue")
                { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments
                    ?.getString("issue").let { json ->
                        val issue = Gson().fromJson(json, Issue::class.java)
                        showNavigationIcon = true
                        appTitle = stringResource(id = R.string.issue_details)
                        IssueDetailScreen(issue) { navController.navigate("$ISSUE_EDIT/$json") }
                    }
            }
            composable(
                route = "$ISSUE_EDIT/{issue}",
                arguments = listOf(navArgument("issue")
                { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("issue").let { json ->
                    val issue = Gson().fromJson(json, Issue::class.java)
                    appTitle = stringResource(id = R.string.edit_issue)
                    showNavigationIcon = true
                    EditIssueScreen(
                        issue = issue,
                        buttonText = R.string.edit
                    ) { navController.popBackStack() }
                }
            }
            composable(ISSUE_EDIT) {
                appTitle = stringResource(id = R.string.create_issue)
                showNavigationIcon = true
                EditIssueScreen(
                    issue = null,
                    buttonText = R.string.create
                ) { navController.popBackStack() }
            }
            composable(INFO) {
                appTitle = stringResource(id = R.string.info)
                showNavigationIcon = true
                InfoScreen(
                    categories = listOf(
                        InfoCategory("imprint", R.string.imprint),
                        InfoCategory("licenses", R.string.rights_and_licenses),
                        InfoCategory("data_protection", R.string.data_protection),
                        InfoCategory("version", R.string.version_number)
                    ),
                    onClickBack = { navController.popBackStack() },
                    onClickCategory = { category ->
                        navController.navigate(category.id)
                    }
                )
            }
            composable(IMPRINT) {
                showNavigationIcon = true
                appTitle = stringResource(id = R.string.imprint)
                ImprintScreen()
            }
            composable(LICENSES) {
                showNavigationIcon = true
                appTitle = stringResource(id = R.string.rights_and_licenses)
                RightsAndLicencesScreen()
            }
            dialog(VERSION) { stackEntry ->
                val model = viewModel<VersionViewModel>(stackEntry)
                VersionDialog(
                    title = model.appName,
                    version = model.getCurrentVersion(),
                    onDismiss = { navController.popBackStack() }
                )
            }
            composable(DATA_PROTECTION) {
                showNavigationIcon = true
                appTitle = stringResource(id = R.string.data_protection)
                DataProtectionScreen()
            }
        }
    }
}

@Composable
internal fun BottomNavigationBar(navController: NavController) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        values().forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null
                    )
                },
                label = { Text(item.title) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
            if (item.ordinal == 1) {
                BottomNavigationItem(
                    icon = { },
                    label = { },
                    selected = false,
                    onClick = { },
                    enabled = false
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
internal fun CustomTopBar(
    title: String,
    onClickBack: () -> Unit,
    showNavigationIcon: Boolean
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = contentColorFor(MaterialTheme.colors.primarySurface),
        elevation = 0.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (showNavigationIcon) {
                    IconButton(onClick = { onClickBack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back_light),
                            contentDescription = "",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                }
                Text(
                    text = title,
                    modifier = Modifier.padding(start = 16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}