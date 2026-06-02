package com.minlish.ui.common.component

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.ui.theme.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircularProgressMetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    progress: Float,
    modifier: Modifier = Modifier,
    detail: String? = null,
    backgroundColor: Color = Color(0xFFFFEDC2),
    contentColor: Color = Color(0xFFC9A13C),
    supportingTextColor: Color = colorOnSurfaceVariant
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(18.dp))
            .background(backgroundColor, RoundedCornerShape(18.dp))
            .padding(18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                color = supportingTextColor,
                fontSize = 13.sp,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                color = contentColor,
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold
            )
            if (!detail.isNullOrBlank()) {
                Text(
                    text = detail,
                    color = supportingTextColor,
                    fontSize = 12.sp,
                    lineHeight = 14.sp
                )
            }
        }

        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(56.dp)) {
                val strokeWidth = 8.dp.toPx()
                val clampedProgress = progress.coerceIn(0f, 1f)
                val arcRadius = (size.minDimension - strokeWidth) / 2f
                val center = Offset(size.width / 2f, size.height / 2f)
                val endAngle = (-90f + clampedProgress * 360f) * (PI.toFloat() / 180f)

                drawArc(
                    color = Color.White.copy(alpha = 0.75f),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                drawArc(
                    color = contentColor,
                    startAngle = -90f,
                    sweepAngle = clampedProgress * 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                drawCircle(
                    color = contentColor,
                    radius = 4.dp.toPx(),
                    center = Offset(
                        x = center.x + cos(endAngle) * arcRadius,
                        y = center.y + sin(endAngle) * arcRadius
                    )
                )
            }
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = contentColor,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(18.dp)
            )
        }
    }
}
