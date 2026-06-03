package com.minlish.domain.repository

import com.minlish.domain.model.ReviewLog
import com.minlish.domain.model.UserProgress

interface AnalyticsRepository {
    suspend fun getReviewLogs(userId: String): List<ReviewLog>
    suspend fun getUserProgresses(userId: String): List<UserProgress>
}
