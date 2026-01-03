package com.example.myapplication.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoshaTrendScreen(
    doshaType: String,
    onBack: () -> Unit
) {
    // Custom Data based on Dosha Type
    val (score, change, changeColor, description) = when (doshaType) {
        "Vata" -> Quad(60, "-5%", Color(0xFFE57373), "Your Vata score has decreased slightly over the past week. This could be due to changes in your diet or stress levels.")
        "Pitta" -> Quad(40, "-5%", Color(0xFFE57373), "Your Pitta score is stable. Continue your cooling diet and avoid excessive heat to maintain this balance.")
        "Kapha" -> Quad(50, "+2%", Color(0xFF4CAF50), "Your Kapha score has increased slightly. Consider increasing physical activity and lighter foods.")
        else -> Quad(0, "0%", Color.Gray, "")
    }

    // Graph Color (Greenish from screenshot)
    val graphColor = Color(0xFF5D9C77)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("$doshaType Dosha Trends", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
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
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Score Section
            Text("$doshaType Score", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Text("$score", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color.Black)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Last 7 Days ", fontSize = 14.sp, color = Color.Gray)
                Text(change, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = changeColor)
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 2. Wave Graph Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                TrendWaveGraph(color = graphColor)
            }

            // Days labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
                    Text(day, fontSize = 12.sp, color = graphColor)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 3. Insights Section
            Text("Insights", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = description,
                fontSize = 16.sp,
                color = Color.DarkGray,
                lineHeight = 24.sp
            )
        }
    }
}

// Helper Data Class
data class Quad(val score: Int, val change: String, val color: Color, val desc: String)

// Custom Canvas to draw the wave from your screenshot
@Composable
fun TrendWaveGraph(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        val path = Path().apply {
            // Starting point (Mon)
            moveTo(0f, height * 0.7f)

            // Bezier curves to simulate the wave in your image
            cubicTo(width * 0.1f, height * 0.3f, width * 0.2f, height * 0.3f, width * 0.3f, height * 0.6f)
            cubicTo(width * 0.4f, height * 0.9f, width * 0.5f, height * 0.5f, width * 0.6f, height * 0.4f)
            cubicTo(width * 0.7f, height * 0.3f, width * 0.8f, height * 0.8f, width * 0.9f, height * 0.2f)
            lineTo(width, height * 0.3f) // End point (Sun)
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}