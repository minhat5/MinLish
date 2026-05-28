package com.minlish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.minlish.ui.screen.auth.LoginScreen
import com.minlish.ui.screen.dailyReviewSummary.DailyReviewSummary
import com.minlish.ui.theme.MinLishTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MinLishTheme {
                Surface {
                    LoginScreen()
                }
            }
        }
    }
}
