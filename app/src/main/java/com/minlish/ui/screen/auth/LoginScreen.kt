package com.minlish.ui.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.R
import com.minlish.ui.common.component.AuthHeader
import com.minlish.ui.common.component.TextFieldAuth
import com.minlish.ui.common.component.TextFieldAuthPassword
import com.minlish.ui.theme.MinLishTheme

@Composable
fun LoginScreen() {
    Surface(
        color = Color(0xFFF7EEFE),
        modifier = Modifier
            .padding(24.dp)
            .shadow(6.dp, RoundedCornerShape(16.dp))
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            AuthHeader("Welcome Back!", "Ready to boost your English today?")
            LoginTextField()
            CreateAccount()
        }
    }
}

@Composable
fun LoginTextField() {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        TextFieldAuth(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = "Email",
            icon = Icons.Filled.Email,
            contentDescription = "Email"
        )
        TextFieldAuthPassword(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = "Password",
            icon = Icons.Filled.Lock,
            contentDescription = "Password",
            isVisible = passwordVisible
        )
        Text(
            text = "Forgot password?",
            color = Color(0xFF22005D),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.End)
                .padding(bottom = 10.dp)
        )
        Button(
            onClick = {},
            modifier = Modifier
                .height(50.dp)
                .padding(bottom = 10.dp)
                .clip(RoundedCornerShape(50.dp))
                .fillMaxWidth(0.8f)
        ) {
            Text(
                text = "Login",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun CreateAccount() {
    Row {
        Text(
            text = "New to MinLish?",
            modifier = Modifier.padding(end = 5.dp)
        )
        Text(
            text = "Create Account",
            color = Color(0xFF22005D),
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    MinLishTheme {
        LoginScreen()
    }
}