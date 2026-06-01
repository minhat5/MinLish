package com.minlish.domain.repository

import com.minlish.domain.model.ProgressSnapshot
import com.minlish.domain.model.DailyPlan
import com.minlish.domain.model.Deck

interface DashboardRepository {
    suspend fun getProgressSnapshot(userId: String): ProgressSnapshot?
    suspend fun getDailyPlan(userId: String): DailyPlan?
    suspend fun getCurrentDeck(userId: String): Deck?
    suspend fun getTodayDailyActivity(userId: String): Long? // returns correctAnswers / totalAnswers * 100
}

