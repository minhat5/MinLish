package com.minlish.ui.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.minlish.R

@Composable
fun LoginScreen() {
    Column {
        LoginHeader()
    }
}

@Composable
fun LoginHeader() {
    Column(modifier = Modifier) {
        val image = painterResource(R.drawable.logo)
        Image(
            painter = image,
            contentDescription = "App Logo"
        )
        Text(text = "Welcome Back!")
        Text(text = "Ready to boost your English today?")
    }
}