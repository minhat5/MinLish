package com.minlish.ui.common.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldVocabs(
    label: String,
    placeholder: String,
    value: String = "",
    onValueChange: ((String) -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    var textValue by remember { mutableStateOf(value) }
    val displayedValue = if (onValueChange != null) value else textValue

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        // Nhãn từ heading nhỏ
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF4A6572),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Khối nhập thông tin
        TextField(
            value = displayedValue,
            onValueChange = { newValue ->
                if (onValueChange != null) {
                    onValueChange(newValue)
                } else {
                    textValue = newValue
                }
            },
            placeholder = {
                Text(text = placeholder, color = Color.Gray, fontSize = 14.sp)
            },
            trailingIcon = trailingIcon,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF4F0F9), // Màu xám ánh tím nhạt khi không click
                focusedContainerColor = Color(0xFFEBE4F2), // Đậm hơn một chút khi người dùng click vào
                focusedIndicatorColor = Color.Transparent, // Xóa gạch chân khi focus
                unfocusedIndicatorColor = Color.Transparent, // Xóa gạch chân khi không focus
                disabledIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth() // Giãn đều chiều ngang
        )
    }
}
