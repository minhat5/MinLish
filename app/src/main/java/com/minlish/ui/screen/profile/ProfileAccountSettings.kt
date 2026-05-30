package com.minlish.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.ui.common.component.AccountSettingRow
import com.minlish.ui.screen.dailyReviewSummary.colorOnSurface
import com.minlish.ui.screen.dailyReviewSummary.colorSurface

@Composable
fun ProfileAccountSettings(
    modifier: Modifier = Modifier,
    darkModeEnabled: Boolean = false,
    onDarkModeChange: (Boolean) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(24.dp))
            .background(colorSurface, RoundedCornerShape(24.dp))
            .padding(top = 20.dp, bottom = 8.dp)
    ) {
        Text(
            text = "Account Settings",
            color = colorOnSurface,
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 18.dp)
        )

        AccountSettingRow(
            icon = Icons.Filled.Notifications,
            title = "Notifications",
            subtitle = "Reminders, updates, and digests",
            showArrow = true
        )
        ProfileSettingDivider()

        AccountSettingRow(
            icon = Icons.Filled.DarkMode,
            title = "Dark Mode",
            subtitle = "Adjust app appearance",
            checked = darkModeEnabled,
            onCheckedChange = onDarkModeChange
        )
        ProfileSettingDivider()

        AccountSettingRow(
            icon = Icons.Filled.Language,
            title = "Native Language",
            subtitle = "Currently: Spanish",
            showArrow = true
        )
        ProfileSettingDivider()

        AccountSettingRow(
            icon = Icons.Filled.Logout,
            title = "Log Out",
            titleColor = Color(0xFFBA1A1A),
            iconColor = Color(0xFFBA1A1A),
            iconBackgroundColor = Color(0xFFFFDAD6)
        )
    }
}

@Composable
private fun ProfileSettingDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 76.dp),
        thickness = 1.dp,
        color = Color(0xFFE7E0EC)
    )
}

@Preview(showBackground = true)
@Composable
private fun ProfileAccountSettingsPreview() {
    ProfileAccountSettings()
}
