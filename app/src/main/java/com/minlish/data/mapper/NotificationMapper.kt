package com.minlish.data.mapper

import com.minlish.data.dto.NotificationPreferenceDto
import com.minlish.domain.model.NotificationPreference

fun NotificationPreferenceDto.toDomain(): NotificationPreference {
    return NotificationPreference(
        userId = userId,
        dailyReminderEnabled = dailyReminderEnabled,
        dailyReminderTime = dailyReminderTime,
        reviewReminderEnabled = reviewReminderEnabled,
        emailEnabled = emailEnabled,
        pushEnabled = pushEnabled,
        timezone = timezone,
        updatedAt = updatedAt
    )
}

fun NotificationPreference.toDto(): NotificationPreferenceDto {
    return NotificationPreferenceDto(
        userId = userId,
        dailyReminderEnabled = dailyReminderEnabled,
        dailyReminderTime = dailyReminderTime,
        reviewReminderEnabled = reviewReminderEnabled,
        emailEnabled = emailEnabled,
        pushEnabled = pushEnabled,
        timezone = timezone,
        updatedAt = updatedAt
    )
}

