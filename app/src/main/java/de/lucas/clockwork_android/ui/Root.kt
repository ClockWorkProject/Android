package de.lucas.clockwork_android.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.squareup.moshi.Moshi
import de.lucas.clockwork_android.R
import de.lucas.clockwork_android.model.InfoCategory
import de.lucas.clockwork_android.model.Issue
import de.lucas.clockwork_android.model.NavigationItem.*
import de.lucas.clockwork_android.model.NavigationItem.Companion.DATA_PROTECTION
import de.lucas.clockwork_android.model.NavigationItem.Companion.IMPRINT
import de.lucas.clockwork_android.model.NavigationItem.Companion.INFO
import de.lucas.clockwork_android.model.NavigationItem.Companion.ISSUE_CREATE
import de.lucas.clockwork_android.model.NavigationItem.Companion.ISSUE_DETAIL
import de.lucas.clockwork_android.model.NavigationItem.Companion.ISSUE_EDIT
import de.lucas.clockwork_android.model.NavigationItem.Companion.LICENSES
import de.lucas.clockwork_android.model.NavigationItem.Companion.LOGIN
import de.lucas.clockwork_android.model.NavigationItem.Companion.VERSION
import de.lucas.clockwork_android.model.Project
import de.lucas.clockwork_android.ui.info.DataProtectionScreen
import de.lucas.clockwork_android.ui.info.ImprintScreen
import de.lucas.clockwork_android.ui.info.RightsAndLicencesScreen
import de.lucas.clockwork_android.ui.info.VersionDialog
import de.lucas.clockwork_android.ui.theme.roundedShape
import de.lucas.clockwork_android.viewmodel.*
import timber.log.Timber
import java.net.URLEncoder

@ExperimentalComposeUiApi
@ExperimentalPagerApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Root() {
    val rootViewModel: RootViewModel = viewModel()
    val context = LocalContext.current
    val navController = rememberNavController()
    val togglePlayerViewModel: TogglePlayerViewModel = hiltViewModel()
    val editViewModel: EditIssueViewModel = hiltViewModel()
    val moshi = Moshi.Builder().build()
    var groupId by remember { mutableStateOf(rootViewModel.getGroupId()) }
    var userRole by remember { mutableStateOf(rootViewModel.getUserRole()) }
    lateinit var timer: CountUpTimer

    // Get all data from firebase if user is in a group
    if (groupId != "") {
        rootViewModel.getProjectData(groupId!!)
        rootViewModel.getAllToggles(groupId!!, rootViewModel.getUserId()!!)
        // If user is "admin" get all members of group
        if (userRole == "admin") {
            rootViewModel.getAllMembers(groupId!!)
        }
    }

    /**
     * Starts the count up timer
     * Int.MAX_VALUE to count indefinitely until canceled
     * in onCount(): state in viewModel gets updated every second to update ui element of shown text in player
     */
    fun startTimer(pausedTime: Int) {
        timer = object : CountUpTimer(Int.MAX_VALUE, 1) {

            override fun onCount(count: Int) {
                togglePlayerViewModel.displayTime(pausedTime, count)
                Timber.e(togglePlayerViewModel.toggleTimeDisplay.value)
            }

            override fun onFinish() {

            }
        }.start() as CountUpTimer
    }

    /**
     * Check if a Toggle is active, for the scenario, that the user closed the app while Toggle is active
     * After reopen it gets checked if a Toggle is saved, to show TogglePlayer with all it's information
     */
    if (togglePlayerViewModel.getToggleIssue()!!.number != "") {
        // App restart while toggle is paused
        if (togglePlayerViewModel.getIsTogglePaused()) {
            // Start timer with time from last pause
            startTimer(togglePlayerViewModel.getCurrentToggleTime())
            // Show time in TogglePlayer
            togglePlayerViewModel.displayTime(togglePlayerViewModel.getCurrentToggleTime(), 0)
            // Pause Toggle
            timer.cancel()
            // Set state to true, to change icons in togglePlayer
            togglePlayerViewModel.setIsTogglePaused(true)
        } else {
            // Start timer with time from counted time in app and time while app was closed (gets added together)
            startTimer(togglePlayerViewModel.getAppClosedTime((System.currentTimeMillis() / 1000).toInt() - togglePlayerViewModel.getPausedTime()))
        }
        Timber.e(togglePlayerViewModel.getIsTogglePaused().toString())
        // Set state to true, to show togglePlayer
        rootViewModel.setShowTogglePlayer(true)
    }
    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                /**
                 * Check if state is true (which will be set true if not in LoginScreen)
                 * Show TopBar and BottomNavigationBar if true
                 */
                if (rootViewModel.showBottomNavigation.value) {
                    CustomTopBar(
                        title = rootViewModel.appTitle.value,
                        onClickBack = { navController.popBackStack() },
                        showNavigationIcon = rootViewModel.showNavigationIcon.value
                    )
                    /**
                     * Check if state to show TogglePlayer is true
                     * Show TogglePlayer and Notification if true
                     */
                    if (rootViewModel.showTogglePlayer.value) {
                        Notification(
                            context = context,
                            channelId = stringResource(id = R.string.app_name),
                            notificationId = 0,
                            textTitle = stringResource(id = R.string.toggle_active),
                            textContent = "#${togglePlayerViewModel.getToggleIssue()!!.number} ${togglePlayerViewModel.getToggleIssue()!!.name}"
                        )
                        TogglePlayer(
                            issue = togglePlayerViewModel.getToggleIssue()!!,
                            project = togglePlayerViewModel.getToggleProject()!!,
                            time = togglePlayerViewModel.toggleTimeDisplay.value,
                            onPause = {
                                // Stop Timer
                                timer.cancel()
                                togglePlayerViewModel.setIsTogglePaused(true)
                            },
                            onResume = {
                                // Start Timer
                                startTimer(togglePlayerViewModel.getCurrentToggleTime())
                                togglePlayerViewModel.setPausedTime()
                                togglePlayerViewModel.setIsTogglePaused(false)
                            },
                            onClose = {
                                // Stop Timer
                                timer.cancel()
                                // Send toggle to backend
                                rootViewModel.saveToggle(
                                    togglePlayerViewModel.getCurrentToggleTime().toString(),
                                    togglePlayerViewModel.getToggleIssue()!!,
                                    togglePlayerViewModel.getToggleProject()!!
                                )
                                Timber.e(togglePlayerViewModel.getToggleIssue()!!.toString())
                                // Reset time to 0 and remove saved issue
                                togglePlayerViewModel.resetToggle()
                                togglePlayerViewModel.setIsTogglePaused(false)
                                removeNotification(context)
                                rootViewModel.setShowTogglePlayer(false)
                            },
                            viewModel = togglePlayerViewModel
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (rootViewModel.showBottomNavigation.value) BottomNavigationBar(navController = navController)
        },
        floatingActionButton = {
            if (rootViewModel.showBottomNavigation.value) {
                FloatingActionButton(
                    // Set showIssuePicker to true, to show IssuePicker when fab is being clicked
                    onClick = { rootViewModel.setShowIssuePicker(true) },
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
                // Show IssuePicker when state is true
                if (rootViewModel.showIssuePickerList.value) {
                    val model: IssuePickerListViewModel = hiltViewModel()
                    IssuePickerList(
                        projectList = if (rootViewModel.getGroupId() != "") rootViewModel.projectList else listOf(),
                        viewModel = model,
                        onStartToggle = { issue, project ->
                            if (togglePlayerViewModel.getToggleIssue()!!.number == "") {
                                togglePlayerViewModel.setStartTime()
                                rootViewModel.setShowTogglePlayer(true)
                                togglePlayerViewModel.setToggle(issue, project)
                                startTimer(togglePlayerViewModel.getCurrentToggleTime())
                            }
                        },
                        onClose = { rootViewModel.setShowIssuePicker(false) }
                    )
                }
            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        NavHost(
            navController,
            // If a user exists (is currently logged in) -> navigate straight to ToggleScreen and skip login
            startDestination = if (rootViewModel.currentUser() != null) TOGGLE.route else LOGIN,
            Modifier.padding(innerPadding)
        ) {
            composable(LOGIN) { stackEntry ->
                val model: LoginViewModel = hiltViewModel(stackEntry)
                LoginScreen(
                    isLoading = model.getIsLoading(),
                    currentState = model.getState(),
                    password = model.getPassword(),
                    email = model.getEmail(),
                    setEmail = { email -> model.setEmail(email) },
                    setState = { state -> model.setState(state) },
                    setPassword = { password -> model.setPassword(password) },
                    loginUser = {
                        model.loginUser(AppCompatActivity()) { id, role ->
                            navController.navigate(TOGGLE.route) {
                                popUpTo(0) {
                                    inclusive = true
                                }
                            }
                            rootViewModel.setShowBottomNavigation(true)
                            groupId = id
                            userRole = role
                        }
                    },
                    signUpUser = {
                        model.signUpUser(AppCompatActivity()) {
                            navController.navigate(TOGGLE.route) {
                                popUpTo(0) {
                                    inclusive = true
                                }
                            }
                            rootViewModel.setShowBottomNavigation(true)
                        }
                    }
                )
            }
            composable(TOGGLE.route) { stackEntry ->
                val model: ToggleViewModel = hiltViewModel(stackEntry)
                rootViewModel.setAppTitle(stringResource(id = R.string.time_record))
                rootViewModel.setShowNavigationIcon(false)
                rootViewModel.setShowBottomNavigation(true)
                ToggleScreen(
                    toggleList = if (rootViewModel.getGroupId() != "") rootViewModel.getSortedToggles() else listOf(),
                    groupId = model.getGroupId() ?: "",
                    showEmptyState = model.showEmptyState.value,
                    showNoGroupState = model.noGroupFoundState.value,
                    showToggleList = model.showToggleList.value,
                    setShowEmptyState = { state ->
                        model.setEmptyState(state)
                    },
                    setShowToggleList = { state ->
                        model.setShowToggleList(state)
                    },
                    setShowNoGroupFound = { state ->
                        model.setNoGroupFound(state)
                    },
                    joinGroup = { id ->
                        model.joinGroup(groupId ?: "") { joined ->
                            if (joined) {
                                groupId = id
                            }
                        }
                    },
                    createGroup = { name ->
                        model.createGroup(name) { id ->
                            groupId = id
                        }
                    }
                )
            }
            composable(BOARD.route) { stackEntry ->
                val model: IssueBoardViewModel = hiltViewModel(stackEntry)
                rootViewModel.setAppTitle(stringResource(id = R.string.issue_board))
                rootViewModel.setShowNavigationIcon(false)
                IssueBoardScreen(
                    projectList = if (rootViewModel.getGroupId() != "") rootViewModel.projectList else listOf(),
                    viewModel = model,
                    onClickIssue = { issue, project_id ->
                        val issueAdapter = moshi.adapter(Issue::class.java)
                        // Create Json of information of clicked Issue
                        val jsonIssue = URLEncoder.encode(issueAdapter.toJson(issue), "UTF-8")
                            .replace("+", " ")
                        editViewModel.setProjectID(project_id)
                        // Navigate with arguments of clicked Issue, to show in IssueDetailScreen
                        navController.navigate("$ISSUE_DETAIL/$jsonIssue")
                    },
                    onClickNewIssue = { project, boardState ->
                        val projectAdapter = moshi.adapter(Project::class.java)
                        // Encode to utf-8 to prevent "unterminated string"-error for special characters
                        val jsonProject = URLEncoder.encode(projectAdapter.toJson(project), "UTF-8")
                            .replace("+", " ")
                        editViewModel.setEditBoardState(boardState)
                        navController.navigate("$ISSUE_CREATE/$jsonProject")
                    }
                )
            }
            composable(STATISTIC.route) {
                rootViewModel.setAppTitle(stringResource(id = R.string.statistic))
                rootViewModel.setShowNavigationIcon(false)
                StatisticScreen(rootViewModel.getUserRole()!!, rootViewModel.memberList)
            }
            composable(PROFILE.route) { stackEntry ->
                val model: ProfileViewModel = hiltViewModel(stackEntry)
                rootViewModel.setAppTitle(stringResource(id = R.string.profile))
                rootViewModel.setShowNavigationIcon(false)
                ProfileScreen(
                    viewModel = model,
                    onClickInfo = { navController.navigate(INFO) },
                    onClickLogout = {
                        // Remove all global lists, to be able to store new data on new login
                        rootViewModel.removeAll()
                        // Sign out from firebase
                        rootViewModel.signOut()
                        navController.navigate(LOGIN) {
                            rootViewModel.setShowBottomNavigation(false)
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                        groupId = ""
                    },
                    onClickLeave = {
                        // Remove all global lists and listeners, to be able to get/store new data on new login
                        rootViewModel.removeAllListeners()
                        groupId = ""
                        rootViewModel.setProjectIndex(-1)
                        navController.navigate(TOGGLE.route)
                    }
                )
            }
            // Navigation to IssueDetailsScreen with issue as navigation argument
            composable(
                route = "$ISSUE_DETAIL/{issue}",
                arguments = listOf(navArgument("issue")
                { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments
                    ?.getString("issue").let { json ->
                        val issueAdapter = moshi.adapter(Issue::class.java)
                        val issue = issueAdapter.fromJson(json!!)
                        rootViewModel.setShowNavigationIcon(true)
                        rootViewModel.setAppTitle(stringResource(id = R.string.issue_details))
                        IssueDetailScreen(issue!!) {
                            navController.navigate(
                                "$ISSUE_EDIT/${
                                    URLEncoder.encode(
                                        json,
                                        "UTF-8"
                                    ).replace("+", " ")
                                }"
                            )
                        }
                    }
            }
            // Navigation to EditIssueScreen with issue as navigation argument
            composable(
                route = "$ISSUE_EDIT/{issue}",
                arguments = listOf(navArgument("issue")
                { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("issue").let { json ->
                    val issueAdapter = moshi.adapter(Issue::class.java)
                    val issue = issueAdapter.fromJson(json!!)
                    rootViewModel.setAppTitle(stringResource(id = R.string.edit_issue))
                    rootViewModel.setShowNavigationIcon(true)
                    EditIssueScreen(
                        issue = issue,
                        project = null,
                        projectID = editViewModel.getProjectID(),
                        isError = editViewModel.getIsError(),
                        buttonText = R.string.save,
                        state = editViewModel.getEditBoardState(),
                        updateIssue = { projectId, issueId, title, desc ->
                            editViewModel.updateIssue(projectId, issueId, title, desc)
                        },
                        createIssue = { projectId, issueId, title, desc, state ->
                            editViewModel.createIssue(projectId, issueId, title, desc, state)
                        }
                    ) {
                        navController.navigate(BOARD.route) {
                            popUpTo(BOARD.route) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
            // Navigation to EditIssueScreen with project as navigation argument
            composable(
                route = "$ISSUE_CREATE/{project}",
                arguments = listOf(navArgument("project")
                { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("project").let { json ->
                    val projectAdapter = moshi.adapter(Project::class.java)
                    val project = projectAdapter.fromJson(json!!)
                    rootViewModel.setAppTitle(stringResource(id = R.string.create_issue))
                    rootViewModel.setShowNavigationIcon(true)
                    EditIssueScreen(
                        issue = null,
                        project = project,
                        projectID = editViewModel.getProjectID(),
                        isError = editViewModel.getIsError(),
                        buttonText = R.string.create,
                        state = editViewModel.getEditBoardState(),
                        updateIssue = { projectId, issueId, title, desc ->
                            editViewModel.updateIssue(projectId, issueId, title, desc)
                        },
                        createIssue = { projectId, issueId, title, desc, state ->
                            editViewModel.createIssue(projectId, issueId, title, desc, state)
                        }
                    ) { navController.popBackStack() }
                }
            }
            composable(INFO) {
                rootViewModel.setAppTitle(stringResource(id = R.string.info))
                rootViewModel.setShowNavigationIcon(true)
                InfoScreen(
                    categories = listOf(
                        InfoCategory("imprint", R.string.imprint),
                        InfoCategory("licenses", R.string.rights_and_licenses),
                        InfoCategory("data_protection", R.string.data_protection),
                        InfoCategory("version", R.string.version_number)
                    ),
                    onClickCategory = { category ->
                        navController.navigate(category.id)
                    }
                )
            }
            composable(IMPRINT) {
                rootViewModel.setShowNavigationIcon(true)
                rootViewModel.setAppTitle(stringResource(id = R.string.imprint))
                ImprintScreen()
            }
            composable(LICENSES) {
                rootViewModel.setShowNavigationIcon(true)
                rootViewModel.setAppTitle(stringResource(id = R.string.rights_and_licenses))
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
                rootViewModel.setShowNavigationIcon(true)
                rootViewModel.setAppTitle(stringResource(id = R.string.data_protection))
                DataProtectionScreen()
            }
        }
        // If isLoading is true, show loading indicator
        if (rootViewModel.getIsLoading()) {
            LoadingIndicator(id = R.string.data_loading)
        }
    }
}

@Composable
internal fun BottomNavigationBar(navController: NavController) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        // Create an BottomNavigationItem for every provided NavigationItem
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
            // Creates an empty Item between second and third, to keep the middle section empty for fab
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

/**
 * Custom TopBar
 * @param title shows provided String as title of the TopBar
 * @param onClickBack callBack for the NavigationIcon (if shown)
 * @param showNavigationIcon boolean to check if NavigationIcon must be shown for specific Screen
 */
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