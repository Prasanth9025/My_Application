package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- COLORS ---
private val NotifBg = Color(0xFFFFFFFF)
private val IconBg = Color(0xFFF1F8E9) // Very light green background for icons
private val IconTint = Color(0xFF2E7D32) // Dark green tint for icons
private val TextBlack = Color(0xFF000000)
private val TextGreen = Color(0xFF2E7D32) // Green for time/dates

// Enum to define notification types
enum class NotifType { CHECK_IN, GUIDANCE, PROGRESS, UPDATE }

// Data Class
data class NotifItem(
    val type: NotifType,
    val title: String,
    val time: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(onBack: () -> Unit) {
    // Sample Data matching your Figma Screenshot
    val notifications = listOf(
        NotifItem(NotifType.CHECK_IN, "Daily Check-in Reminder", "Today, 9:00 AM"),
        NotifItem(NotifType.GUIDANCE, "New Personalized Guidance", "Yesterday, 6:00 PM"),
        NotifItem(NotifType.PROGRESS, "Progress Milestone Reached", "July 22, 10:00 AM"),
        NotifItem(NotifType.UPDATE, "App Update Available", "July 20, 2:00 PM")
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Notifications", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = NotifBg)
            )
        },
        containerColor = NotifBg
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            items(notifications) { item ->
                NotificationRow(item)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun NotificationRow(item: NotifItem) {
    val icon = getIconForType(item.type)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Icon Box (Square with Rounded Corners)
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(IconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = IconTint, // Dark Green
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 2. Text Column
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = TextBlack
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.time,
                fontSize = 14.sp,
                color = TextGreen // Green text for time
            )
        }
    }
}

// Helper to get Icon
fun getIconForType(type: NotifType): ImageVector {
    return when (type) {
        NotifType.CHECK_IN -> Icons.Default.CalendarToday
        NotifType.GUIDANCE -> Icons.Default.Explore // Compass icon
        NotifType.PROGRESS -> Icons.Default.InsertChart // Chart icon
        NotifType.UPDATE -> Icons.Default.Notifications // Bell icon
    }
}