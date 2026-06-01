package com.minlish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.minlish.ui.theme.MinLishTheme
import com.minlish.di.AppContainer
import com.minlish.ui.navigation.AppNavHost
import com.minlish.ui.screen.analytics.AnalyticsScreen
import com.minlish.ui.screen.dashboardHome.HomeScreen
import com.minlish.ui.screen.vocabs.AddDeckScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppContainer.initialize(applicationContext)
        enableEdgeToEdge()
        setContent {
            MinLishTheme {
                Surface {
                    AnalyticsScreen ()
//                    AddDeckScreen()
                }
            }
        }
    }
}
