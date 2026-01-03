package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

// 1. Your Screen Definitions
sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    object CheckIn : BottomNavItem("checkin", "Check-in", Icons.Default.CheckCircle)
    object Guidance : BottomNavItem("guidance", "Guidance", Icons.Default.SelfImprovement)
    object Insights : BottomNavItem("insights", "Insights", Icons.Default.Analytics)
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.AccountCircle)
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.CheckIn,
        BottomNavItem.Guidance,
        BottomNavItem.Insights,
        BottomNavItem.Profile
    )

    // Observe current route to highlight the correct item
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Container matching your CSS specs
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // "border-top-width: 1px"
        HorizontalDivider(thickness = 1.dp, color = Color(0xFFE0E0E0))

        NavigationBar(
            containerColor = Color.White, // White background
            tonalElevation = 0.dp, // Flat look
            modifier = Modifier
                .height(75.dp) // "height: 75"
                // "padding-top: 8px", "padding-bottom: 12px", "padding-left/right: 16px"
                .padding(top = 8.dp, bottom = 12.dp, start = 16.dp, end = 16.dp)
        ) {
            items.forEach { item ->
                // Check if this item is selected
                val isSelected = currentRoute == item.route

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            fontSize = 10.sp // Small text for 5 items to fit
                        )
                    },
                    selected = isSelected,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                // Pop up to the start destination to avoid stacking
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    // Custom Colors: Black for active, Gray for inactive
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = Color.Black,
                        indicatorColor = Color(0xFFEFF5F0), // Light Mint pill background
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        }
    }
}