package com.minlish.domain.repository

import com.minlish.domain.model.ProfileStats

interface ProfileRepository {
    suspend fun getProfileStats(userId: String): ProfileStats
}
