package com.example.myapplication.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

// --- PRIVATE COLORS ---
private val DetailWhite = Color(0xFFFFFFFF)
private val DetailBlack = Color(0xFF000000)
private val DetailGray = Color(0xFF757575)
private val GraphGreen = Color(0xFF5D8F78) // The specific green from your screenshot
private val NegativeRed = Color(0xFFEF5350)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoshaDetailScreen(
    doshaType: String, // "Vata", "Pitta", or "Kapha"
    onBack: () -> Unit
) {
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
                    text = getScore(doshaType),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = DetailBlack
                )
                Spacer(Modifier.height(8.dp))
                Text("Last 30 Days -5%", fontSize = 14.sp, color = NegativeRed, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(32.dp))
            }

            // 2. Graph Section
            item {
                // The Wavy Line Chart
                DetailWavyGraph(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )

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
                Text(
                    text = getInsightsText(doshaType),
                    fontSize = 15.sp,
                    color = Color(0xFF424242),
                    lineHeight = 24.sp
                )
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

// --- COMPONENTS ---

@Composable
private fun DetailWavyGraph(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Custom wavy path to match your design
        val path = Path().apply {
            moveTo(0f, height * 0.7f)
            cubicTo(width * 0.05f, height * 0.5f, width * 0.1f, height * 0.5f, width * 0.15f, height * 0.6f)
            cubicTo(width * 0.2f, height * 0.8f, width * 0.25f, height * 0.6f, width * 0.3f, height * 0.7f)
            cubicTo(width * 0.35f, height * 0.8f, width * 0.4f, height * 0.7f, width * 0.45f, height * 0.65f)
            cubicTo(width * 0.5f, height * 0.6f, width * 0.55f, height * 0.9f, width * 0.6f, height * 0.8f)
            cubicTo(width * 0.65f, height * 0.5f, width * 0.7f, height * 0.3f, width * 0.75f, height * 0.6f)
            cubicTo(width * 0.8f, height * 0.8f, width * 0.85f, height * 0.7f, width * 0.9f, height * 0.4f)
            lineTo(width, height * 0.5f)
        }

        drawPath(
            path = path,
            color = GraphGreen,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

// --- DATA HELPERS ---

private fun getScore(dosha: String): String {
    return when(dosha) {
        "Vata" -> "60"
        "Pitta" -> "40"
        "Kapha" -> "50"
        else -> "0"
    }
}

private fun getInsightsText(dosha: String): String {
    return when(dosha) {
        "Vata" -> "Your Vata score has decreased slightly over the past week. This could be due to changes in your diet or stress levels. Consider reviewing your recent activities and meals to identify potential factors."
        "Pitta" -> "Your Pitta score shows stability but has mild fluctuations. Ensure you are staying cool and avoiding overly spicy foods to maintain this balance."
        "Kapha" -> "Your Kapha score is rising. This suggests a need for more physical activity and lighter, warmer foods to counteract the heaviness."
        else -> "Your score is stable."
    }
}