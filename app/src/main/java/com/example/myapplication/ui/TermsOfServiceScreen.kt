package com.example.myapplication.ui

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfServiceScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Terms of Service", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
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
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Intro Text
            Text(
                text = "Welcome to AyushTrack! These Terms of Service (\"Terms\") govern your access to and use of the AyushTrack mobile application (the \"App\") provided by AyushTrack Inc. (\"AyushTrack,\" \"we,\" \"us,\" or \"our\"). By accessing or using the App, you agree to be bound by these Terms. If you do not agree to these Terms, you may not access or use the App.",
                fontSize = 14.sp,
                color = Color(0xFF424242),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- SECTION 1 ---
            TermSection(
                title = "1. Acceptance of Terms",
                body = "By accessing or using the App, you agree to be bound by these Terms and our Privacy Policy, which is incorporated herein by reference. If you are using the App on behalf of an organization, you represent and warrant that you have the authority to bind that organization to these Terms."
            )

            // --- SECTION 2 ---
            TermSection(
                title = "2. Description of Service",
                body = "AyushTrack is a preventive wellness mobile application that predicts internal health imbalance using Ayurvedic principles and AI-based pattern analysis. The App provides personalized health insights and recommendations based on your input and data analysis. The App is not intended to diagnose, treat, cure, or prevent any disease."
            )

            // --- SECTION 3 ---
            TermSection(
                title = "3. User Accounts",
                body = "To use certain features of the App, you may be required to create an account. You are responsible for maintaining the confidentiality of your account credentials and for all activities that occur under your account. You agree to notify us immediately of any unauthorized use of your account."
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun TermSection(title: String, body: String) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = body,
            fontSize = 14.sp,
            color = Color(0xFF424242), // Dark Grey
            lineHeight = 20.sp
        )
    }
}