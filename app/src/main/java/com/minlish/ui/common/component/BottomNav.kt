package com.minlish.ui.common.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val bottomNavItems = listOf("Home", "Decks", "Analytics", "Profile")
val bottomNavIcons = listOf(Icons.Default.Home, Icons.Default.List, Icons.Default.DateRange, Icons.Default.Person)

@Composable
fun BottomNav(
    selectedTab: String,               // Tab nào đang được chọn (để làm sáng icon)
    onTabClick: (String) -> Unit       // Logic xử lý khi click (tạm thời để đổi state demo)
) {
    NavigationBar(
        containerColor = Color.White
    ) {
        bottomNavItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(bottomNavIcons[index], contentDescription = item) },
                label = { Text(item) },
                selected = selectedTab == item, // Nếu trùng với selectedTab truyền vào thì sẽ sáng lên
                onClick = { onTabClick(item) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF311B92),
                    selectedTextColor = Color(0xFF311B92),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color(0xFFEDE7F6)
                )
            )
        }
    }
}