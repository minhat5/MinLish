package com.minlish.ui.screen.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.ui.common.component.AuthHeader
import com.minlish.ui.common.component.ButtonAuth
import com.minlish.ui.common.component.TextFieldAuth
import com.minlish.ui.common.viewmodel.AuthViewModel
import com.minlish.ui.common.viewmodel.AuthViewModelFactory
import com.minlish.ui.theme.MinLishTheme

@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit = {},
    viewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory())
) {
    val uiState by viewModel.uiState.collectAsState()
    val emailState = remember { mutableStateOf("") }

    Surface(
        color = Color(0xFFF7EEFE),
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 32.dp, vertical = 56.dp)
            .shadow(6.dp, RoundedCornerShape(16.dp))
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.Start)
            ) {
                IconButton(onClick = onBackToLogin) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back to login")
                }
                Text(
                    text = "Back",
                    color = Color(0xFF22005D),
                    fontWeight = FontWeight.Medium
                )
            }

            AuthHeader("Forgot Password", "Enter your registered email to receive a reset message.")

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TextFieldAuth(
                    value = emailState.value,
                    onValueChange = { emailState.value = it },
                    label = "Registered email",
                    icon = Icons.Filled.Email,
                    contentDescription = "Registered email"
                )

                uiState.errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = Color(0xFFB00020),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
                uiState.successMessage?.let { message ->
                    Text(
                        text = message,
                        color = Color(0xFF0A7A3B),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                ButtonAuth(
                    onClick = {
                        viewModel.resetPassword(emailState.value.trim())
                    },
                    text = if (uiState.isLoading) "Sending..." else "Send reset email",
                    enabled = !uiState.isLoading
                )
            }

            TextButton(onClick = onBackToLogin) {
                Text(
                    text = "Return to login",
                    color = Color(0xFF22005D),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordPreview() {
    MinLishTheme {
        ForgotPasswordScreen()
    }
}
