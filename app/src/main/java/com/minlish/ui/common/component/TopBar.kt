package com.minlish.ui.common.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.ui.common.state.StreakState

@Composable
fun TopBar(
    mainTitle: String = "MinLish",
    subTitle: String? = null,
    streakCount: Int = StreakState.streakCount,
    onProfileClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    showCloseButton: Boolean = false,
    onCloseClick: () -> Unit = {},
    bottomPadding: Dp = 16.dp
) {
    Box(
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxWidth()
            .padding(start = 8.dp, end = 12.dp, top = 16.dp, bottom = bottomPadding),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = if (showCloseButton) onCloseClick else onProfileClick,
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = if (showCloseButton) Icons.Default.Close else Icons.Default.AccountCircle,
                contentDescription = if (showCloseButton) "Close" else "Profile",
                tint = Color(0xFF6C63FF),
                modifier = Modifier.size(if (showCloseButton) 22.dp else 34.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 104.dp)
        ) {
            Text(
                text = mainTitle,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF311B92),
                fontSize = 24.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (subTitle != null) {
                Text(
                    text = subTitle,
                    color = Color(0xFFFF7043),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFFF3E0),
                modifier = Modifier.padding(end = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "\uD83D\uDD25 $streakCount",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        maxLines = 1
                    )
                }
            }

            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color(0xFF311B92),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
