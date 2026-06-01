package com.minlish.data.dto

data class NotificationPreferenceDto(
    val userId: String = "",
    val dailyReminderEnabled: Boolean = true,
    val dailyReminderTime: String = "19:00",
    val reviewReminderEnabled: Boolean = true,
    val emailEnabled: Boolean = false,
    val pushEnabled: Boolean = true,
    val timezone: String = "UTC",
    val updatedAt: Long = 0L
)

