package com.minlish.ui.screen.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.ui.common.component.AuthHeader
import com.minlish.ui.common.component.ButtonAuth
import com.minlish.ui.common.component.TextFieldAuth
import com.minlish.ui.common.component.TextFieldAuthPassword
import com.minlish.ui.theme.MinLishTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextAlign
import com.minlish.domain.model.UserProfile
import com.minlish.ui.common.viewmodel.AuthViewModel
import com.minlish.ui.common.viewmodel.AuthViewModelFactory

@Composable
fun LoginScreen(
    onLoginSuccess: (UserProfile) -> Unit = {},
    onNavigateRegister: () -> Unit = {},
    onNavigateForgotPassword: () -> Unit = {},
    viewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory())
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.currentUser?.id) {
        uiState.currentUser?.let(onLoginSuccess)
    }

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
            LoginTextField(
                uiState = uiState,
                onLogin = { email, password -> viewModel.login(email, password) },
                onForgotPassword = onNavigateForgotPassword
            )
            CreateAccount(onNavigateRegister = onNavigateRegister)
        }
    }
}

@Composable
fun LoginTextField(
    uiState: AuthUiState,
    onLogin: (String, String) -> Unit,
    onForgotPassword: () -> Unit
) {
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
        TextButton(
            onClick = onForgotPassword,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = "Forgot password?",
                color = Color(0xFF22005D),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )
        }
        uiState.errorMessage?.let { message ->
            Text(
                text = message,
                color = Color(0xFFB00020),
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )
        }
        uiState.successMessage?.let { message ->
            Text(
                text = message,
                color = Color(0xFF0A7A3B),
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )
        }
        ButtonAuth(
            onClick = {
                val email = emailState.value.trim()
                val password = passwordState.value
                onLogin(email, password)
            },
            text = if (uiState.isLoading) "Logging in..." else "Login"
        )
    }
}

@Composable
fun CreateAccount(onNavigateRegister: () -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "New to MinLish?",
            modifier = Modifier.padding(top = 1.dp)
        )
        TextButton(
            onClick = onNavigateRegister,
        ) {
            Text(
                text = "Create Account",
                color = Color(0xFF22005D),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    MinLishTheme {
        LoginScreen()
    }
}
