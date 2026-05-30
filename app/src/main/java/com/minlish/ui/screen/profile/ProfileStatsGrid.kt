package com.minlish.ui.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Translate
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minlish.ui.common.component.StatCard

data class ProfileStatUiModel(
    val icon: ImageVector,
    val value: String,
    val label: String
)

@Composable
fun ProfileStatsGrid(
    stats: List<ProfileStatUiModel>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        stats.chunked(PROFILE_STATS_COLUMN_COUNT).forEach { rowStats ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rowStats.forEach { stat ->
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = stat.icon,
                        value = stat.value,
                        label = stat.label
                    )
                }

                repeat(PROFILE_STATS_COLUMN_COUNT - rowStats.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

private const val PROFILE_STATS_COLUMN_COUNT = 2

private val previewProfileStats = listOf(
    ProfileStatUiModel(
        icon = Icons.Filled.LibraryBooks,
        value = "42",
        label = "Decks Mastered"
    ),
    ProfileStatUiModel(
        icon = Icons.Filled.Star,
        value = "128",
        label = "Perfect Scores"
    ),
    ProfileStatUiModel(
        icon = Icons.Filled.Translate,
        value = "2.5k",
        label = "Words Learned"
    ),
    ProfileStatUiModel(
        icon = Icons.Filled.Timer,
        value = "45h",
        label = "Study Time"
    )
)

@Preview(showBackground = true)
@Composable
private fun ProfileStatsGridPreview() {
    ProfileStatsGrid(stats = previewProfileStats)
}
