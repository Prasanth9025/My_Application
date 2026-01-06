package com.example.myapplication.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.viewmodel.CheckInViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsistencyScreen(
    viewModel: CheckInViewModel = viewModel(), // Inject ViewModel
    onBack: () -> Unit
) {
    // 1. Observe Real Data
    val dashboardData by viewModel.dashboardState.collectAsState()

    // 2. Refresh Data
    LaunchedEffect(Unit) {
        viewModel.fetchDashboard()
    }

    // 3. Get Real Streak
    val streak = dashboardData?.streak ?: 0
    val progress = (streak / 10f).coerceIn(0f, 1f) // Example goal: 10 days

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Consistency", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- DYNAMIC HEADER ---
            Text(
                text = "You're on a $streak-day streak!", // <--- REAL VALUE
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Keep up the great work! Consistency is key to achieving your wellness goals.",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- PROGRESS BAR ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Current Streak", fontWeight = FontWeight.SemiBold)
                Text("$streak/10", color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF00C853), // Green
                trackColor = Color(0xFFE8F5E9),
            )

            Spacer(modifier = Modifier.height(40.dp))

            // --- BADGES SECTION ---
            Text(
                text = "Badges",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Dynamic Badges (Unlocked based on streak)
            BadgeItem(
                title = "3-Day Streak",
                description = "You've checked in for 3 days in a row!",
                isUnlocked = streak >= 3,
                imageRes = R.drawable.badge_3 // Make sure you have these images or placeholders
            )
            Spacer(modifier = Modifier.height(16.dp))

            BadgeItem(
                title = "7-Day Streak",
                description = "You've checked in for 7 days in a row!",
                isUnlocked = streak >= 7,
                imageRes = R.drawable.badge_14
            )
            Spacer(modifier = Modifier.height(16.dp))

            BadgeItem(
                title = "14-Day Streak",
                description = "You've checked in for 14 days in a row!",
                isUnlocked = streak >= 14,
                imageRes = R.drawable.badge_14
            )
        }
    }
}

@Composable
fun BadgeItem(
    title: String,
    description: String,
    isUnlocked: Boolean,
    imageRes: Int?
) {
    val opacity = if (isUnlocked) 1f else 0.4f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFAFAFA), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Daily Check-in",
                fontSize = 12.sp,
                color = Color(0xFF4CAF50)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = if(isUnlocked) Color.Black else Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = if(isUnlocked) Color(0xFF66BB6A) else Color.Gray
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Badge Image Placeholder
        if (imageRes != null) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = opacity,
                modifier = Modifier
                    .width(80.dp)
                    .height(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFCCBC)) // Fallback color
            )
        } else {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if(isUnlocked) Color(0xFFFFCC80) else Color.LightGray)
            )
        }
    }
}