package com.example.myapplication.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    viewModel: HistoryViewModel = viewModel()
) {
    // 1. Observe Real Data
    val historyList by viewModel.historyList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // 2. Fetch Data
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
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {

                    // --- GRAPH SECTION ---
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Weekly Vata Overview", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Extract scores for the graph (Reverse to show Oldest -> Newest)
                        // We extract vataScore for the visualization
                        val graphScores = remember(historyList) {
                            historyList.take(7).map { it.vataScore }.reversed()
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))
                        ) {
                            Box(modifier = Modifier.padding(16.dp)) {
                                if (graphScores.isNotEmpty()) {
                                    DynamicHistoryGraph(
                                        scores = graphScores,
                                        color = Color(0xFF5D8F78),
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Text("Not enough data", modifier = Modifier.align(Alignment.Center))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                        Text("Past Logs", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // --- LIST ITEMS ---
                    if (historyList.isEmpty()) {
                        item {
                            Text("No history found yet.", color = Color.Gray, modifier = Modifier.padding(top = 20.dp))
                        }
                    } else {
                        items(historyList) { item ->
                            HistoryListItem(
                                dosha = item.predictedDosha,
                                dateString = item.createdAt,
                                // Pass detailed scores for the card
                                details = "Vata: ${item.vataScore} | Pitta: ${item.pittaScore} | Kapha: ${item.kaphaScore}"
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                    item { Spacer(modifier = Modifier.height(50.dp)) }
                }
            }
        }
    }
}

@Composable
fun HistoryListItem(dosha: String, dateString: String, details: String) {
    val formattedDate = try {
        val input = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val output = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        val date = input.parse(dateString)
        output.format(date ?: "")
    } catch (e: Exception) {
        dateString
    }

    val cardColor = when (dosha) {
        "Vata" -> Color(0xFFE3F2FD)
        "Pitta" -> Color(0xFFFFEBEE)
        "Kapha" -> Color(0xFFE8F5E9)
        else -> Color(0xFFF5F5F5)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Dosha", fontSize = 12.sp, color = Color.Gray)
                Text(text = dosha, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                // Show detailed scores
                Text(text = details, fontSize = 10.sp, color = Color.Gray)
            }
            Text(text = formattedDate, fontSize = 14.sp, color = Color.DarkGray)
        }
    }
}

// --- DYNAMIC GRAPH LOGIC ---
@Composable
fun DynamicHistoryGraph(scores: List<Int>, color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        if (scores.isEmpty()) return@Canvas

        val maxScore = 100f
        val widthPerPoint = size.width / (scores.size - 1).coerceAtLeast(1)
        val height = size.height
        val path = Path()

        scores.forEachIndexed { index, score ->
            // Invert Y axis because Canvas (0,0) is top-left
            val x = index * widthPerPoint
            val y = height - ((score / maxScore) * height)

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                val prevX = (index - 1) * widthPerPoint
                val prevScore = scores[index - 1]
                val prevY = height - ((prevScore / maxScore) * height)

                // Smooth Cubic Bezier Curve
                path.cubicTo((prevX + x) / 2, prevY, (prevX + x) / 2, y, x, y)
            }
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}