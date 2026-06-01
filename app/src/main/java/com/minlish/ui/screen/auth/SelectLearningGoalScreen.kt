package com.minlish.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minlish.ui.common.component.AuthHeader
import com.minlish.ui.common.component.ButtonAuth

@Composable
fun SelectLearningGoalScreen(
    onGoalSelected: (String) -> Unit = {},
    onSkip: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val selectedGoal = remember { mutableStateOf<String?>(null) }
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF22005D)
                    )
                }
                Text(
                    text = "Skip",
                    color = Color(0xFF22005D),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onSkip() }
                )
            }
            
            AuthHeader("Learning Goal", "What's your main goal? (Optional)")
            SelectGoal(onGoalSelected = { goal ->
                selectedGoal.value = goal
            })
            ButtonAuth(
                onClick = {
                    selectedGoal.value?.let { onGoalSelected(it) }
                },
                text = "Start Learning",
                enabled = selectedGoal.value != null
            )
        }
    }
}

@Composable
fun SelectGoal(onGoalSelected: (String) -> Unit) {
    val selectedGoal = remember { mutableIntStateOf(-1) }
    val listGoals = listOf("Communication", "Career", "Exam Preparation", "Travel")

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth(0.85f)
    ) {
        listGoals.forEachIndexed { index, goalName ->
            Goal(
                name = goalName,
                isSelected = selectedGoal.intValue == index,
                onGoalSelected = {
                    selectedGoal.intValue = index
                    onGoalSelected(goalName)
                }
            )
        }
    }
}

@Composable
fun Goal(
    name: String,
    isSelected: Boolean,
    onGoalSelected: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFF22005D) else Color.White
    val textColor = if (isSelected) Color.White else Color(0xFF22005D)
    val borderColor = Color(0xFF22005D)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onGoalSelected() }
            .padding(20.dp)
    ) {
        Text(
            text = name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

