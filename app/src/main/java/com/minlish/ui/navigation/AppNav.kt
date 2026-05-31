package com.minlish.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.minlish.di.AppContainer
import com.minlish.ui.common.viewmodel.AuthViewModel
import com.minlish.ui.common.viewmodel.AuthViewModelFactory
import com.minlish.ui.common.viewmodel.SetupViewModel
import com.minlish.ui.common.viewmodel.SetupViewModelFactory
import com.minlish.ui.common.component.BottomNav
import com.minlish.ui.common.component.TopBar
import com.minlish.ui.screen.analytics.AnalyticsScreen
import com.minlish.ui.screen.auth.LoginScreen
import com.minlish.ui.screen.auth.RegisterScreen
import com.minlish.ui.screen.auth.SelectCefrLevelScreen
import com.minlish.ui.screen.auth.SelectLearningGoalScreen
import com.minlish.ui.screen.auth.SelectLevelScreen
import com.minlish.ui.screen.dashboardHome.HomeScreen
import com.minlish.ui.screen.deck.DeckScreen
import com.minlish.ui.screen.profile.ProfileScreen
import com.minlish.ui.screen.profile.ProfileStatUiModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.Modifier

private object Routes {
    const val HOME = "home"
    const val DECKS = "decks"
    const val ANALYTICS = "analytics"
    const val PROFILE = "profile"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val SELECT_LEVEL = "selectLevel"
    const val SELECT_CERTIFICATE = "selectCertificate"
    const val SELECT_LEARNING_GOAL = "selectLearningGoal"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory())
    val setupViewModel: SetupViewModel = viewModel(factory = SetupViewModelFactory())

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            MainScaffold(
                currentRoute = Routes.HOME,
                onTabSelect = { route ->
                    handleTabNavigation(route, navController, shouldGuardProfile = true)
                }
            ) { padding ->
                HomeScreen(modifier = Modifier.fillMaxSize().padding(padding))
            }
        }
        composable(Routes.DECKS) {
            MainScaffold(
                currentRoute = Routes.DECKS,
                onTabSelect = { route ->
                    handleTabNavigation(route, navController, shouldGuardProfile = true)
                }
            ) { padding ->
                DeckScreen(modifier = Modifier.fillMaxSize().padding(padding))
            }
        }
        composable(Routes.ANALYTICS) {
            MainScaffold(
                currentRoute = Routes.ANALYTICS,
                onTabSelect = { route ->
                    handleTabNavigation(route, navController, shouldGuardProfile = true)
                }
            ) { padding ->
                AnalyticsScreen(modifier = Modifier.fillMaxSize().padding(padding))
            }
        }
        composable(Routes.PROFILE) {
            MainScaffold(
                currentRoute = Routes.PROFILE,
                onTabSelect = { route ->
                    handleTabNavigation(route, navController, shouldGuardProfile = true)
                }
            ) { padding ->
                ProfileScreen(
                    profileStats = previewProfileStats, 
                    modifier = Modifier.fillMaxSize().padding(padding),
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { userProfile ->
                    navController.navigate(Routes.HOME) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateRegister = { navController.navigate(Routes.REGISTER) }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = { userProfile ->
                    navController.navigate(Routes.SELECT_LEVEL) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
                onNavigateLogin = { navController.popBackStack() }
            )
        }
        composable(Routes.SELECT_LEVEL) {
            SelectLevelScreen(
                onLevelSelected = { level ->
                    setupViewModel.saveLevel(level)
                    navController.navigate(Routes.SELECT_CERTIFICATE)
                },
                onSkip = {
                    navController.navigate(Routes.SELECT_LEARNING_GOAL)
                }
            )
        }
        composable(Routes.SELECT_CERTIFICATE) {
            SelectCefrLevelScreen(
                onCefrLevelSelected = { level ->
                    setupViewModel.saveCefrLevel(level)
                    navController.navigate(Routes.SELECT_LEARNING_GOAL)
                },
                onSkip = {
                    navController.navigate(Routes.SELECT_LEARNING_GOAL)
                },
                onBack = {
                    // Back từ SelectCefrLevel quay lại SelectLevel
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.SELECT_LEARNING_GOAL) {
            SelectLearningGoalScreen(
                onGoalSelected = { goal ->
                    setupViewModel.saveLearningGoal(goal)
                    navController.navigate(Routes.HOME) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onSkip = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onBack = {
                    navController.navigate(Routes.SELECT_CERTIFICATE) {
                        popUpTo(Routes.SELECT_LEARNING_GOAL) { inclusive = false }
                    }
                }
            )
        }
    }
}

@Composable
private fun MainScaffold(
    currentRoute: String,
    onTabSelect: (String) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                mainTitle = "MinLish",
                subTitle = when (currentRoute) {
                    Routes.HOME -> "Home"
                    Routes.DECKS -> "Decks"
                    Routes.ANALYTICS -> "Analytics"
                    Routes.PROFILE -> "Profile"
                    else -> null
                },
                onProfileClick = {
                    onTabSelect(Routes.PROFILE)
                }
            )
        },
        bottomBar = {
            BottomNav(
                selectedTab = routeToTab(currentRoute),
                onTabClick = { tab -> onTabSelect(tabToRoute(tab)) }
            )
        }
    ) { padding ->
        content(padding)
    }
}

private fun handleTabNavigation(
    route: String,
    navController: androidx.navigation.NavHostController,
    shouldGuardProfile: Boolean
) {
    val destination = if (shouldGuardProfile && route == Routes.PROFILE && !AppContainer.isLoggedInUseCase()) {
        Routes.LOGIN
    } else {
        route
    }
    navController.navigate(destination) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

private fun routeToTab(route: String?): String {
    return when (route) {
        Routes.DECKS -> "Decks"
        Routes.ANALYTICS -> "Analytics"
        Routes.PROFILE -> "Profile"
        else -> "Home"
    }
}

private fun tabToRoute(tab: String): String {
    return when (tab) {
        "Decks" -> Routes.DECKS
        "Analytics" -> Routes.ANALYTICS
        "Profile" -> Routes.PROFILE
        else -> Routes.HOME
    }
}

private val previewProfileStats = listOf(
    ProfileStatUiModel(
        icon = Icons.Filled.LibraryBooks,
        value = "42",
        label = "Decks Mastered"
    ),
    ProfileStatUiModel(
        icon = Icons.Filled.Star,
        value = "128",
        label = "Perfect Scores"
    ),
    ProfileStatUiModel(
        icon = Icons.Filled.Translate,
        value = "2.5k",
        label = "Words Learned"
    ),
    ProfileStatUiModel(
        icon = Icons.Filled.Timer,
        value = "45h",
        label = "Study Time"
    )
)
