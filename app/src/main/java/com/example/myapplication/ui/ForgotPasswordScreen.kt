package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.AuthStatus
import com.example.myapplication.viewmodel.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    onSendCodeClick: () -> Unit,
    onBackClick: () -> Unit,
    authViewModel: AuthViewModel = viewModel() // Inject ViewModel
) {
    var email by remember { mutableStateOf("") }

    // 1. Observe Authentication State
    val forgotState by authViewModel.forgotPasswordState.collectAsState()
    val isLoading = forgotState is AuthStatus.Loading

    // 2. Handle Success (Navigate to next screen)
    LaunchedEffect(forgotState) {
        if (forgotState is AuthStatus.Success) {
            authViewModel.resetForgotState() // Reset state so it doesn't auto-trigger on back press
            onSendCodeClick()
        }
    }

    // Exact colors from your screenshot
    val inputBgColor = Color(0xFFEFF5F0)  // Light mint/gray for input
    val buttonColor = Color(0xFF00E676)   // Bright Green for button
    val linkColor = Color(0xFF4CAF50)     // Darker Green for footer link

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        // 1. Header (Back Icon + Title)
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(24.dp)
                    .clickable { onBackClick() }
            )

            Text(
                text = "Forgot Password",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // 2. Input Label
        Text(
            text = "Enter your email",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 3. Email Input Field
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            enabled = !isLoading, // Disable while loading
            colors = TextFieldDefaults.colors(
                focusedContainerColor = inputBgColor,
                unfocusedContainerColor = inputBgColor,
                disabledContainerColor = inputBgColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black
            )
        )

        // 4. Error Message Display
        if (forgotState is AuthStatus.Error) {
            Text(
                text = (forgotState as AuthStatus.Error).msg,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 5. Send Code Button
        Button(
            onClick = {
                if (email.isNotEmpty()) {
                    authViewModel.sendOtp(email)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            shape = RoundedCornerShape(8.dp),
            enabled = !isLoading && email.isNotEmpty()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text(
                    text = "Send Verification Code",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        // 6. Spacer to push "Back to Login" to the bottom
        Spacer(modifier = Modifier.weight(1f))

        // 7. Footer Link
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Back to Login",
                color = linkColor,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onBackClick() }
            )
        }

        // Bottom safe area padding
        Spacer(modifier = Modifier.height(16.dp))
    }
}