package com.example.myapplication.ui

// --- Added necessary imports for Image support ---
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
// Removed unused icon imports
// import androidx.compose.material.icons.Icons
// import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
// Added ContentScale and painterResource imports
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// --- IMPORTANT: Make sure to import your project's R file ---
import com.example.myapplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Custom colors from your design
    val beigeInputColor = Color(0xFFF3F0E9) // Light beige background for inputs
    val leafGreen = Color(0xFF436B48) // Dark green for button/icon

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 1. TOP IMAGE SECTION (Updated)
        Box(
            modifier = Modifier
                .fillMaxWidth() // Matches width: 412 requirement automatically on standard phones
                .height(218.dp), // Updated height to 218 as requested
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.login),
                contentDescription = "Login header image",
                modifier = Modifier.fillMaxSize(),
                // ContentScale.Crop ensures the image fills the 218dp height without distortion,
                // cropping edges if necessary. Use ContentScale.FillBounds if you want it stretched exactly.
                contentScale = ContentScale.Crop
                // Opacity is 1.0f and angle is 0deg by default, so no extra modifiers needed.
            )
        }

        // 2. FORM SECTION
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome back",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email Input (Beige Style)
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = beigeInputColor,
                    unfocusedContainerColor = beigeInputColor,
                    disabledContainerColor = beigeInputColor,
                    focusedIndicatorColor = Color.Transparent, // Removes underline
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Input (Beige Style)
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = beigeInputColor,
                    unfocusedContainerColor = beigeInputColor,
                    disabledContainerColor = beigeInputColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Forgot Password (Centered Text)
            Text(
                text = "Forgot password?",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable { onForgotPasswordClick() }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button
            Button(
                onClick = onLoginSuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = leafGreen),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Login", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up Link
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Don't have an account? ", color = Color.Gray)
                Text(
                    text = "Sign up",
                    color = leafGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onSignUpClick() }
                )
            }
        }
    }
}