package com.minlish.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.minlish.ui.theme.*

@Composable
fun ProgressMetricCard(
    title: String,
    value: String,
    detail: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFF0E7FA),
    contentColor: Color = colorPrimary,
    supportingTextColor: Color = colorOnSurfaceVariant
) {
    Column(
        modifier = modifier
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(18.dp))
            .background(backgroundColor, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = title,
                color = supportingTextColor,
                fontSize = 13.sp,
                lineHeight = 16.sp
            )
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(Color.White.copy(alpha = 0.45f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = contentColor,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value,
                color = contentColor,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 36.sp
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = detail,
                color = supportingTextColor,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}
