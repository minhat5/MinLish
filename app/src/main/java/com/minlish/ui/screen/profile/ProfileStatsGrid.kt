package com.minlish.ui.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Translate
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minlish.ui.common.component.StatCard

@Composable
fun ProfileStatsGrid(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.LibraryBooks,
                value = "42",
                label = "Decks Mastered"
            )
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.Star,
                value = "128",
                label = "Perfect Scores"
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.Translate,
                value = "2.5k",
                label = "Words Learned"
            )
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.Timer,
                value = "45h",
                label = "Study Time"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileStatsGridPreview() {
    ProfileStatsGrid()
}
