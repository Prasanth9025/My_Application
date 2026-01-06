package com.example.myapplication.ui

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.network.PredictionResponse
import com.example.myapplication.viewmodel.ProfileViewModel
import com.example.myapplication.data.UserSession
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    prediction: PredictionResponse?,
    onEditProfile: () -> Unit,
    onSettings: () -> Unit,
    onLogout: () -> Unit,
    onAboutClick: () -> Unit,
    onStreakClick: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val userProfile by viewModel.userProfile.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchProfileData()
    }

    val age = remember(userProfile.dob) {
        calculateAge(userProfile.dob)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Wellness Achievements", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- PROFILE IMAGE (Circle) ---
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFCC80)), // Skin-tone/Orange background
                contentAlignment = Alignment.Center
            ) {
                // You can replace this Icon with an Image() composable if you have the PNG resource
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- NAME ---
            Text(
                text = if (userProfile.name == "Loading...") "Loading..." else userProfile.name.ifEmpty { "User" },
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // --- DETAILS LINE ---
            val detailsText = buildString {
                append("Age: $age")
                append(", Email: ${userProfile.email}")
                if (!userProfile.phone.isNullOrEmpty()) {
                    append(", Phone: ${userProfile.phone}")
                }
            }

            Text(
                text = detailsText,
                fontSize = 14.sp,
                color = Color(0xFF6B9B8A), // Muted Green for details
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- EDIT PROFILE BUTTON ---
            Button(
                onClick = onEditProfile,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEFF5F1)), // Light Greenish Gray
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text("Edit Profile", color = Color.Black, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- MILESTONES ---
            SectionHeader("Key Milestones")
            CardItem(
                icon = Icons.Default.CalendarToday,
                title = "Consecutive Daily Check-in Streak",
                subtitle = "14 days",
                subtitleColor = Color(0xFF6B9B8A),
                onClick = onStreakClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- DOSHA SCORES (Matching the List Style) ---
            SectionHeader("Dosha Scores")

            val vataScore = prediction?.vata ?: 0
            val pittaScore = prediction?.pitta ?: 0
            val kaphaScore = prediction?.kapha ?: 0

            // 1. Vata (Air)
            CardItem(
                icon = Icons.Default.Air, // Or Icons.Default.Cloud if Air not available
                title = "Vata: $vataScore",
                onClick = {}
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 2. Pitta (Fire)
            CardItem(
                icon = Icons.Default.LocalFireDepartment,
                title = "Pitta: $pittaScore",
                onClick = {}
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 3. Kapha (Water)
            CardItem(
                icon = Icons.Default.WaterDrop,
                title = "Kapha: $kaphaScore",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 4. About Doshas
            CardItem(
                icon = Icons.Default.School,
                title = "About Doshas",
                onClick = onAboutClick
            )

            Spacer(modifier = Modifier.height(40.dp))

            // --- LOGOUT BUTTON ---
            Button(
                onClick = {
                    UserSession.clearSession()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF0000)), // Red
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Logout", fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

// --- SHARED COMPONENT FOR LIST ITEMS ---
@Composable
fun CardItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    subtitleColor: Color = Color.Gray,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)), // Light Gray Background
        elevation = CardDefaults.cardElevation(0.dp) // Flat look
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Box
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFEFF5F1)), // Light Green Icon Background
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.Black, // Dark Icon
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Medium, // Medium weight matches screenshot
                    fontSize = 16.sp,
                    color = Color.Black
                )
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = subtitleColor
                    )
                }
            }
        }
    }
}

// --- HELPER FUNCTIONS ---
@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        textAlign = TextAlign.Start
    )
}

fun calculateAge(dobString: String?): String {
    if (dobString.isNullOrEmpty()) return "N/A"
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentYear = LocalDate.now().year
            if (dobString.matches(Regex("^\\d{4}$"))) {
                val birthYear = dobString.toInt()
                return (currentYear - birthYear).toString()
            }
            try {
                val birthDate = LocalDate.parse(dobString)
                val currentDate = LocalDate.now()
                return java.time.Period.between(birthDate, currentDate).years.toString()
            } catch (e: Exception) {
                val yearRegex = Regex("(\\d{4})")
                val match = yearRegex.find(dobString)
                if (match != null) {
                    val birthYear = match.groupValues[1].toInt()
                    return (currentYear - birthYear).toString()
                }
            }
        }
        "N/A"
    } catch (e: Exception) {
        "N/A"
    }
}