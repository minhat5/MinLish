package com.minlish.ui.common.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.R

@Composable
fun AuthHeader(mainText: String, subText: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 32.dp)
    ) {
        val image = painterResource(R.drawable.logo)
        Image(
            painter = image,
            contentDescription = "App Logo",
            modifier = Modifier
                .height(100.dp)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 24.dp)
        )
        Text(
            text = mainText,
            color = Color(0xFF22005D),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = subText,
            fontSize = 15.sp,
            fontWeight = FontWeight.Light,
        )
    }
}