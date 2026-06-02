package com.minlish.ui.common.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.minlish.ui.common.state.StreakState

@Composable
fun TopBar(
    mainTitle: String = "MinLish",        // Tiêu đề chính, mặc định là MinLish
    subTitle: String? = null,             // Tiêu đề phụ dưới dạng tùy chọn (nullable)
    streakCount: Int = StreakState.streakCount, // Số lượng streak từ state
    onProfileClick: () -> Unit = {},      // Sự kiện khi bấm vào Profile
    onSettingsClick: () -> Unit = {}      // Sự kiện khi bấm vào Cài đặt
) {
    Box(
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        // 1. Icon Profile (Căn trái)
        IconButton(
            onClick = onProfileClick,
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.CenterStart)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "Profile", 
                    tint = Color(0xFF6C63FF),
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        // 2. Cụm Tiêu đề
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = mainTitle,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF311B92),
                fontSize = 30.sp
            )
            if (subTitle != null) {
                Text(
                    text = subTitle,
                    color = Color(0xFFFF7043),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // 3. Cụm Streak & Settings (Căn phải)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            // Khối hiển thị Streak
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFFF3E0),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "🔥 $streakCount", 
                        fontWeight = FontWeight.Bold, 
                        fontSize = 14.sp
                    )
                }
            }

            // Nút mở cài đặt
            IconButton(
                onClick = onSettingsClick, 
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.Settings, 
                    contentDescription = "Settings", 
                    tint = Color(0xFF311B92),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}