package com.example.myapplication.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Import for List items
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
import com.example.myapplication.viewmodel.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendsHistoryScreen(
    onBack: () -> Unit,
    viewModel: HistoryViewModel = viewModel() // Inject ViewModel
) {
    // 1. Observe Real Data
    val historyList by viewModel.historyList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // 2. Fetch Data when screen opens
    LaunchedEffect(Unit) {
        viewModel.fetchHistory()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("History & Trends", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            if (isLoading) {
                // Show Loading Spinner
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Weekly Overview", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Graph Section (Visual Only for now)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))
                        ) {
                            Box(modifier = Modifier.padding(16.dp)) {
                                WaveGraph(
                                    modifier = Modifier.fillMaxSize(),
                                    color = Color(0xFF5D8F78)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                        Text("Past Logs", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // 3. REAL LIST ITEMS
                    if (historyList.isEmpty()) {
                        item {
                            Text(
                                "No history found yet.",
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 20.dp)
                            )
                        }
                    } else {
                        items(historyList) { item ->
                            HistoryListItem(
                                dosha = item.predictedDosha, // Correct (CamelCase)
                                dateString = item.createdAt  // Correct (CamelCase)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    // Add bottom spacing so last item isn't cut off
                    item { Spacer(modifier = Modifier.height(50.dp)) }
                }
            }
        }
    }
}

@Composable
fun HistoryListItem(dosha: String, dateString: String) {
    // Format Date: "2024-05-10 14:00:00" -> "10 May, 2024"
    val formattedDate = try {
        val input = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val output = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        val date = input.parse(dateString)
        output.format(date ?: "")
    } catch (e: Exception) {
        dateString // Fallback if parsing fails
    }

    // Color code based on Dosha
    val cardColor = when (dosha) {
        "Vata" -> Color(0xFFE3F2FD) // Light Blue
        "Pitta" -> Color(0xFFFFEBEE) // Light Red
        "Kapha" -> Color(0xFFE8F5E9) // Light Green
        else -> Color(0xFFF5F5F5)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Dosha", fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = dosha, // Shows "Vata", "Pitta", etc.
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            Text(
                text = formattedDate,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun WaveGraph(modifier: Modifier = Modifier, color: Color) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val path = Path().apply {
            moveTo(0f, height * 0.7f)
            cubicTo(width * 0.2f, height * 0.9f, width * 0.4f, height * 0.4f, width * 0.5f, height * 0.6f)
            cubicTo(width * 0.6f, height * 0.8f, width * 0.8f, height * 0.3f, width, height * 0.5f)
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}