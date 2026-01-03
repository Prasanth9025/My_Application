package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
fun SettingsScreen(
    onBack: () -> Unit,
    onSubscriptionClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onHelpClick: () -> Unit,
    onTermsClick: () -> Unit // <--- NEW PARAMETER
) {
    var notificationsEnabled by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
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
            SettingsHeader("Account")
            SettingsItem(Icons.Outlined.Star, "Subscription", "Manage your subscription", onSubscriptionClick)

            Spacer(modifier = Modifier.height(24.dp))

            SettingsHeader("Preferences")
            SettingsItem(
                Icons.Outlined.Notifications, "Notifications", "Customize your notification settings",
                { notificationsEnabled = !notificationsEnabled },
                {
                    Switch(
                        checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF4CAF50),
                            uncheckedThumbColor = Color.White, uncheckedTrackColor = Color(0xFFEEEEEE),
                            uncheckedBorderColor = Color.Transparent
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            SettingsItem(Icons.Default.Language, "Language", "Choose your preferred language", onLanguageClick)
            Spacer(modifier = Modifier.height(16.dp))
            SettingsItem(Icons.Outlined.Security, "Privacy", "Adjust your privacy settings", onPrivacyClick)

            Spacer(modifier = Modifier.height(24.dp))
            SettingsHeader("Support")
            SettingsItem(Icons.Outlined.HelpOutline, "Help Center", "Get help and support", onHelpClick)
            Spacer(modifier = Modifier.height(16.dp))

            // Connect Terms of Service here
            SettingsItem(
                Icons.Outlined.Description,
                "Terms of Service",
                "Read our terms of service",
                onTermsClick // <--- CONNECTED
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// --- HELPER COMPONENTS ---
@Composable
fun SettingsHeader(text: String) {
    Text(text, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(bottom = 16.dp))
}

@Composable
fun SettingsItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit, trailingContent: (@Composable () -> Unit)? = null) {
    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFE8F5E9)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = Color(0xFF2E3E32), modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
            Text(subtitle, fontSize = 13.sp, color = Color(0xFF66BB6A))
        }
        if (trailingContent != null) trailingContent()
    }
}