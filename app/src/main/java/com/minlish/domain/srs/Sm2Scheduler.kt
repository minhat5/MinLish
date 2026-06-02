package com.minlish.domain.srs

import kotlin.math.max
import kotlin.math.roundToInt

data class Sm2Result(
    val easeFactor: Double,
    val repetition: Int,
    val interval: Int,
    val nextReviewDate: Long
)

object Sm2Scheduler {
    private const val MIN_EASE_FACTOR = 1.3
    private const val DAY_MILLIS = 24 * 60 * 60 * 1000L

    fun calculate(
        quality: Int,
        oldEaseFactor: Double,
        oldRepetition: Int,
        oldInterval: Int,
        now: Long = System.currentTimeMillis()
    ): Sm2Result {
        if (quality < 3) {
            return Sm2Result(
                easeFactor = oldEaseFactor.coerceAtLeast(MIN_EASE_FACTOR),
                repetition = 0,
                interval = 0,
                nextReviewDate = now
            )
        }

        val newEaseFactor = calculateEaseFactor(oldEaseFactor, quality)
        val newRepetition = oldRepetition + 1
        val newInterval = when (newRepetition) {
            1 -> 1
            2 -> 6
            else -> max(1, (oldInterval * newEaseFactor).roundToInt())
        }

        return Sm2Result(
            easeFactor = newEaseFactor,
            repetition = newRepetition,
            interval = newInterval,
            nextReviewDate = now + newInterval * DAY_MILLIS
        )
    }

    private fun calculateEaseFactor(oldEaseFactor: Double, quality: Int): Double {
        val qualityGap = 5 - quality
        val newEaseFactor = oldEaseFactor + (0.1 - qualityGap * (0.08 + qualityGap * 0.02))
        return newEaseFactor.coerceAtLeast(MIN_EASE_FACTOR)
    }
}
