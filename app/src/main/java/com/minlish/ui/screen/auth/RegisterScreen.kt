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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

@Composable
fun RegisterScreen() {
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
            AuthHeader("Register", "Create an account to get started!")
            RegisterTextField()
            BackToLogin()
        }
    }
}

@Composable
fun RegisterTextField() {
    val nameState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val confirmPasswordState = remember { mutableStateOf("") }
    val confirmPasswordVisible = remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextFieldAuth(
            value = nameState.value,
            onValueChange = { nameState.value = it },
            label = "Name",
            icon = Icons.Filled.Person,
            contentDescription = "Name"
        )
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
        TextFieldAuthPassword(
            value = confirmPasswordState.value,
            onValueChange = { confirmPasswordState.value = it },
            label = "Confirm Password",
            icon = Icons.Filled.Lock,
            contentDescription = "Confirm Password",
            isVisible = confirmPasswordVisible
        )
        ButtonAuth({}, "Register")
    }
}

@Composable
fun BackToLogin() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Already have an account?",
        )
        TextButton(
            onClick = {}
        ) {
            Text(
                text = "Login",
                color = Color(0xFF22005D),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    RegisterScreen()
}
