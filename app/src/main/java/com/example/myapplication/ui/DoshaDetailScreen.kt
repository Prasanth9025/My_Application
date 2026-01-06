package com.example.myapplication.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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

// --- PRIVATE COLORS ---
private val DetailWhite = Color(0xFFFFFFFF)
private val DetailBlack = Color(0xFF000000)
private val DetailGray = Color(0xFF757575)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoshaDetailScreen(
    doshaType: String, // "Vata", "Pitta", or "Kapha"
    onBack: () -> Unit,
    viewModel: CheckInViewModel = viewModel() // Inject ViewModel
) {
    // 1. Observe Real Data
    val dashboardData by viewModel.dashboardState.collectAsState()

    // 2. Fetch Data if missing
    LaunchedEffect(Unit) {
        viewModel.fetchDashboard()
    }

    // 3. Extract Data safely
    val current = dashboardData?.current
    val history = dashboardData?.history ?: emptyList()
    val trends = dashboardData?.trends

    // 4. Select Data Dynamically based on 'doshaType'
    // This allows us to use one screen for all 3 Doshas
    val (currentScore, trendChange, graphScores, color) = when (doshaType) {
        "Vata" -> QuadDetailData(
            score = current?.vataScore ?: 0,
            change = trends?.vataChange ?: 0,
            history = history.map { it.vataScore },
            color = Color(0xFF5D8F78) // Greenish
        )
        "Pitta" -> QuadDetailData(
            score = current?.pittaScore ?: 0,
            change = trends?.pittaChange ?: 0,
            history = history.map { it.pittaScore },
            color = Color(0xFFE57373) // Reddish
        )
        "Kapha" -> QuadDetailData(
            score = current?.kaphaScore ?: 0,
            change = trends?.kaphaChange ?: 0,
            history = history.map { it.kaphaScore },
            color = Color(0xFF64B5F6) // Blueish
        )
        else -> QuadDetailData(0, 0, emptyList(), Color.Gray)
    }

    val trendSign = if (trendChange > 0) "+" else ""
    val trendColor = if (trendChange > 0) Color.Red else Color(0xFF4CAF50)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("$doshaType Dosha Trends", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DetailWhite)
            )
        },
        containerColor = DetailWhite
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            // 1. Score Section
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text("$doshaType Score", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = DetailBlack)
                Spacer(Modifier.height(8.dp))

                // Dynamic Score
                Text(
                    text = "$currentScore",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = DetailBlack
                )
                Spacer(Modifier.height(8.dp))

                // Dynamic Trend
                Text(
                    text = "Last 7 Days $trendSign$trendChange%",
                    fontSize = 14.sp,
                    color = trendColor,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(32.dp))
            }

            // 2. Graph Section (Dynamic)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))
                ) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        if (graphScores.isNotEmpty()) {
                            DetailTrendGraph(scores = graphScores, lineColor = color)
                        } else {
                            Text(
                                "No history data available",
                                modifier = Modifier.align(Alignment.Center),
                                color = Color.Gray
                            )
                        }
                    }
                }

                // Days Labels
                Row(
                    Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
                        Text(day, fontSize = 12.sp, color = DetailGray)
                    }
                }

                Spacer(Modifier.height(40.dp))
            }

            // 3. Insights Text Section
            item {
                Text("Insights", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DetailBlack)
                Spacer(Modifier.height(16.dp))

                // Dynamic Insight Text
                val dynamicInsight = if (trendChange > 0) {
                    "Your $doshaType score has increased recently. This could be due to changes in diet or stress. Consider activities that pacify $doshaType."
                } else {
                    "Your $doshaType score is stable or decreasing, which indicates good balance. Maintain your current routine."
                }

                Text(
                    text = dynamicInsight,
                    fontSize = 15.sp,
                    color = Color(0xFF424242),
                    lineHeight = 24.sp
                )
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

// --- DATA HELPER ---
data class QuadDetailData(
    val score: Int,
    val change: Int,
    val history: List<Int>,
    val color: Color
)

// --- DYNAMIC GRAPH COMPONENT ---
@Composable
private fun DetailTrendGraph(scores: List<Int>, lineColor: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (scores.isEmpty()) return@Canvas

        val maxScore = 100f
        val widthPerPoint = size.width / (scores.size - 1).coerceAtLeast(1)
        val height = size.height
        val path = Path()

        scores.forEachIndexed { index, score ->
            // Map score (0-100) to height
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
            color = lineColor,
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}