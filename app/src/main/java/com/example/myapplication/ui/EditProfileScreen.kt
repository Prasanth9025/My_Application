package com.example.myapplication.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.viewmodel.EditProfileViewModel
import com.example.myapplication.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    editViewModel: EditProfileViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel() // 1. Inject ProfileViewModel
) {
    // 2. Observe Real Data from Backend
    val userProfile by profileViewModel.userProfile.collectAsState()

    // 3. Local State for Editing
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }

    // 4. Pre-fill fields when data loads
    LaunchedEffect(userProfile) {
        if (userProfile.name != "Loading...") {
            name = userProfile.name
            email = userProfile.email
            phone = userProfile.phone ?: ""
            gender = userProfile.gender ?: ""
            dob = userProfile.dob ?: ""
            country = userProfile.country ?: ""
        }
    }

    // 5. Backend Status Handling
    val updateStatus by editViewModel.updateStatus.collectAsState()
    val isLoading by editViewModel.isLoading.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(updateStatus) {
        if (updateStatus == "Success") {
            Toast.makeText(context, "Profile Updated!", Toast.LENGTH_SHORT).show()
            editViewModel.resetStatus()
            profileViewModel.fetchProfileData() // Refresh the profile data
            onSaveSuccess()
        } else if (updateStatus != null) {
            Toast.makeText(context, updateStatus, Toast.LENGTH_LONG).show()
            editViewModel.resetStatus()
        }
    }

    // Figma Colors
    val inputBgColor = Color(0xFFEFF5F0)
    val buttonColor = Color(0xFF00E676)
    val handleColor = Color(0xFF6B9B8A)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Profile", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- PROFILE HEADER ---
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_pic),
                    contentDescription = "Profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Show "User" if name is empty during loading
            Text(name.ifEmpty { "User" }, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("@${name.replace(" ", "").lowercase()}", fontSize = 14.sp, color = handleColor)

            Spacer(modifier = Modifier.height(32.dp))

            // --- FORM FIELDS ---

            FigmaInput("Name", name, { name = it }, inputBgColor)
            Spacer(modifier = Modifier.height(16.dp))

            // Email is usually read-only, but we display it
            FigmaInput("Email", email, { email=it }, inputBgColor)
            Spacer(modifier = Modifier.height(16.dp))

            FigmaInput("Phone", phone, { phone = it }, inputBgColor)
            Spacer(modifier = Modifier.height(16.dp))

            FigmaInput("Gender", gender, { gender = it }, inputBgColor)
            Spacer(modifier = Modifier.height(16.dp))

            FigmaInput("Date of Birth", dob, { dob = it }, inputBgColor)
            Spacer(modifier = Modifier.height(16.dp))

            FigmaInput("Country", country, { country = it }, inputBgColor)

            Spacer(modifier = Modifier.height(40.dp))

            // --- SAVE BUTTON ---
            Button(
                onClick = {
                    // 6. SEND ALL DATA TO VIEWMODEL
                    editViewModel.updateProfile(
                        name = name,
                        email=email,
                        phone = phone,
                        gender = gender,
                        dob = dob,
                        country = country,
                        password = null // Password not changed here
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(8.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Save Changes", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// --- HELPER COMPONENT ---
@Composable
fun FigmaInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    bgColor: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = bgColor,
                unfocusedContainerColor = bgColor,
                disabledContainerColor = bgColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            singleLine = true
        )
    }
}