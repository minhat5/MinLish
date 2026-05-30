package com.minlish.ui.common.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldAuth(value: String, onValueChange: (String) -> Unit, label: String, icon: ImageVector, contentDescription: String) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label)
        },
        leadingIcon = {
            Icon(imageVector = icon, contentDescription = contentDescription)
        },
        modifier = Modifier
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(30.dp))
            .fillMaxWidth(0.8f)
    )
}

@Composable
fun TextFieldAuthPassword(value: String, onValueChange: (String) -> Unit, label: String, icon: ImageVector, contentDescription: String, isVisible: MutableState<Boolean>) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label)
        },
        leadingIcon = {
            Icon(imageVector = icon, contentDescription = contentDescription)
        },
        trailingIcon = {
            val image = if (isVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

            val description = if (isVisible.value) "Hide password" else "Show password"

            IconButton(onClick = { isVisible.value = !isVisible.value }) {
                Icon(imageVector = image, contentDescription = description)
            }
        },
        visualTransformation = if (isVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(30.dp))
            .fillMaxWidth(0.8f)
    )
}