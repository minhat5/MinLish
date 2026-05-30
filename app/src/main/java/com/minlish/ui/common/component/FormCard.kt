package com.minlish.ui.common.component
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FormCard(
    title: String,                               // Tiêu đề khối (VD: "Manual Entry" hoặc "Deck Info")
    icon: ImageVector = Icons.Default.Edit,      // Icon tiêu đề (Mặc định là cây bút)
    content: @Composable () -> Unit              // ĐÂY LÀ SLOT API: Nơi chứa các TextField
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        // 1. Phần Tiêu đề (Header)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
//            Icon(icon, contentDescription = null, tint = Color(0xFF311B92), modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, fontWeight = FontWeight.Bold, color = Color(0xFF311B92),fontSize =20.sp)
        }

        // 2. Khối hộp trắng đổ bóng (Card Body)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp), spotColor = Color.LightGray)
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Column {
                // Gọi nội dung được truyền từ bên ngoài vào đây
                content()
            }
        }
    }
}