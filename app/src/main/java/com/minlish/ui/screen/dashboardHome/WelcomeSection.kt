package com.minlish.ui.screen.dashboardHome

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.minlish.ui.theme.*


@Composable
fun WelcomeSection(
    userName: String
){
    Column{
        Text(
            text = "Welcome back, $userName",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = colorOnSurface
        )
        Text(
            text = "Ready to crush your daily goals?",
            fontSize = 16.sp,
            color = colorOnSurfaceVariant
        )
    }
}

@Preview
@Composable
fun rv(){
    WelcomeSection("Alex")
}