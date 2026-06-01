package com.minlish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Surface
import com.minlish.ui.theme.MinLishTheme
import com.minlish.di.AppContainer
import com.minlish.ui.navigation.AppNavHost
import com.minlish.ui.screen.analytics.AnalyticsScreen
import com.minlish.ui.screen.dashboardHome.HomeScreen
import com.minlish.ui.screen.vocabs.AddDeckScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppContainer.initialize(applicationContext)
        enableEdgeToEdge()
        setContent {
            MinLishTheme {
                Surface {
//                    AnalyticsScreen ()
//                    AddDeckScreen()
                    AppNavHost()
                }
            }
        }
    }
}

private object Routes {
    const val Home = "home"
    const val Decks = "decks"
    const val AddDeck = "add_deck"
    const val Flashcard = "flashcard"
    const val VocabularyDetail = "vocabulary_detail"
    const val Analytics = "analytics"
    const val Profile = "profile"
}

@Composable
private fun MinLishApp() {
    val navController = rememberNavController()
    val profileStats = remember {
        listOf(
            ProfileStatUiModel(Icons.Filled.LibraryBooks, "42", "Decks Mastered"),
            ProfileStatUiModel(Icons.Filled.Star, "128", "Perfect Scores"),
            ProfileStatUiModel(Icons.Filled.Translate, "2.5k", "Words Learned"),
            ProfileStatUiModel(Icons.Filled.Timer, "45h", "Study Time")
        )
    }

    NavHost(
        navController = navController,
        startDestination = Routes.Decks
    ) {
        composable(Routes.Home) {
            HomeScreen()
        }

        composable(Routes.Decks) {
            DeckScreen(
                onDeckSelect = { navController.navigate(Routes.Flashcard) },
                onAddDeckClick = { navController.navigate(Routes.AddDeck) },
                onBottomTabClick = { navController.navigateToBottomTab(it) }
            )
        }

        composable(Routes.AddDeck) {
            AddDeckScreen(
                selectedTab = "Decks",
                onBottomTabClick = { navController.navigateToBottomTab(it) }
            )
        }

        composable(Routes.Flashcard) {
            FlashcardScreen(
                onBackToHome = { navController.popBackStack(Routes.Decks, inclusive = false) },
                onViewDetailClick = { navController.navigate(Routes.VocabularyDetail) }
            )
        }

        composable(Routes.VocabularyDetail) {
            VocabularyDetailScreen()
        }

        composable(Routes.Analytics) {
            AnalyticsScreen()
        }

        composable(Routes.Profile) {
            ProfileScreen(profileStats = profileStats)
        }
    }
}

private fun NavController.navigateToBottomTab(tab: String) {
    val route = when (tab) {
        "Home" -> Routes.Home
        "Decks" -> Routes.Decks
        "Analytics" -> Routes.Analytics
        "Profile" -> Routes.Profile
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
