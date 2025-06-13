package com.amipet.app.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.amipet.app.R
import com.amipet.app.ui.screens.home.HomeScreen
import com.amipet.app.ui.screens.login.ForgotPasswordScreen
import com.amipet.app.ui.screens.login.LoginScreen
import com.amipet.app.ui.screens.login.RegisterScreen
import com.amipet.app.ui.screens.matches.MatchesScreen
import com.amipet.app.ui.screens.profile.ProfileScreen
import com.amipet.app.viewmodel.AuthViewModel
import com.amipet.app.viewmodel.AuthViewModelFactory
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

sealed class Screen(val route: String, val titleResId: Int) {
    object Login : Screen("login", R.string.login_screen_title)
    object Home : Screen("home", R.string.home_screen_title)
    object Matches : Screen("matches", R.string.matches_screen_title)
    object Profile : Screen("profile", R.string.profile_screen_title)
    object ForgotPassword : Screen("forgot_password", R.string.link_forgot_password)
    object Register : Screen("register", R.string.link_register)
}

@Composable
fun AmiPetNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(context)
    )
    viewModel.setNavController(navController)

    val isLoggedIn by viewModel.isLoggedInState.collectAsState()

    Scaffold(
        bottomBar = {
            if (isLoggedIn) {
                AmiPetBottomNavigation(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) Screen.Home.route else Screen.Login.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }
        ) {
            composable(Screen.Login.route) {
                LoginScreen(navController, viewModel)
            }
            composable(Screen.Home.route) {
                HomeScreen()
            }
            composable(Screen.Matches.route) {
                MatchesScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(Screen.Register.route) {
                RegisterScreen(navController, viewModel)
            }
            composable(Screen.ForgotPassword.route) {
                ForgotPasswordScreen(navController, viewModel)
            }
        }
    }
}

@Composable
fun AmiPetBottomNavigation(navController: NavHostController) {
    val items = listOf(
        Triple(Screen.Home, Icons.Outlined.Home, Icons.Filled.Home),
        Triple(Screen.Matches, Icons.Outlined.FavoriteBorder, Icons.Filled.Favorite),
        Triple(Screen.Profile, Icons.Outlined.Person, Icons.Filled.Person)
    )

    NavigationBar(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .background(Color(0xFF8AE548))
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { (screen, iconOutlined, iconFilled) ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selected) iconFilled else iconOutlined,
                        contentDescription = stringResource(id = screen.titleResId),
                        tint = if (selected) Color.White else Color(0xFF444444) // Dark gray for unselected
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = screen.titleResId),
                        color = if (selected) Color.White else Color(0xFF444444),
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color(0xFF444444),
                    selectedTextColor = Color(0xFFDBF450),
                    unselectedTextColor = Color(0xFF444444),
                    indicatorColor = Color(0xFFDBF450)
                )
            )
        }
    }
}