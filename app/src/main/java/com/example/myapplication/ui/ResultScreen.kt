package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
    onGoHome: () -> Unit = {} // <--- This was missing in your file
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Your Dominant Dosha",
            fontSize = 20.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Large Dosha Name (Purple)
        Text(
            text = prediction.dosha,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6750A4)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Stats Card (Yellowish Green)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF5C6))
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                DoshaStatRow("Vata (Air)", prediction.vata)
                Spacer(modifier = Modifier.height(16.dp))
                DoshaStatRow("Pitta (Fire)", prediction.pitta)
                Spacer(modifier = Modifier.height(16.dp))
                DoshaStatRow("Kapha (Earth)", prediction.kapha)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // "Go to Dashboard" Button
        Button(
            onClick = onGoHome, // <--- Connected here
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Go to Dashboard", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun DoshaStatRow(label: String, percentage: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
        Text("$percentage%", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}