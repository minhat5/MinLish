package com.minlish.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.minlish.di.AppContainer
import com.minlish.domain.model.UserProfile
import com.minlish.domain.usecase.AnalyticsMetrics
import com.minlish.ui.common.component.MonthlyConsistencyCalendarEntry
import com.minlish.ui.common.component.WeeklyConsistencyChartEntry
import com.minlish.ui.screen.analytics.ProgressSummaryData
import com.minlish.ui.screen.analytics.RetentionLevelData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AnalyticsUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val progressSummaryData: ProgressSummaryData = ProgressSummaryData(),
    val weeklyEntries: List<WeeklyConsistencyChartEntry> = emptyList(),
    val monthlyEntries: List<MonthlyConsistencyCalendarEntry> = emptyList(),
    val firstMonthDayOffset: Int = 0,
    val hasStudiedToday: Boolean = false,
    val retentionLevels: List<RetentionLevelData> = emptyList(),
    val wordsReadyForReview: Int = 0
)

class AnalyticsViewModel(
    private val getCurrentUser: suspend () -> UserProfile?,
    private val getAnalyticsMetrics: suspend (userId: String) -> AnalyticsMetrics
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    init {
        loadAnalytics()
    }

    fun refresh() {
        loadAnalytics()
    }

    private fun loadAnalytics() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val user = getCurrentUser()
                if (user == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "User not found"
                        )
                    }
                    return@launch
                }

                val metrics = getAnalyticsMetrics(user.id)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        progressSummaryData = metrics.toProgressSummaryData(),
                        weeklyEntries = metrics.toWeeklyEntries(),
                        monthlyEntries = metrics.toMonthlyEntries(),
                        firstMonthDayOffset = metrics.firstMonthDayOffset,
                        hasStudiedToday = metrics.hasStudiedToday,
                        retentionLevels = metrics.toRetentionLevelData(),
                        wordsReadyForReview = metrics.wordsReadyForReview
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to load analytics"
                    )
                }
            }
        }
    }

    private fun AnalyticsMetrics.toProgressSummaryData(): ProgressSummaryData {
        val retentionPercent = (retentionRate * 100).toInt()
        return ProgressSummaryData(
            masteredWords = masteredWordsThisWeek.toString(),
            masteredWordsDetail = "mastered this\nweek",
            atRiskWords = atRiskWordsThisWeek.toString(),
            atRiskWordsDetail = "at risk this\nweek",
            retentionRate = "$retentionPercent%",
            retentionDetail = "$weeklyRememberedReviews/$weeklyTotalReviews\nreviews",
            retentionProgress = retentionRate
        )
    }

    private fun AnalyticsMetrics.toWeeklyEntries(): List<WeeklyConsistencyChartEntry> {
        val labels = listOf("M", "T", "W", "T", "F", "S", "S")
        return weeklyStudyDays.map { day ->
            WeeklyConsistencyChartEntry(
                dayLabel = labels.getOrElse(day.dayIndex) { "" },
                hasStudied = day.hasStudied,
                isToday = day.isToday
            )
        }
    }

    private fun AnalyticsMetrics.toMonthlyEntries(): List<MonthlyConsistencyCalendarEntry> {
        return monthlyStudyDays.map { day ->
            MonthlyConsistencyCalendarEntry(
                dayOfMonth = day.dayOfMonth,
                hasStudied = day.hasStudied,
                isToday = day.isToday
            )
        }
    }

    private fun AnalyticsMetrics.toRetentionLevelData(): List<RetentionLevelData> {
        return retentionLevels.map { level ->
            RetentionLevelData(
                label = level.label,
                count = level.count,
                intervalRange = level.intervalRange
            )
        }
    }
}

class AnalyticsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnalyticsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AnalyticsViewModel(
                getCurrentUser = AppContainer.getCurrentUserUseCase::invoke,
                getAnalyticsMetrics = AppContainer.getAnalyticsMetricsUseCase::invoke
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
