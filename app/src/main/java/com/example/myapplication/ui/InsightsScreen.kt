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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.InsightsViewModel // Import your new ViewModel

// --- PRIVATE COLORS ---
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
    onTrendClick: (String) -> Unit = {},
    onHistoryClick: () -> Unit = {},
    viewModel: InsightsViewModel = viewModel() // 1. Inject ViewModel
) {
    // 2. Observe Real Data
    val data by viewModel.insightsData.collectAsState()

    // 3. Fetch Data on Launch
    LaunchedEffect(Unit) {
        viewModel.fetchInsights()
    }

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

            // 2. Dosha Balance Section (Visual only for now, can be updated later)
            // ... inside LazyColumn ...

            // 2. Dosha Balance Section
            item {
                Text("Dosha Balance", fontSize = 14.sp, color = InsightsTextGray)
                Spacer(Modifier.height(4.dp))

                // UPDATED: Use Real Data
                Text(
                    text = data.doshaBalanceText, // <--- CHANGED
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(Modifier.height(4.dp))

                // UPDATED: Use Real Trend
                Text(
                    text = data.doshaTrendText, // <--- CHANGED
                    fontSize = 14.sp,
                    color = InsightsPositive,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(24.dp))

                InsightsLineChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )

                // ... keep the rest ...
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

            // Sleep Chart (REAL DATA)
            item {
                InsightsFactorCard(
                    title = "Sleep",
                    value = data.avgSleep, // From ViewModel
                    trend = "Avg",
                    barValues = data.sleepChart // From ViewModel
                )
                Spacer(Modifier.height(24.dp))
            }

            // Hydration Chart (REAL DATA)
            item {
                InsightsFactorCard(
                    title = "Hydration",
                    value = data.avgHydration, // From ViewModel
                    trend = "Avg",
                    trendColor = InsightsNegative,
                    barValues = data.hydrationChart // From ViewModel
                )
                Spacer(Modifier.height(24.dp))
            }

            // Stress Chart (REAL DATA)
            item {
                InsightsFactorCard(
                    title = "Stress",
                    value = data.avgStress, // From ViewModel
                    trend = "Avg",
                    barValues = data.stressChart // From ViewModel
                )
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

// --- COMPONENTS ---

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
            // Fill with empty bars if list is short to maintain layout
            val displayValues = if (barValues.isEmpty()) List(7) { 0.1f } else barValues

            displayValues.take(7).forEachIndexed { index, fillLevel ->
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
                                .fillMaxHeight(fillLevel.coerceIn(0.1f, 1f)) // Ensure minimum height visibility
                                .clip(RoundedCornerShape(4.dp))
                                .background(InsightsActiveBar)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    // Simple day labels
                    val dayLabel = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").getOrElse(index) { "" }
                    Text(
                        text = dayLabel,
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