package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.viewmodel.CheckInViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInSummaryScreen(
    viewModel: CheckInViewModel,
    onBack: () -> Unit,
    onConfirm: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Daily Check-in", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Summary for Today", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Color(0xFFF5F5F5))

            SummaryRow("Sleep Duration", "${state.sleepDuration.toInt()} hours")
            SummaryRow("Sleep Quality", formatEnum(state.sleepQuality.name))
            SummaryRow("Stress Level", formatEnum(state.stressLevel.name))
            SummaryRow("Morning Energy", formatEnum(state.morningEnergy.name))
            SummaryRow("Evening Energy", formatEnum(state.eveningEnergy.name))

            val symptomsText = if (state.symptoms.isNotEmpty()) state.symptoms.joinToString(", ") else "None"
            SummaryRow("Body Sensation", symptomsText)

            SummaryRow("Bowel Movement", formatEnum(state.bowelMovement.name))
            SummaryRow("Hydration", "${state.hydration} Liters")
            SummaryRow("Digestion", formatEnum(state.digestion.name))
            SummaryRow("Appetite", formatEnum(state.appetite.name))
            SummaryRow("Mood", state.mood)
            SummaryRow("Activity", state.physicalActivity)

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676)),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            ) {
                Text("View Daily Forecast", color = Color.Black, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, color = Color(0xFF81C784), fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Text(value, color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Normal, modifier = Modifier.widthIn(max = 180.dp), textAlign = TextAlign.End)
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color(0xFFF5F5F5))
    }
}

fun formatEnum(name: String): String {
    return name.lowercase(Locale.ROOT).replace("_", " ").replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
    }
}