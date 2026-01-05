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
    // 1. Observe User Data
    val userProfile by viewModel.userProfile.collectAsState()

    // 2. Refresh Data on Entry
    LaunchedEffect(Unit) {
        viewModel.fetchProfileData()
    }

    // 3. Smart Age Calculation
    // Recalculates only when DOB changes
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

            // --- PROFILE IMAGE ---
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFCC80)),
                contentAlignment = Alignment.Center
            ) {
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
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // --- DETAILS LINE (Age, Email, Phone) ---
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
                color = Color(0xFF6B9B8A), // Greenish text color
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
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEFF5F1)),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text("Edit Profile", color = Color.Black, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- MILESTONES & STATS ---
            SectionHeader("Key Milestones")

            CardItem(
                icon = Icons.Default.CalendarToday,
                title = "Consecutive Daily Check-in Streak",
                subtitle = "14 days",
                subtitleColor = Color(0xFF6B9B8A),
                onClick = onStreakClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            SectionHeader("Dosha Scores")
            val vataScore = prediction?.vata ?: 60
            val pittaScore = prediction?.pitta ?: 40
            val kaphaScore = prediction?.kapha ?: 50

            CardItem(Icons.Default.Air, "Vata: $vataScore", onClick = {})
            Spacer(modifier = Modifier.height(12.dp))
            CardItem(Icons.Default.LocalFireDepartment, "Pitta: $pittaScore", onClick = {})
            Spacer(modifier = Modifier.height(12.dp))
            CardItem(Icons.Default.WaterDrop, "Kapha: $kaphaScore", onClick = {})
            Spacer(modifier = Modifier.height(12.dp))

            // About Doshas
            CardItem(Icons.Default.Face, "About Doshas", onClick = onAboutClick)

            Spacer(modifier = Modifier.height(40.dp))

            // --- LOGOUT ---
            Button(
                onClick = {
                    UserSession.clearSession()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Logout", fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

// --- ROBUST AGE CALCULATION LOGIC ---
fun calculateAge(dobString: String?): String {
    if (dobString.isNullOrEmpty()) return "N/A"

    return try {
        // We need Android O (API 26+) for java.time, but this check makes it safe
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentYear = LocalDate.now().year

            // 1. Check if input is just a 4-digit Year (e.g. "1995")
            // Regex checks if the string is exactly 4 digits
            if (dobString.matches(Regex("^\\d{4}$"))) {
                val birthYear = dobString.toInt()
                return (currentYear - birthYear).toString()
            }

            // 2. Check if input is a Full Date (e.g. "1995-05-20")
            try {
                val birthDate = LocalDate.parse(dobString) // Expects YYYY-MM-DD
                val currentDate = LocalDate.now()
                return java.time.Period.between(birthDate, currentDate).years.toString()
            } catch (e: Exception) {
                // If parsing fails (maybe format is DD-MM-YYYY?), try to extract just the year
                // This finds the first 4-digit number in the string
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

// ---------------- Helper Components ----------------

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        textAlign = TextAlign.Start
    )
}

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
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFEFF5F1)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.Black, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = subtitle, fontSize = 14.sp, color = subtitleColor)
                }
            }
        }
    }
}