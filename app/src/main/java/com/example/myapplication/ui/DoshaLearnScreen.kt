package com.example.myapplication.ui

import androidx.compose.foundation.Image // Import Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale // Import ContentScale
import androidx.compose.ui.res.painterResource // Import painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// IMPORTANT: Make sure to import your project's R file
import com.example.myapplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoshaLearnScreen(
    doshaType: String,
    onBack: () -> Unit
) {
    val content = getLearnContent(doshaType)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(doshaType, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Image (Replaced the colored Box)
            Image(
                painter = painterResource(id = content.imageRes),
                contentDescription = "$doshaType Illustration",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Crop // Ensures image fills the area without distortion
            )

            // Content Sections
            Column(modifier = Modifier.padding(24.dp)) {
                LearnSection("Qualities", content.qualities, content.textColor)
                Spacer(modifier = Modifier.height(24.dp))

                LearnSection("Triggers", content.triggers, content.textColor)
                Spacer(modifier = Modifier.height(24.dp))

                LearnSection("Balancing Tips", content.tips, content.textColor)
            }
        }
    }
}

@Composable
fun LearnSection(title: String, body: String, titleColor: Color) {
    Column {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = body,
            fontSize = 16.sp,
            color = titleColor, // Specific color for the text body
            lineHeight = 24.sp
        )
    }
}

// --- DATA ---
data class LearnContent(
    val qualities: String,
    val triggers: String,
    val tips: String,
    val imageRes: Int, // Changed from headerColor to imageRes
    val textColor: Color
)

fun getLearnContent(type: String): LearnContent {
    return when (type) {
        "Vata" -> LearnContent(
            qualities = "Vata is characterized by qualities like dry, light, cold, rough, subtle, mobile, clear, and astringent.These qualities manifest in the body and mind,influencing physical and mental characteristics. ",
            triggers = "Vata can be aggravated by factors such as irregular routines, excessive travel, cold weather, dry foods, and emotional stress. These triggers disrupt the balance of Vata in the body.",
            tips = "To balance Vata, focus on establishing routines, staying warm, consuming nourishing foods, practicing grounding activities like yoga, and managing stress through mindfulness and relaxation techniques.",
            imageRes = R.drawable.vata, // Using vata.png
            textColor = Color(0xFF424242) // Dark Gray
        )
        "Pitta" -> LearnContent(
            qualities = "Pitta is characterized by qualities like hot, sharp, light, oily, liquid, spreading, and sour. These qualities manifest in the body and mind, influencing physical and mental characteristics.",
            triggers = "Pitta can be aggravated by factors such as spicy foods, excessive heat, overexertion, anger, and criticism. These triggers disrupt the balance of Pitta in the body.",
            tips = "To balance Pitta, focus on cooling foods, avoiding excessive heat, practicing relaxation techniques, cultivating patience, and engaging in calming activities like swimming or nature walks.",
            imageRes = R.drawable.pitta, // Using pitta.png
            textColor = Color(0xFF2E7D32) // Green Text
        )
        "Kapha" -> LearnContent(
            qualities = "Kapha is characterized by qualities like heavy, slow, cool, oily, smooth, dense, soft, stable, and sweet. These qualities manifest in the body and mind, influencing physical and mental characteristics.",
            triggers = "Kapha can be aggravated by factors such as overeating, sedentary lifestyle, cold and damp environments, excessive sleep, and emotional stagnation. These triggers disrupt the balance of Kapha in the body.",
            tips = "To balance Kapha, focus on staying active, eating light and warm foods, seeking warmth, engaging in stimulating activities, and promoting emotional expression and movement",
            imageRes = R.drawable.kapha, // Using kapha.png
            textColor = Color(0xFFE65100) // Orange Text
        )
        // Fallback case - providing a safe default image to prevent crashes if type is unknown
        else -> LearnContent("", "", "", R.drawable.vata, Color.Black)
    }
}