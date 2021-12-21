package de.lucas.clockwork_android.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.lucas.clockwork_android.R
import de.lucas.clockwork_android.model.NavigationItem.*
import de.lucas.clockwork_android.model.NavigationItem.Companion.LOGIN
import de.lucas.clockwork_android.ui.theme.roundedShape

@Composable
fun Root() {
    val navController = rememberNavController()
    var showBottomNavigation by remember { mutableStateOf(false) }
    Scaffold(
        bottomBar = {
            if (showBottomNavigation) BottomNavigationBar(navController = navController)
        },
        floatingActionButton = {
            if (showBottomNavigation) {
                FloatingActionButton(
                    onClick = { /*TODO*/ },
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
                            popUpTo(LOGIN) {
                                inclusive = true
                            }
                        }
                        showBottomNavigation = true
                    },
                    onClickSignUp = {
                        navController.navigate(TOGGLE.route) {
                            popUpTo(LOGIN) {
                                inclusive = true
                            }
                        }
                        showBottomNavigation = true
                    },
                )
            }
            composable(TOGGLE.route) {
                ToggleScreen()
            }
            composable(BOARD.route) {

            }
            composable(STATISTIC.route) {

            }
            composable(PROFILE.route) {

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


@Composable
fun TopAppBar(title: String) {
    TopAppBar(
        title = { Text(title) },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = contentColorFor(MaterialTheme.colors.primarySurface),
        elevation = 0.dp
    )
}