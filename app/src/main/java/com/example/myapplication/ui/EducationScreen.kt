package com.example.myapplication.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// IMPORT YOUR R FILE
import com.example.myapplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EducationScreen(
    onBack: () -> Unit,
    onLearnClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Education", fontWeight = FontWeight.Bold) },
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
                .fillMaxSize() // Automatically fits width: 412, height: 732
                .background(Color.White) // Opacity: 1
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Ayurveda", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(24.dp))

            // Vata Card - Height: 207dp
            EducationCard(
                title = "Vata",
                description = "Vata governs movement, creativity. When balanced, you feel energized and adaptable. When imbalanced, you may experience anxiety or restlessness.",
                textColor = Color.Black,
                imageRes = R.drawable.ed_vatta,
                onClick = { onLearnClick("Vata") },
                modifier = Modifier.height(207.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Pitta Card - Height: 186dp
            EducationCard(
                title = "Pitta",
                description = "Pitta controls metabolism, digestion, and transformation. Balanced Pitta brings focus and drive. Imbalanced Pitta can lead to irritability or inflammation.",
                textColor = Color(0xFF4CAF50),
                imageRes = R.drawable.ed_pitta,
                onClick = { onLearnClick("Pitta") },
                modifier = Modifier.height(186.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Kapha Card - Height: 207dp
            EducationCard(
                title = "Kapha",
                description = "Kapha provides structure, stability, and lubrication. Balanced Kapha fosters calmness and compassion. Imbalanced Kapha may result in lethargy or congestion.",
                textColor = Color(0xFFD87A54),
                imageRes = R.drawable.ed_kappa,
                onClick = { onLearnClick("Kapha") },
                modifier = Modifier.height(207.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun EducationCard(
    title: String,
    description: String,
    textColor: Color,
    imageRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp) // Internal padding
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text(description, fontSize = 14.sp, color = textColor, lineHeight = 20.sp)
            }
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}