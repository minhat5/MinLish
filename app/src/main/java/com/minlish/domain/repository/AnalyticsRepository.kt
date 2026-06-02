package com.minlish.domain.repository

import com.minlish.domain.model.ReviewLog

interface AnalyticsRepository {
    suspend fun getReviewLogs(userId: String): List<ReviewLog>
}
