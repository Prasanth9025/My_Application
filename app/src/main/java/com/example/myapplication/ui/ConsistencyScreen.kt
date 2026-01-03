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
import androidx.compose.runtime.Composable
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
import com.example.myapplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsistencyScreen(onBack: () -> Unit) {
    // Colors matching the Figma design
    val bgMintColor = Color(0xFFF4F9F6) // Matches background
    val primaryGreen = Color(0xFF4CAF50) // Matches text highlights
    val progressGreen = Color(0xFF00E676) // Bright green bar

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Consistency",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = bgMintColor
                )
            )
        },
        containerColor = bgMintColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize() // Matches width: 412, height: 917
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 1. Header Section
            Text(
                text = "You're on a 3-day streak!",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Keep up the great work! Consistency is key to achieving your wellness goals.",
                color = Color(0xFF616161),
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 2. Progress Bar Section
            // Uses Arrangement.SpaceBetween to match "justify-content: space-between"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Current Streak",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = "3/10",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Progress Indicator
            LinearProgressIndicator(
                progress = { 0.3f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = progressGreen,
                trackColor = Color(0xFFE0E0E0),
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 3. Badges Header
            Text(
                text = "Badges",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(20.dp))

            // 4. Badges List
            BadgeItem(
                label = "Daily Check-in",
                title = "3-Day Streak",
                description = "You've checked in for 3 days in a row!",
                imageRes = R.drawable.threeday,
                textColor = primaryGreen
            )

            Spacer(modifier = Modifier.height(20.dp))

            BadgeItem(
                label = "Daily Check-in",
                title = "7-Day Streak",
                description = "You've checked in for 7 days in a row!",
                imageRes = R.drawable.sevenday,
                textColor = primaryGreen
            )

            Spacer(modifier = Modifier.height(20.dp))

            BadgeItem(
                label = "Daily Check-in",
                title = "14-Day Streak",
                description = "You've checked in for 14 days in a row!",
                imageRes = R.drawable.fourteenday,
                textColor = primaryGreen
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun BadgeItem(
    label: String,
    title: String,
    description: String,
    imageRes: Int,
    textColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
            // Default Row behavior pushes content, and weight(1f) ensures image goes to end
        ) {
            // Left Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = textColor,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = textColor,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Right Image
            Box(
                modifier = Modifier
                    .width(110.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEFEBE9))
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}