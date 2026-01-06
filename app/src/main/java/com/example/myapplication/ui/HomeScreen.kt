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
import androidx.compose.runtime.*
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
import com.example.myapplication.R
import com.example.myapplication.viewmodel.CheckInViewModel
import kotlin.math.abs

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
    viewModel: CheckInViewModel,
    onStartCheckIn: () -> Unit,
    onDoshaClick: (String) -> Unit,
    onNotificationClick: () -> Unit,
    onGuidanceClick: () -> Unit
) {
    // 1. Observe Real Data from Backend
    val dashboardData by viewModel.dashboardState.collectAsState()

    // 2. Fetch Data on Launch
    LaunchedEffect(Unit) {
        viewModel.fetchDashboard()
    }

    // 3. Extract Values safely
    val current = dashboardData?.current
    val history = dashboardData?.history ?: emptyList()
    val trends = dashboardData?.trends

    // --- FIX 1: Use CamelCase (vataScore) for calculations ---
    val balanceScore = if (current != null) {
        ((current.vataScore + current.pittaScore + current.kaphaScore) / 3).toString()
    } else "--"

    // --- FIX 2: Use CamelCase for Display ---
    val vataScore = current?.vataScore?.toString() ?: "--"
    val pittaScore = current?.pittaScore?.toString() ?: "--"
    val kaphaScore = current?.kaphaScore?.toString() ?: "--"

    // --- FIX 3: Graph Data (Visualizing Vata History as primary metric) ---
    val graphScores = history.map { it.vataScore }

    // --- FIX 4: Trend Text Logic ---
    val vataChange = trends?.vataChange ?: 0
    val trendSign = if (vataChange > 0) "+" else ""
    val trendText = "Next 7 Days $trendSign$vataChange% (Vata)"
    val trendColor = if (vataChange > 0) Color.Red else HomePrimaryGreen

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
                val userName = "Anya" // Placeholder name
                Text("Good Morning, $userName", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = HomeTextBlack)
                Spacer(Modifier.height(16.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Daily Balance Score", fontSize = 14.sp, color = HomeTextGray)
                    Text(balanceScore, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = HomeTextBlack)
                }
                Spacer(Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFFE0F2F1))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.75f) // Static visual for now
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(3.dp))
                            .background(HomePrimaryGreen)
                    )
                }
                Spacer(Modifier.height(24.dp))
            }

            // 2. Dosha Cards (REAL DATA CONNECTED)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HomeDoshaCard("Vata", vataScore, Modifier.weight(1f)) { onDoshaClick("Vata") }
                    HomeDoshaCard("Pitta", pittaScore, Modifier.weight(1f)) { onDoshaClick("Pitta") }
                }
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    HomeDoshaCard("Kapha", kaphaScore, Modifier.fillMaxWidth()) { onDoshaClick("Kapha") }
                }
                Spacer(Modifier.height(32.dp))
            }

            // 3. Today's Check-in
            item {
                HomeFeatureCard(
                    category = "Today's Check-in",
                    title = "Check-in Now",
                    description = "Track your daily health metrics to keep your Doshas in balance.",
                    buttonText = "Start",
                    imageRes = R.drawable.checkin,
                    onClick = onStartCheckIn
                )
                Spacer(Modifier.height(24.dp))
            }

            // 4. Health Insights
            item {
                HomeFeatureCard(
                    category = "Health Insights",
                    title = null,
                    description = "Focus on mindful eating today. Try to incorporate more leafy greens into your meals.",
                    buttonText = "View All",
                    imageRes = R.drawable.health,
                    onClick = onGuidanceClick
                )
                Spacer(Modifier.height(32.dp))
            }

            // 5. Dosha Balance Graph (REAL DATA GRAPH)
            item {
                Text("Dosha Balance History (Vata)", fontSize = 14.sp, color = HomeTextGray)
                Spacer(Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Status: ", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = HomeTextBlack)
                    Text(if(vataChange > 0) "Aggravating" else "Balancing", fontSize = 14.sp, color = trendColor)
                }
                Text(trendText, fontSize = 14.sp, color = trendColor, fontWeight = FontWeight.Medium)

                Spacer(Modifier.height(24.dp))

                if (graphScores.isNotEmpty()) {
                    HomeLineChart(
                        scores = graphScores,
                        modifier = Modifier.fillMaxWidth().height(120.dp)
                    )
                } else {
                    Box(modifier = Modifier.fillMaxWidth().height(120.dp), contentAlignment = Alignment.Center) {
                        Text("No history data yet", color = Color.Gray, fontSize = 12.sp)
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Day 1", "Day 2", "Day 3", "Day 4", "Day 5", "Day 6", "Today").forEach {
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
private fun HomeLineChart(
    scores: List<Int>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        if (scores.isEmpty()) return@Canvas

        val maxScore = 100f
        val widthPerPoint = size.width / (scores.size - 1).coerceAtLeast(1)
        val height = size.height
        val path = Path()

        scores.forEachIndexed { index, score ->
            // Map Score (0-100) to Height (Bottom to Top)
            val x = index * widthPerPoint
            val y = height - ((score / maxScore) * height)

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                // Smooth Curve
                val prevX = (index - 1) * widthPerPoint
                val prevScore = scores[index - 1]
                val prevY = height - ((prevScore / maxScore) * height)

                val conX1 = (prevX + x) / 2
                val conY1 = prevY
                val conX2 = (prevX + x) / 2
                val conY2 = y

                path.cubicTo(conX1, conY1, conX2, conY2, x, y)
            }
        }

        drawPath(
            path = path,
            color = HomeGraphColor,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}