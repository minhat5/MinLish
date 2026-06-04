package com.minlish.domain.repository

import com.minlish.domain.model.DailyActivity
import com.minlish.domain.model.ReviewLog
import com.minlish.domain.model.UserProgress

interface AnalyticsRepository {
    suspend fun getDailyActivities(userId: String): List<DailyActivity>
    suspend fun getReviewLogs(userId: String): List<ReviewLog>
    suspend fun getUserProgresses(userId: String): List<UserProgress>
}
