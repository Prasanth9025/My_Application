package com.example.myapplication.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- PRIVATE COLORS (Renamed to avoid conflicts with other files) ---
private val InsightsDarkGreen = Color(0xFF2E5E56)
private val InsightsLightBar = Color(0xFFE8F5E9)
private val InsightsActiveBar = Color(0xFFA5D6A7)
private val InsightsBackground = Color(0xFFFFFFFF)
private val InsightsTextGray = Color(0xFF757575)
private val InsightsPositive = Color(0xFF4CAF50)
private val InsightsNegative = Color(0xFFEF5350)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    onBackClick: () -> Unit = {},
    // Unused params kept to prevent MainActivity crash
    onTrendClick: (String) -> Unit = {},
    onHistoryClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Dosha Trends History", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = InsightsBackground)
            )
        },
        containerColor = InsightsBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            // 1. Toggle Switch
            item {
                Spacer(modifier = Modifier.height(16.dp))
                InsightsToggle()
                Spacer(modifier = Modifier.height(24.dp))
            }

            // 2. Dosha Balance Section (Line Chart)
            item {
                Text("Dosha Balance", fontSize = 14.sp, color = InsightsTextGray)
                Spacer(Modifier.height(4.dp))
                Text("Vata 30%, Pitta 40%, Kapha 30%", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(Modifier.height(4.dp))
                Text("Last 7 Days +5%", fontSize = 14.sp, color = InsightsPositive, fontWeight = FontWeight.Medium)

                Spacer(Modifier.height(24.dp))

                // The Wavy Line Chart
                InsightsLineChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )

                // Days Row
                Row(
                    Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
                        Text(day, fontSize = 12.sp, color = InsightsTextGray)
                    }
                }

                Spacer(Modifier.height(32.dp))
                HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))
                Spacer(Modifier.height(24.dp))
            }

            // 3. Factors Section
            item {
                Text("Factors", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(Modifier.height(24.dp))
            }

            // Sleep Chart
            item {
                InsightsFactorCard(
                    title = "Sleep",
                    value = "7.5 hours",
                    trend = "+10%",
                    barValues = listOf(0.7f, 0.75f, 0.6f, 0.8f, 0.7f, 0.5f, 0.75f)
                )
                Spacer(Modifier.height(24.dp))
            }

            // Hydration Chart
            item {
                InsightsFactorCard(
                    title = "Hydration",
                    value = "2.5 liters",
                    trend = "-5%",
                    trendColor = InsightsNegative,
                    barValues = listOf(0.5f, 0.6f, 0.5f, 0.5f, 0.4f, 0.5f, 0.5f)
                )
                Spacer(Modifier.height(24.dp))
            }

            // Stress Chart
            item {
                InsightsFactorCard(
                    title = "Stress",
                    value = "Moderate",
                    trend = "+2%",
                    barValues = listOf(0.3f, 0.4f, 0.3f, 0.3f, 0.2f, 0.3f, 0.3f)
                )
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

// --- RENAMED COMPONENTS TO AVOID CONFLICTS ---

@Composable
private fun InsightsToggle() {
    var selected by remember { mutableStateOf("Daily") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
            .padding(4.dp)
    ) {
        listOf("Daily", "Weekly").forEach { option ->
            val isSelected = selected == option
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) Color.White else Color.Transparent)
                    .clickable { selected = option },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) Color.Black else InsightsTextGray
                )
            }
        }
    }
}

@Composable
private fun InsightsFactorCard(
    title: String,
    value: String,
    trend: String,
    trendColor: Color = InsightsPositive,
    barValues: List<Float>
) {
    Column {
        Text(title, fontSize = 14.sp, color = InsightsTextGray)
        Spacer(Modifier.height(4.dp))
        Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(Modifier.height(4.dp))
        Text("Last 7 Days $trend", fontSize = 14.sp, color = trendColor, fontWeight = FontWeight.Medium)

        Spacer(Modifier.height(16.dp))

        // Bar Chart
        Row(
            modifier = Modifier.fillMaxWidth().height(120.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            barValues.forEachIndexed { index, fillLevel ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(100.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(InsightsLightBar),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(fillLevel)
                                .clip(RoundedCornerShape(4.dp))
                                .background(InsightsActiveBar)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")[index],
                        fontSize = 12.sp,
                        color = InsightsTextGray
                    )
                }
            }
        }
    }
}

@Composable
private fun InsightsLineChart(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val path = Path().apply {
            moveTo(0f, height * 0.7f)
            cubicTo(width * 0.1f, height * 0.3f, width * 0.2f, height * 0.3f, width * 0.3f, height * 0.6f)
            cubicTo(width * 0.4f, height * 0.8f, width * 0.5f, height * 0.5f, width * 0.6f, height * 0.5f)
            cubicTo(width * 0.7f, height * 0.5f, width * 0.75f, height * 0.9f, width * 0.85f, height * 0.3f)
            cubicTo(width * 0.9f, height * 0.1f, width * 0.95f, height * 0.6f, width, height * 0.4f)
        }

        drawPath(
            path = path,
            color = InsightsDarkGreen,
            style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}