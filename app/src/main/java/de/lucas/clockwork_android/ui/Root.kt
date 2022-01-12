package de.lucas.clockwork_android.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
import de.lucas.clockwork_android.ui.info.*
import de.lucas.clockwork_android.ui.theme.roundedShape
import timber.log.Timber

@ExperimentalPagerApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Root(rootViewModel: RootViewModel) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val togglePlayerViewModel = TogglePlayerViewModel(context)
    val auth: FirebaseAuth = Firebase.auth
    lateinit var timer: CountUpTimer

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

    if (togglePlayerViewModel.getToggle()!!.number != -1) {
        // App restart while toggle is paused
        if (togglePlayerViewModel.getIsTogglePaused()) {
            startTimer(togglePlayerViewModel.getCurrentToggleTime())
            togglePlayerViewModel.displayTime(togglePlayerViewModel.getCurrentToggleTime(), 0)
            timer.cancel()
            togglePlayerViewModel.setIsTogglePaused(true)
        } else {
            startTimer(togglePlayerViewModel.getAppClosedTime((System.currentTimeMillis() / 1000).toInt() - togglePlayerViewModel.getPausedTime()))
        }
        Timber.e(togglePlayerViewModel.getIsTogglePaused().toString())
        rootViewModel.setShowTogglePlayer(true)
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (rootViewModel.showBottomNavigation.value) {
                    CustomTopBar(
                        title = rootViewModel.appTitle.value,
                        onClickBack = { navController.popBackStack() },
                        showNavigationIcon = rootViewModel.showNavigationIcon.value
                    )
                    if (rootViewModel.showTogglePlayer.value) {
                        Notification(
                            context = context,
                            channelId = stringResource(id = R.string.app_name),
                            notificationId = 0,
                            textTitle = stringResource(id = R.string.toggle_active),
                            textContent = "#${togglePlayerViewModel.getToggle()!!.number} ${togglePlayerViewModel.getToggle()!!.title}"
                        )
                        TogglePlayer(
                            issue = togglePlayerViewModel.getToggle()!!,
                            time = togglePlayerViewModel.toggleTimeDisplay.value,
                            onPause = {
                                timer.cancel()
                                togglePlayerViewModel.setIsTogglePaused(true)
                            },
                            onResume = {
                                startTimer(togglePlayerViewModel.getCurrentToggleTime())
                                togglePlayerViewModel.setPausedTime()
                                togglePlayerViewModel.setIsTogglePaused(false)
                            },
                            onClose = {
                                /* TODO add item to list with total toggle time */
                                timer.cancel()
                                togglePlayerViewModel.resetToggle()
                                togglePlayerViewModel.setIsTogglePaused(false)
                                removeNotification(context)
                                rootViewModel.setShowTogglePlayer(false)
                            },
                            viewModel = TogglePlayerViewModel(context)
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
                if (rootViewModel.showIssuePickerList.value) {
                    IssuePickerList(
                        issueList = projectList,
                        onStartToggle = { issue ->
                            if (togglePlayerViewModel.getToggle()!!.number == -1) {
                                togglePlayerViewModel.setStartTime()
                                rootViewModel.setShowTogglePlayer(true)
                                togglePlayerViewModel.setToggle(issue)
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
            startDestination = if (auth.currentUser != null) TOGGLE.route else LOGIN,
            Modifier.padding(innerPadding)
        ) {
            composable(LOGIN) {
                val loginViewModel = LoginViewModel(context)
                LoginScreen(
                    viewModel = loginViewModel,
                    auth = auth,
                    onClickLogin = {
                        navController.navigate(TOGGLE.route) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                        rootViewModel.setShowBottomNavigation(true)
                    },
                    onClickSignUp = {
                        navController.navigate(TOGGLE.route) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                        rootViewModel.setShowBottomNavigation(true)
                    }
                )
            }
            composable(TOGGLE.route) {
                rootViewModel.setAppTitle(stringResource(id = R.string.time_record))
                rootViewModel.setShowNavigationIcon(false)
                rootViewModel.setShowBottomNavigation(true)
                ToggleScreen()
            }
            composable(BOARD.route) {
                rootViewModel.setAppTitle(stringResource(id = R.string.issue_board))
                rootViewModel.setShowNavigationIcon(false)
                IssueBoardScreen(
                    viewModel = IssueBoardViewModel(context),
                    onClickIssue = { issue ->
                        val jsonIssue = Gson().toJson(issue)
                        navController.navigate("$ISSUE_DETAIL/$jsonIssue")
                    },
                    onClickNewIssue = { navController.navigate(ISSUE_EDIT) }
                )
            }
            composable(STATISTIC.route) {
                rootViewModel.setAppTitle(stringResource(id = R.string.statistic))
                rootViewModel.setShowNavigationIcon(false)
                StatisticScreen()
            }
            composable(PROFILE.route) {
                rootViewModel.setAppTitle(stringResource(id = R.string.profile))
                rootViewModel.setShowNavigationIcon(false)
                ProfileScreen(
                    viewModel = ProfileViewModel(context),
                    onClickInfo = { navController.navigate(INFO) },
                    onClickLogout = {
                        auth.signOut()
                        navController.navigate(LOGIN) {
                            rootViewModel.setShowBottomNavigation(false)
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    },
                    onClickLeave = { navController.navigate(TOGGLE.route) }
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
                        rootViewModel.setShowNavigationIcon(true)
                        rootViewModel.setAppTitle(stringResource(id = R.string.issue_details))
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
                    rootViewModel.setAppTitle(stringResource(id = R.string.edit_issue))
                    rootViewModel.setShowNavigationIcon(true)
                    EditIssueScreen(
                        issue = issue,
                        buttonText = R.string.save
                    ) { navController.popBackStack() }
                }
            }
            composable(ISSUE_EDIT) {
                rootViewModel.setAppTitle(stringResource(id = R.string.create_issue))
                rootViewModel.setShowNavigationIcon(true)
                EditIssueScreen(
                    issue = null,
                    buttonText = R.string.create
                ) { navController.popBackStack() }
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