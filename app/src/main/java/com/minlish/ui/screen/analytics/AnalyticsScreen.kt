package com.minlish.ui.screen.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minlish.ui.common.component.BottomNav
import com.minlish.ui.common.component.TopBar
import com.minlish.ui.common.viewmodel.AnalyticsUiState
import com.minlish.ui.common.viewmodel.AnalyticsViewModel
import com.minlish.ui.common.viewmodel.AnalyticsViewModelFactory
import com.minlish.ui.theme.*

@Composable
fun AnalyticsScreen(
    modifier: Modifier = Modifier,
    retentionLevels: List<RetentionLevelData> = defaultRetentionLevelData(),
    wordsReadyForReview: Int = 1547,
    selectedBottomTab: String = "Analytics",
    onBottomTabClick: (String) -> Unit = {},
    onProfileClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onStartReviewSession: () -> Unit = {},
    viewModel: AnalyticsViewModel = viewModel(factory = AnalyticsViewModelFactory())
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        containerColor = colorSurface,
        topBar = {
            TopBar(
                mainTitle = "MinLish",
                subTitle = "Analytics",
                onProfileClick = onProfileClick,
                onSettingsClick = onSettingsClick
            )
        },
        bottomBar = {
            BottomNav(
                selectedTab = selectedBottomTab,
                onTabClick = onBottomTabClick
            )
        }
    ) { paddingValues ->
        AnalyticsContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            uiState = uiState,
            retentionLevels = retentionLevels,
            wordsReadyForReview = wordsReadyForReview,
            onStartReviewSession = onStartReviewSession
        )
    }
}

@Composable
private fun AnalyticsContent(
    modifier: Modifier = Modifier,
    uiState: AnalyticsUiState,
    retentionLevels: List<RetentionLevelData>,
    wordsReadyForReview: Int,
    onStartReviewSession: () -> Unit
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("Progress", "Retention")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorSurface)
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = colorSurface,
            contentColor = colorPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .padding(horizontal = 32.dp)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                    height = 4.dp,
                    color = colorPrimary
                )
            },
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTabIndex == index) colorPrimary else colorOnSurfaceVariant,
                            fontSize = 16.sp,
                            fontWeight = if (selectedTabIndex == index) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            }
                        )
                    }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            when (selectedTabIndex) {
                0 -> ProgressTabContent(uiState = uiState)
                1 -> RetentionTabContent(
                    levels = retentionLevels,
                    wordsReadyForReview = wordsReadyForReview,
                    onStartReviewSession = onStartReviewSession
                )
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun ProgressTabContent(uiState: AnalyticsUiState) {
    Text(
        text = "Your Progress",
        color = colorOnSurface,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = "Keep up the great work! You're learning faster than 78% of users.",
        color = colorOnSurfaceVariant,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        modifier = Modifier.padding(top = 6.dp)
    )

    Spacer(modifier = Modifier.height(24.dp))
    ProgressSummary(data = uiState.progressSummaryData)

    Spacer(modifier = Modifier.height(24.dp))
    ConsistencyChart(
        weeklyEntries = uiState.weeklyEntries,
        monthlyEntries = uiState.monthlyEntries,
        firstMonthDayOffset = uiState.firstMonthDayOffset,
        hasStudiedToday = uiState.hasStudiedToday
    )
}

@Preview(showBackground = true)
@Composable
private fun AnalyticsScreenPreview() {
    AnalyticsScreen()
}
