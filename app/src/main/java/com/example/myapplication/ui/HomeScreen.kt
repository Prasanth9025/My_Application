package com.example.myapplication.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// IMPORT YOUR R FILE
import com.example.myapplication.R

// --- COLORS ---
private val HomeBg = Color(0xFFFFFFFF)
private val HomeTextBlack = Color(0xFF1B1B1B)
private val HomeTextGray = Color(0xFF757575)
private val HomePrimaryGreen = Color(0xFF00C853)
private val HomeLightGreenBg = Color(0xFFE8F5E9)
private val HomeCardBorder = Color(0xFFEEEEEE)
private val HomeGraphColor = Color(0xFF4DB6AC)

@Composable
fun HomeScreen(
    onStartCheckIn: () -> Unit,
    onDoshaClick: (String) -> Unit,
    onNotificationClick: () -> Unit,
    onGuidanceClick: () -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "AyurPredict",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = HomeTextBlack
                )
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = HomeTextBlack,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onNotificationClick() }
                )
            }
        },
        containerColor = HomeBg
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            // 1. Greeting & Score
            item {
                Text("Good Morning, Anya", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = HomeTextBlack)
                Spacer(Modifier.height(16.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Daily Balance Score", fontSize = 14.sp, color = HomeTextGray)
                    Text("75", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = HomeTextBlack)
                }
                Spacer(Modifier.height(8.dp))

                // Progress Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFFE0F2F1))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(3.dp))
                            .background(HomePrimaryGreen)
                    )
                }
                Spacer(Modifier.height(24.dp))
            }

            // 2. Dosha Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HomeDoshaCard("Vata", "60", Modifier.weight(1f)) { onDoshaClick("Vata") }
                    HomeDoshaCard("Pitta", "40", Modifier.weight(1f)) { onDoshaClick("Pitta") }
                }
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    HomeDoshaCard("Kapha", "50", Modifier.fillMaxWidth()) { onDoshaClick("Kapha") }
                }
                Spacer(Modifier.height(32.dp))
            }

            // 3. Today's Check-in (Using 'checkin' image)
            item {
                HomeFeatureCard(
                    category = "Today's Check-in",
                    title = "Completed",
                    description = "Your daily check-in is complete.\nKeep up the good work!",
                    buttonText = "View Details",
                    imageRes = R.drawable.checkin,
                    onClick = onStartCheckIn
                )
                Spacer(Modifier.height(24.dp))
            }

            // 4. Health Insights (UPDATED: Using 'health' image)
            item {
                HomeFeatureCard(
                    category = "Health Insights",
                    title = null,
                    description = "Focus on mindful eating today. Try to incorporate more leafy greens into your meals.",
                    buttonText = "View All",
                    // --- CHANGED THIS LINE TO USE 'R.drawable.health' ---
                    imageRes = R.drawable.health,
                    onClick = onGuidanceClick
                )
                Spacer(Modifier.height(32.dp))
            }

            // 5. Dosha Balance Graph
            item {
                Text("Dosha Balance Over Time", fontSize = 14.sp, color = HomeTextGray)
                Spacer(Modifier.height(4.dp))
                Text("Balanced", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = HomeTextBlack)
                Text("Next 7 Days +5%", fontSize = 14.sp, color = HomePrimaryGreen, fontWeight = FontWeight.Medium)

                Spacer(Modifier.height(24.dp))
                HomeLineChart(modifier = Modifier.fillMaxWidth().height(120.dp))
                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Day 1", "Day 2", "Day 3", "Day 4", "Day 5", "Day 6", "Day 7").forEach {
                        Text(it, fontSize = 10.sp, color = HomeTextGray)
                    }
                }
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

// --- COMPONENTS ---

@Composable
fun HomeFeatureCard(
    category: String,
    title: String?,
    description: String,
    buttonText: String,
    imageRes: Int?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        // Left Column (Text)
        Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
            Text(category, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = HomeTextBlack)
            Spacer(Modifier.height(4.dp))
            if (title != null) {
                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = HomeTextBlack)
                Spacer(Modifier.height(4.dp))
            }
            Text(description, fontSize = 13.sp, color = Color(0xFF66BB6A), lineHeight = 18.sp)
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = HomeLightGreenBg),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(buttonText, color = Color(0xFF2E7D32), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Right Column (Image)
        if (imageRes != null) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(120.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        } else {
            // Fallback placeholder
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFCCBC))
            )
        }
    }
}

@Composable
private fun HomeDoshaCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, HomeCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(title, fontSize = 14.sp, color = HomeTextGray)
            Spacer(Modifier.height(8.dp))
            Text(value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = HomeTextBlack)
        }
    }
}

@Composable
private fun HomeLineChart(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val path = Path().apply {
            moveTo(0f, height * 0.8f)
            cubicTo(width * 0.1f, height * 0.4f, width * 0.2f, height * 0.5f, width * 0.3f, height * 0.6f)
            cubicTo(width * 0.4f, height * 0.7f, width * 0.5f, height * 0.4f, width * 0.6f, height * 0.6f)
            cubicTo(width * 0.7f, height * 0.8f, width * 0.8f, height * 0.2f, width * 0.9f, height * 0.5f)
            lineTo(width, height * 0.4f)
        }
        drawPath(
            path = path,
            color = HomeGraphColor,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}