package com.minlish.ui.common.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object StreakState {
    val userStreaks = mutableStateMapOf<String, Int>()
    var currentUserId by mutableStateOf<String?>(null)

    var streakCount: Int
        get() = currentUserId?.let { userStreaks[it] } ?: 0
        set(value) {
            currentUserId?.let {
                userStreaks[it] = value
            }
        }
}
