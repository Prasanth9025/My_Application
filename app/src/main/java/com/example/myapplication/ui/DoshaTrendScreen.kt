package com.example.myapplication.ui

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.CheckInViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoshaTrendScreen(
    doshaType: String, // "Vata", "Pitta", or "Kapha"
    onBack: () -> Unit,
    viewModel: CheckInViewModel = viewModel()
) {
    // 1. Observe Real Data
    val dashboardData by viewModel.dashboardState.collectAsState()

    // 2. Fetch Data if missing
    LaunchedEffect(Unit) {
        viewModel.fetchDashboard()
    }

    // 3. Extract specific data based on 'doshaType'
    val current = dashboardData?.current
    val history = dashboardData?.history ?: emptyList()
    val trends = dashboardData?.trends

    // Dynamic Data Selection (Using CamelCase)
    val (score, trendChange, graphScores, color) = when (doshaType) {
        "Vata" -> QuadData(
            score = current?.vataScore ?: 0,
            change = trends?.vataChange ?: 0,
            history = history.map { it.vataScore },
            color = Color(0xFF5D8F78) // Greenish
        )
        "Pitta" -> QuadData(
            score = current?.pittaScore ?: 0,
            change = trends?.pittaChange ?: 0,
            history = history.map { it.pittaScore },
            color = Color(0xFFE57373) // Reddish
        )
        "Kapha" -> QuadData(
            score = current?.kaphaScore ?: 0,
            change = trends?.kaphaChange ?: 0,
            history = history.map { it.kaphaScore },
            color = Color(0xFF64B5F6) // Blueish
        )
        else -> QuadData(0, 0, emptyList(), Color.Gray)
    }

    val trendSign = if (trendChange > 0) "+" else ""
    val trendColor = if (trendChange > 0) Color.Red else Color(0xFF4CAF50) // Green if decreasing (good), Red if increasing (aggravated)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("$doshaType Dosha Trends", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Header
            Text("$doshaType Score", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text("$score", fontSize = 48.sp, fontWeight = FontWeight.Bold)

            // Trend Text
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Last 7 Days ", fontSize = 14.sp, color = Color.Gray)
                Text("$trendSign$trendChange%", fontSize = 14.sp, color = trendColor, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- DYNAMIC GRAPH ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    if (graphScores.isNotEmpty()) {
                        TrendGraph(scores = graphScores, lineColor = color)
                    } else {
                        Text("No history data available", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
                    }
                }
            }

            // X-Axis Labels
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
                    Text(day, fontSize = 12.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Insights Section
            Text("Insights", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            val insightText = if (trendChange > 0) {
                "Your $doshaType score has increased recently. This could be due to changes in diet, stress, or environment. Consider activities that pacify $doshaType."
            } else {
                "Your $doshaType score is stable or decreasing, which is a good sign of balance. Keep up your current routine!"
            }

            Text(
                text = insightText,
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp
            )
        }
    }
}

// Helper Data Class for clean code
data class QuadData(
    val score: Int,
    val change: Int,
    val history: List<Int>,
    val color: Color
)

@Composable
fun TrendGraph(scores: List<Int>, lineColor: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (scores.isEmpty()) return@Canvas

        val maxScore = 100f
        val widthPerPoint = size.width / (scores.size - 1).coerceAtLeast(1)
        val height = size.height
        val path = Path()

        scores.forEachIndexed { index, score ->
            val x = index * widthPerPoint
            val y = height - ((score / maxScore) * height)

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                val prevX = (index - 1) * widthPerPoint
                val prevScore = scores[index - 1]
                val prevY = height - ((prevScore / maxScore) * height)

                // Smooth Curve
                path.cubicTo((prevX + x) / 2, prevY, (prevX + x) / 2, y, x, y)
            }
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}