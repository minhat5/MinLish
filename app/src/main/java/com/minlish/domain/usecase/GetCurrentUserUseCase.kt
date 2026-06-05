package com.minlish.domain.usecase

import android.util.Log
import com.minlish.domain.model.UserProfile
import com.minlish.domain.repository.AuthRepository
import com.minlish.domain.repository.ProfileRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): UserProfile? {
        val user = authRepository.getCurrentUser() ?: return null

        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val today = dateFormat.format(Date())

            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -1)
            val yesterday = dateFormat.format(cal.time)

            if (user.streak > 0 && user.lastStudyDate != today && user.lastStudyDate != yesterday) {
                // Reset streak locally
                val resetUser = user.copy(streak = 0)

                // Asynchronously reset streak in Firestore (non-blocking)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        profileRepository.resetStreak(user.id)
                    } catch (e: Exception) {
                        Log.e("GetCurrentUserUseCase", "Failed to reset streak in Firestore: ${e.message}", e)
                    }
                }

                return resetUser
            }
        } catch (e: Exception) {
            Log.e("GetCurrentUserUseCase", "Error during streak check: ${e.message}", e)
        }

        return user
    }
}

