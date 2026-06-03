package com.minlish.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minlish.ui.common.state.StreakState
import com.minlish.ui.common.viewmodel.ProfileViewModel
import com.minlish.ui.common.viewmodel.ProfileViewModelFactory
import com.minlish.ui.theme.*
import kotlin.math.roundToInt

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {},
    viewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory())
) {
    val uiState by viewModel.uiState.collectAsState()
    val user = uiState.currentUser
    val stats = uiState.stats
    var darkModeEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorSurface)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        ProfileAvatarCard(
            modifier = Modifier.fillMaxWidth(),
            name = user?.displayName ?: "Guest",
            level = user?.levelEstimate?.name?.toDisplayText() ?: "Beginner",
            xp = "${formatCount(stats.xp)} XP",
            achievementText = "${stats.accuracyRate.roundToInt()}% Accuracy",
            streakText = "${StreakState.streakCount} Day Streak"
        )
        Spacer(modifier = Modifier.height(24.dp))

        ProfileStatsGrid(
            decksMastered = stats.decksMastered.toString(),
            perfectScores = stats.perfectScores.toString(),
            wordsLearned = formatCount(stats.wordsLearned),
            studyTime = "${stats.studyTimeHours}h"
        )
        Spacer(modifier = Modifier.height(24.dp))

        ProfileAccountSettings(
            darkModeEnabled = darkModeEnabled,
            onDarkModeChange = { darkModeEnabled = it },
            onLogout = onLogout
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen()
}

private fun formatCount(value: Int): String {
    return when {
        value >= 1_000_000 -> "${trimDecimal(value / 1_000_000.0)}m"
        value >= 1_000 -> "${trimDecimal(value / 1_000.0)}k"
        else -> value.toString()
    }
}

private fun trimDecimal(value: Double): String {
    val roundedToOneDecimal = (value * 10).roundToInt() / 10.0
    return if (roundedToOneDecimal % 1.0 == 0.0) {
        roundedToOneDecimal.toInt().toString()
    } else {
        roundedToOneDecimal.toString()
    }
}

private fun String.toDisplayText(): String {
    return lowercase()
        .replaceFirstChar { firstChar -> firstChar.uppercase() }
}

