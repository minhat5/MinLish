package com.minlish.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    val name = remember { mutableStateOf("") }
    Column {
        Welcome(name.value)
        Body()
    }
}

@Composable
fun Welcome(name: String) {
    Column {
        Text(
            text = "Welcome back, $name!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Ready to crush your daily goals?"
        )
    }
}

@Composable
fun Body() {
    Surface(
        color = Color(0xFFF7EEFE),
        modifier = Modifier
            .padding(24.dp)
            .shadow(6.dp, RoundedCornerShape(16.dp))
    ) {
        Column {
            Text(
                text = "Daily Goal",
                fontWeight = FontWeight.Bold
            )
            Diagram()
        }
    }
}

@Composable
fun Diagram() {

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}