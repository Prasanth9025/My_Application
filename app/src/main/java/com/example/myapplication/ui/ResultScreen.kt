package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.network.PredictionResponse

@Composable
fun ResultScreen(
    prediction: PredictionResponse,
    onGoHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()), // Make scrollable
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // 1. Main Result
        Text("Your Dominant Dosha", fontSize = 18.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = prediction.dosha,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4CAF50)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 2. Score Breakdown Cards
        // We use prediction.vata, prediction.pitta, prediction.kapha here
        ScoreRow("Vata", prediction.vata, Color(0xFF90CAF9))   // Blue
        Spacer(modifier = Modifier.height(12.dp))
        ScoreRow("Pitta", prediction.pitta, Color(0xFFFFCC80)) // Orange
        Spacer(modifier = Modifier.height(12.dp))
        ScoreRow("Kapha", prediction.kapha, Color(0xFFA5D6A7)) // Green

        Spacer(modifier = Modifier.height(48.dp))

        // 3. Recommendations
        Text("Recommendations", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        prediction.recommendations?.forEach { rec ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Text(
                    text = rec,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onGoHome,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
        ) {
            Text("Go to Dashboard")
        }
    }
}

@Composable
fun ScoreRow(label: String, score: Int, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // FIX: Use 'modifier = Modifier.width(...)' instead of just 'width'
        Text(
            text = label,
            modifier = Modifier.width(60.dp),
            fontWeight = FontWeight.Bold
        )

        // Progress Bar
        Box(
            modifier = Modifier
                .weight(1f)
                .height(12.dp)
                .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(score / 100f) // Convert score to percentage
                    .fillMaxHeight()
                    .background(color, RoundedCornerShape(6.dp))
            )
        }

        Spacer(modifier = Modifier.width(12.dp))
        Text(text = "$score%", fontWeight = FontWeight.Bold)
    }
}