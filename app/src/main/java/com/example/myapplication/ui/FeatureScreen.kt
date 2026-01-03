package com.example.myapplication.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

@Composable
fun FeatureScreen(onGetStartedClick: () -> Unit) {
    // 1. Root Container
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FFF8)) // Light Mint Background
            .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 16.dp)
    ) {

        // 2. Scrollable Content Area
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Top App Header ---
            Text(
                text = "AyurPredict",
                fontSize = 22.sp, // INCREASED
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- SECTION 1: FOOD ---
            Text(
                text = "Nourish Your Dosha: Food Plans",
                fontSize = 24.sp, // INCREASED
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            FeatureItem(
                category = "Personalized Diet",
                title = "Ayurvedic Food Guidance",
                desc = "Discover food plans tailored to your dosha for optimal health and balance.",
                categoryColor = Color(0xFF4CAF50),
                descColor = Color(0xFF2E7D32),
                imageRes = R.drawable.personal
            )

            Spacer(modifier = Modifier.height(28.dp))

            // --- SECTION 2: HERBS ---
            Text(
                text = "Ancient Wisdom: Herbal Guidance",
                fontSize = 24.sp, // INCREASED
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            FeatureItem(
                category = "Herbal Remedies",
                title = "Safe Home Dosages",
                desc = "Learn about Ayurvedic herbs and their safe dosages for home use, promoting natural wellness.",
                categoryColor = Color(0xFF4CAF50),
                descColor = Color(0xFF2E7D32),
                imageRes = R.drawable.herbal
            )

            Spacer(modifier = Modifier.height(28.dp))

            // --- SECTION 3: YOGA ---
            Text(
                text = "Inner Harmony: Yoga & Breathwork",
                fontSize = 24.sp, // INCREASED
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            FeatureItem(
                category = "Mind-Body Practices",
                title = "Dosha-Specific Routines",
                desc = "Explore yoga and breathwork routines designed to harmonize your dosha and enhance inner peace.",
                categoryColor = Color(0xFF4CAF50),
                descColor = Color(0xFF2E7D32),
                imageRes = R.drawable.mindbody
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        // 3. Bottom Fixed Section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Pagination Dots
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                FeatureDot(Color.LightGray)
                Spacer(modifier = Modifier.width(8.dp))
                FeatureDot(Color.LightGray)
                Spacer(modifier = Modifier.width(8.dp))
                FeatureDot(Color.Black)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // "Get Started" Button
            Button(
                onClick = { onGetStartedClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp), // Slightly taller button
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Get Started", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.White) // INCREASED
            }
        }
    }
}

@Composable
fun FeatureItem(
    category: String,
    title: String,
    desc: String,
    categoryColor: Color,
    descColor: Color,
    imageRes: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // Text Column
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = category,
                color = categoryColor,
                fontSize = 15.sp, // INCREASED
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = title,
                fontSize = 19.sp, // INCREASED
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = desc,
                fontSize = 15.sp, // INCREASED
                color = descColor,
                lineHeight = 22.sp // Increased line height for readability
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Image
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(130.dp)
                .height(85.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    }
}

@Composable
private fun FeatureDot(color: Color) {
    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(color)
    )
}