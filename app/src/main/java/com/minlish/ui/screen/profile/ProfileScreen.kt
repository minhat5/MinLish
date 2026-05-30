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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Translate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minlish.ui.screen.dailyReviewSummary.colorSurface

@Composable
fun ProfileScreen(
    profileStats: List<ProfileStatUiModel>,
    modifier: Modifier = Modifier
) {
    var darkModeEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorSurface)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        ProfileAvatarCard(
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        ProfileStatsGrid(stats = profileStats)
        Spacer(modifier = Modifier.height(24.dp))

        ProfileAccountSettings(
            darkModeEnabled = darkModeEnabled,
            onDarkModeChange = { darkModeEnabled = it }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen(profileStats = previewProfileStats)
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
