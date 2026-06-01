package com.minlish.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.ui.theme.*

@Composable
fun ProfileAvatarCard(
    modifier: Modifier = Modifier,
    name: String = "Đinh Văn Sáng",
    level: String = "Nguyên Anh Hậu Kì",
    xp: String = "9,9999 XP",
    achievementText: String = "Top 10%",
    streakText: String = "7 Day Streak",
    onEditClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(24.dp))
            .background(colorSurface, RoundedCornerShape(24.dp))
            .padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(84.dp)
                    .shadow(elevation = 8.dp, shape = CircleShape)
                    .background(Color(0xFFFFD19B), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = name,
                    tint = Color(0xFF4F378A),
                    modifier = Modifier.size(52.dp)
                )
            }

            IconButton(
                onClick = onEditClick,
                modifier = Modifier
                    .offset(x = 8.dp, y = 8.dp)
                    .size(34.dp)
                    .background(Color(0xFFC7A23A), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit profile",
                    tint = Color(0xFF3B2F00),
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Text(
            text = name,
            color = colorOnSurface,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 18.dp)
        )
        Text(
            text = "$level - $xp",
            color = colorOnSurfaceVariant,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp)
        )

        Row(
            modifier = Modifier.padding(top = 14.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileAchievementChip(
                icon = Icons.Filled.EmojiEvents,
                text = achievementText
            )
            Spacer(modifier = Modifier.width(8.dp))
            ProfileAchievementChip(
                icon = Icons.Filled.LocalFireDepartment,
                text = streakText,
                backgroundColor = colorPrimary,
                contentColor = Color.White
            )
        }
    }
}

@Composable
private fun ProfileAchievementChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    backgroundColor: Color = Color(0xFFEADDFF),
    contentColor: Color = colorPrimary
) {
    Row(
        modifier = Modifier
            .background(backgroundColor, CircleShape)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = contentColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileAvatarCardPreview() {
    ProfileAvatarCard()
}
