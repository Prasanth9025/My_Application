package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyScreen(
    onBack: () -> Unit
) {
    // State for toggles
    var researchEnabled by remember { mutableStateOf(false) }
    var partnerEnabled by remember { mutableStateOf(false) }
    var personalizedContent by remember { mutableStateOf(false) }
    var tailoredRecs by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Privacy Settings", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
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

            // --- SECTION: DATA SHARING ---
            PrivacyHeader("Data Sharing")

            PrivacyToggleItem(
                icon = Icons.Outlined.Science, // Flask icon
                title = "Research Participation",
                subtitle = "Allow sharing of anonymized data for research purposes",
                isChecked = researchEnabled,
                onCheckedChange = { researchEnabled = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PrivacyToggleItem(
                icon = Icons.Outlined.Handshake, // Handshake icon (or People)
                title = "Partner Data Sharing",
                subtitle = "Share your wellness data with trusted partners",
                isChecked = partnerEnabled,
                onCheckedChange = { partnerEnabled = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- SECTION: PERSONALIZATION ---
            PrivacyHeader("Personalization")

            PrivacyToggleItem(
                icon = Icons.Outlined.AutoFixHigh, // Magic wand icon
                title = "Personalized Content",
                subtitle = "Enable personalized content based on your wellness data",
                isChecked = personalizedContent,
                onCheckedChange = { personalizedContent = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PrivacyToggleItem(
                icon = Icons.Outlined.Analytics, // Chart/Board icon
                title = "Tailored Recommendations",
                subtitle = "Receive tailored recommendations based on your patterns",
                isChecked = tailoredRecs,
                onCheckedChange = { tailoredRecs = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- SECTION: DATA RETENTION ---
            PrivacyHeader("Data Retention")

            PrivacyActionItem(
                icon = Icons.Outlined.Schedule, // Clock icon
                title = "Data Retention Period",
                subtitle = "Set how long your data is stored before automatic deletion",
                trailingText = "3 Months",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- SECTION: ACCOUNT ---
            PrivacyHeader("Account")

            PrivacyActionItem(
                icon = Icons.Outlined.Download,
                title = "Request Data Copy",
                subtitle = "Request a copy of your personal data",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            PrivacyActionItem(
                icon = Icons.Outlined.Delete,
                title = "Delete Account",
                subtitle = "Delete your account and all associated data",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// --- HELPER COMPONENTS ---

@Composable
fun PrivacyHeader(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
fun PrivacyToggleItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF1F8F3)), // Light Mint
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF2E3E32), modifier = Modifier.size(24.dp))
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Text
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, fontSize = 12.sp, color = Color(0xFF6B9B8A), lineHeight = 16.sp)
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Switch
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF4CAF50),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFEEEEEE),
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
fun PrivacyActionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    trailingText: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF1F8F3)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF2E3E32), modifier = Modifier.size(24.dp))
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, fontSize = 12.sp, color = Color(0xFF6B9B8A), lineHeight = 16.sp)
        }

        if (trailingText != null) {
            Text(trailingText, fontSize = 14.sp, color = Color.Black)
        } else {
            Icon(
                Icons.AutoMirrored.Outlined.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color.Gray
            )
        }
    }
}