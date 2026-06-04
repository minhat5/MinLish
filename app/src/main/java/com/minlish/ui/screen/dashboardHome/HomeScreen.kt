package com.minlish.ui.screen.dashboardHome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.minlish.ui.common.component.ProgressCard
import com.minlish.ui.common.viewmodel.HomeViewModel
import com.minlish.ui.common.viewmodel.HomeViewModelFactory
import com.minlish.ui.theme.*

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onContinueLearningClick: () -> Unit = {}
) {
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory())
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    if (uiState.isLoading) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else if (uiState.errorMessage != null) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiState.errorMessage ?: "Unknown error",
                color = Color.Red
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, top = 0.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            WelcomeSection(uiState.currentUser?.displayName ?: "User")
            BentoGridDashboard(
                streakDays = uiState.streakDays
            )
            ProgressCard(
                title = "Continue Learning",
                tag = uiState.currentDeckTag,
                subtitle = uiState.currentDeckSubtitle,
                progressText = "${uiState.deckProgress}%",
                progress = uiState.deckProgress / 100f,
                onClick = onContinueLearningClick
            )
            StatsGrid(
                wordsLearned = uiState.wordsLearned,
                weeklyProgress = uiState.weeklyProgress
            )
        }
    }
}
