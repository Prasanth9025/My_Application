package com.example.myapplication.ui

import androidx.compose.foundation.Image // Import Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale // Import ContentScale
import androidx.compose.ui.res.painterResource // Import painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R // Import R file

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    // State variables
    var name by remember { mutableStateOf("Priya Sharma") }
    var email by remember { mutableStateOf("priya.sharma@example.com") }
    var phone by remember { mutableStateOf("+1-555-123-4567") }
    var gender by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }

    // Colors from design
    val inputBgColor = Color(0xFFEFF5F0) // Light Mint background for inputs
    val buttonColor = Color(0xFF00E676)   // Bright Green
    val handleColor = Color(0xFF6B9B8A)   // Muted Green for @handle

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Profile", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
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
            // 1. Profile Picture (Image instead of Icon)
            Box(
                modifier = Modifier
                    .size(110.dp) // Size matches screenshot
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_pic), // Ensure file exists!
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Name & Handle
            Text("Priya Sharma", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text("@priyasharma", fontSize = 14.sp, color = handleColor)

            Spacer(modifier = Modifier.height(32.dp))

            // 2. Input Fields
            CustomTextField(label = "Name", value = name, onValueChange = { name = it }, bgColor = inputBgColor)
            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(label = "Email", value = email, onValueChange = { email = it }, bgColor = inputBgColor)
            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(label = "Phone", value = phone, onValueChange = { phone = it }, bgColor = inputBgColor)
            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(label = "Gender", value = gender, onValueChange = { gender = it }, bgColor = inputBgColor)
            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(label = "Date of Birth", value = dob, onValueChange = { dob = it }, bgColor = inputBgColor)
            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(label = "Country", value = country, onValueChange = { country = it }, bgColor = inputBgColor)

            Spacer(modifier = Modifier.height(40.dp))

            // 3. Save Button
            Button(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Save Changes", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    bgColor: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)), // Rounded corners
            colors = TextFieldDefaults.colors(
                focusedContainerColor = bgColor,
                unfocusedContainerColor = bgColor,
                disabledContainerColor = bgColor,
                focusedIndicatorColor = Color.Transparent, // No underline
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            singleLine = true
        )
    }
}