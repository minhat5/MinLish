package com.minlish.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.minlish.ui.screen.auth.ForgotPasswordScreen
import com.minlish.ui.screen.auth.LoginScreen
import com.minlish.ui.screen.auth.RegisterScreen
import com.minlish.ui.screen.auth.SelectCefrLevelScreen
import com.minlish.ui.screen.auth.SelectLearningGoalScreen
import com.minlish.ui.screen.auth.SelectLevelScreen
import com.minlish.ui.screen.dashboardHome.HomeScreen
import com.minlish.ui.screen.profile.ProfileScreen
import com.minlish.ui.screen.vocabs.AddDeckScreen
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import com.minlish.ui.screen.deck.DeckScreen
import com.minlish.ui.screen.deckDetail.DeckDetailScreen
import com.minlish.ui.screen.flashcard.FlashcardScreen
import com.minlish.ui.screen.vocabularyDetail.VocabularyDetailScreen
import com.minlish.ui.screen.dailyReviewSummary.DailyReviewSummary
import androidx.navigation.navArgument
import com.minlish.ui.screen.vocabs.AddVocabsScreen

private object Routes {
    const val HOME = "home"
    const val DECKS = "decks"
    const val ANALYTICS = "analytics"
    const val PROFILE = "profile"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgotPassword"
    const val SELECT_LEVEL = "selectLevel"
    const val SELECT_CERTIFICATE = "selectCertificate"
    const val SELECT_LEARNING_GOAL = "selectLearningGoal"
    const val ARG_DECK_ID = "deckId"
    const val DECK_DETAIL = "deck_detail/{$ARG_DECK_ID}"
    const val FLASHCARD = "flashcard/{$ARG_DECK_ID}"
    const val ADD_VOCAB = "add_vocab/{$ARG_DECK_ID}"
    const val ARG_WORD = "word"
    const val VOCABULARY_DETAIL = "vocabulary_detail/{$ARG_WORD}"
    const val ARG_WORDS_COUNT = "wordsCount"
    const val ARG_ACCURACY = "accuracy"
    const val DAILY_REVIEW_SUMMARY = "daily_review_summary/{$ARG_WORDS_COUNT}/{$ARG_ACCURACY}"
    const val ADD_DECK = "add_deck"

    fun flashcard(deckId: String): String = "flashcard/$deckId"
    fun deckDetail(deckId: String): String = "deck_detail/$deckId"
    fun addVocab(deckId: String): String = "add_vocab/$deckId"
    fun vocabularyDetail(word: String): String = "vocabulary_detail/$word"
    fun dailyReviewSummary(wordsCount: Int, accuracy: Int): String = "daily_review_summary/$wordsCount/$accuracy"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory())
    val setupViewModel: SetupViewModel = viewModel(factory = SetupViewModelFactory())
    var analyticsResetKey by rememberSaveable { mutableIntStateOf(0) }

    // Determine start destination based on login status
    val startDestination = if (AppContainer.isLoggedInUseCase()) {
        Routes.HOME
    } else {
        Routes.LOGIN
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.HOME) {
            MainScaffold(
                currentRoute = Routes.HOME,
                onTabSelect = { route ->
                    if (route == Routes.ANALYTICS) analyticsResetKey++
                    handleTabNavigation(route, navController, shouldGuardProfile = true)
                }
            ) { padding ->
                HomeScreen(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    onContinueLearningClick = {
                        handleTabNavigation(Routes.DECKS, navController, shouldGuardProfile = true)
                    }
                )
            }
        }
        composable(Routes.DECKS) {
            MainScaffold(
                currentRoute = Routes.DECKS,
                onTabSelect = { route ->
                    if (route == Routes.ANALYTICS) analyticsResetKey++
                    handleTabNavigation(route, navController, shouldGuardProfile = true)
                }
            ) { padding ->
//                AddDeckScreen(modifier = Modifier.fillMaxSize().padding(padding))
                DeckScreen(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    onAddDeckClick = {
                    navController.navigate(Routes.ADD_DECK)
                },
                onDeckSelect = { deckId -> navController.navigate(Routes.deckDetail(deckId)) })
            }
        }
        composable(Routes.ADD_DECK) {
            AddDeckScreen(
                onDeckCreated = {
                    navController.navigate(Routes.DECKS) {
                        popUpTo(Routes.DECKS) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.DECK_DETAIL,
            arguments = listOf(navArgument(Routes.ARG_DECK_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getString(Routes.ARG_DECK_ID).orEmpty()
            DeckDetailScreen(
                deckId = deckId,
                onStartLearning = { selectedDeckId -> navController.navigate(Routes.flashcard(selectedDeckId)) },
                onAddWord = { selectedDeckId -> navController.navigate(Routes.addVocab(selectedDeckId)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.ADD_VOCAB,
            arguments = listOf(navArgument(Routes.ARG_DECK_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getString(Routes.ARG_DECK_ID).orEmpty()
            AddVocabsScreen(
                deckId = deckId,
                onWordAdded = {
                    navController.navigate(Routes.deckDetail(deckId)) {
                        popUpTo(Routes.deckDetail(deckId)) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.FLASHCARD,
            arguments = listOf(navArgument(Routes.ARG_DECK_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getString(Routes.ARG_DECK_ID).orEmpty()
            FlashcardScreen(
                deckId = deckId,
                onBackToHome = { navController.popBackStack(Routes.DECKS, inclusive = false) },
                onViewDetailClick = { word -> navController.navigate(Routes.vocabularyDetail(word)) },
                onSessionComplete = { wordsCount, accuracy ->
                    navController.navigate(Routes.dailyReviewSummary(wordsCount, accuracy)) {
                        popUpTo(Routes.flashcard(deckId)) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Routes.VOCABULARY_DETAIL,
            arguments = listOf(navArgument(Routes.ARG_WORD) { type = NavType.StringType })
        ) { backStackEntry ->
            val word = backStackEntry.arguments?.getString(Routes.ARG_WORD).orEmpty()
            VocabularyDetailScreen(
                word = word,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.DAILY_REVIEW_SUMMARY,
            arguments = listOf(
                navArgument(Routes.ARG_WORDS_COUNT) { type = NavType.IntType },
                navArgument(Routes.ARG_ACCURACY) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val wordsCount = backStackEntry.arguments?.getInt(Routes.ARG_WORDS_COUNT) ?: 0
            val accuracy = backStackEntry.arguments?.getInt(Routes.ARG_ACCURACY) ?: 0
            DailyReviewSummary(
                wordsCount = wordsCount,
                accuracy = accuracy,
                onContinueClick = {
                    navController.popBackStack(Routes.DECKS, inclusive = false)
                }
            )
        }
        composable(Routes.ANALYTICS) {
            AnalyticsScreen(
                modifier = Modifier.fillMaxSize(),
                selectedBottomTab = routeToTab(Routes.ANALYTICS),
                resetProgressTabKey = analyticsResetKey,
                onBottomTabClick = { tab ->
                    val route = tabToRoute(tab)
                    if (route == Routes.ANALYTICS) analyticsResetKey++
                    handleTabNavigation(route, navController, shouldGuardProfile = true)
                },
                onProfileClick = {
                    handleTabNavigation(Routes.PROFILE, navController, shouldGuardProfile = true)
                },
                onStartReviewSession = {
                    handleTabNavigation(Routes.DECKS, navController, shouldGuardProfile = true)
                }
            )
        }
        composable(Routes.PROFILE) {
            MainScaffold(
                currentRoute = Routes.PROFILE,
                onTabSelect = { route ->
                    if (route == Routes.ANALYTICS) analyticsResetKey++
                    handleTabNavigation(route, navController, shouldGuardProfile = true)
                }
            ) { padding ->
                ProfileScreen(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) {
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
                viewModel = authViewModel,
                onLoginSuccess = { userProfile ->
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                        //restoreState = true
                    }
                },
                onNavigateRegister = { navController.navigate(Routes.REGISTER) },
                onNavigateForgotPassword = {
                    authViewModel.clearMessages()
                    navController.navigate(Routes.FORGOT_PASSWORD)
                }
            )
        }
        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                viewModel = authViewModel,
                onBackToLogin = {
                    authViewModel.clearMessages()
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel = authViewModel,
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
                },
                bottomPadding = when (currentRoute) {
                    Routes.HOME, Routes.DECKS, Routes.PROFILE -> 4.dp
                    else -> 16.dp
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

private fun NavController.navigateToBottomTab(tab: String) {
    val route = when (tab) {
        "Home" -> Routes.HOME
        "Decks" -> Routes.DECKS
        "Analytics" -> Routes.ANALYTICS
        "Profile" -> Routes.PROFILE
        else -> return
    }

    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
