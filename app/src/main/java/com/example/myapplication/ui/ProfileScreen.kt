package com.example.myapplication.ui

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.viewmodel.ProfileViewModel
import com.example.myapplication.viewmodel.CheckInViewModel
import com.example.myapplication.data.UserSession
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    checkInViewModel: CheckInViewModel,
    onEditProfile: () -> Unit,
    onSettings: () -> Unit,
    onLogout: () -> Unit,
    onAboutClick: () -> Unit,
    onStreakClick: () -> Unit,
    profileViewModel: ProfileViewModel = viewModel()
) {
    // 1. Observe Profile Data
    val userProfile by profileViewModel.userProfile.collectAsState()
    val dashboardData by checkInViewModel.dashboardState.collectAsState()

    // 2. Observe Selected Image
    val profileImageUri by profileViewModel.profileImageUri.collectAsState()

    // 3. Setup Image Picker
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { profileViewModel.updateProfileImage(it) }
    }

    LaunchedEffect(Unit) {
        profileViewModel.fetchProfileData()
        checkInViewModel.fetchDashboard()
    }

    val age = remember(userProfile.dob) { calculateAge(userProfile.dob) }
    val streak = dashboardData?.streak ?: 0
    val currentScores = dashboardData?.current
    val vataScore = currentScores?.vataScore ?: 0
    val pittaScore = currentScores?.pittaScore ?: 0
    val kaphaScore = currentScores?.kaphaScore ?: 0

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Wellness Achievements", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
                },
                actions = {
                    IconButton(onClick = onSettings) { Icon(Icons.Default.Settings, contentDescription = "Settings") }
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
            // --- PROFILE IMAGE SECTION (UPDATED) ---
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFCC80))
                    .clickable {
                        // Launch gallery on click
                        launcher.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {
                if (profileImageUri != null) {
                    // Show Selected Image
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(profileImageUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Profile Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Show Default Placeholder
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Name
            Text(text = userProfile.name.ifEmpty { "User" }, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            // Details
            Text(
                text = "Age: $age, Email: ${userProfile.email}",
                fontSize = 14.sp, color = Color(0xFF6B9B8A), textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Edit Button
            Button(
                onClick = onEditProfile,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEFF5F1)),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text("Edit Profile", color = Color.Black, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(32.dp))

            // --- REAL STREAK ---
            SectionHeader("Key Milestones")
            CardItem(
                icon = Icons.Default.CalendarToday,
                title = "Consecutive Daily Check-in Streak",
                subtitle = "$streak days",
                subtitleColor = if(streak > 0) Color(0xFF00C853) else Color.Gray,
                onClick = onStreakClick
            )
            Spacer(modifier = Modifier.height(24.dp))

            // --- REAL SCORES ---
            SectionHeader("Dosha Scores")
            CardItem(Icons.Default.Air, "Vata: $vataScore", onClick = {})
            Spacer(modifier = Modifier.height(12.dp))
            CardItem(Icons.Default.LocalFireDepartment, "Pitta: $pittaScore", onClick = {})
            Spacer(modifier = Modifier.height(12.dp))
            CardItem(Icons.Default.WaterDrop, "Kapha: $kaphaScore", onClick = {})
            Spacer(modifier = Modifier.height(12.dp))

            CardItem(Icons.Default.School, "About Doshas", onClick = onAboutClick)
            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { UserSession.clearSession(); onLogout() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF0000)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Logout", fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

// ... Keep existing Helper Functions (CardItem, SectionHeader, calculateAge) ...
@Composable
fun CardItem(icon: ImageVector, title: String, subtitle: String? = null, subtitleColor: Color = Color.Gray, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFEFF5F1)), contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = null, tint = Color.Black, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = Color.Black)
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = subtitle, fontSize = 14.sp, color = subtitleColor)
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))
}

fun calculateAge(dobString: String?): String {
    if (dobString.isNullOrEmpty()) return "N/A"
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val birthDate = LocalDate.parse(dobString)
            java.time.Period.between(birthDate, LocalDate.now()).years.toString()
        } else "N/A"
    } catch (e: Exception) { "N/A" }
}