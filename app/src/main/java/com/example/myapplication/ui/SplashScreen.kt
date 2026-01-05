package com.example.myapplication.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// --- IMPORTS ---
import com.example.myapplication.R
import com.example.myapplication.data.UserSession // Import UserSession

@Composable
fun SplashScreen(
    // CHANGED: Now accepts a String to tell MainActivity where to go
    onSplashFinished: (String) -> Unit
) {
    // This effect runs once when the screen opens
    LaunchedEffect(key1 = true) {
        delay(3000) // 3 seconds delay

        // --- AUTO-LOGIN LOGIC ---
        if (UserSession.isLoggedIn()) {
            // User is already logged in -> Go straight to Home
            onSplashFinished("home")
        } else {
            // No user found -> Go to Welcome (Onboarding)
            onSplashFinished("welcome")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 1. Top Image Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.65f) // Image takes 65% of screen height
        ) {
            Image(
                painter = painterResource(id = R.drawable.ayur_predict_logo),
                contentDescription = "Forest Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // 2. Bottom Text Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.35f) // Text area takes remaining 35%
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "AyurPredict",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "AI-Driven Preventive Wellness",
                color = Color.DarkGray,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}