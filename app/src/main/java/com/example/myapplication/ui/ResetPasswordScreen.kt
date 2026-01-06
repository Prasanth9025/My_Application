package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.AuthStatus
import com.example.myapplication.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    onResetClick: () -> Unit, // Navigate to Login on success
    onBackClick: () -> Unit,
    authViewModel: AuthViewModel = viewModel() // Inject ViewModel
) {
    var otp by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    // 1. Observe State
    val resetState by authViewModel.resetPasswordState.collectAsState()
    val isLoading = resetState is AuthStatus.Loading

    // 2. Handle Success
    LaunchedEffect(resetState) {
        if (resetState is AuthStatus.Success) {
            authViewModel.resetResetState()
            onResetClick() // Navigate to Login
        }
    }

    // Colors
    val inputBgColor = Color(0xFFEFF5F0)
    val buttonColor = Color(0xFF00E676)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Set New Password",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
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
                .padding(24.dp),
            horizontalAlignment = Alignment.Start
        ) {

            // --- 1. OTP SECTION (New) ---
            Text(
                text = "Verification Code (OTP)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            TextField(
                value = otp,
                onValueChange = { otp = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text("Enter 6-digit code", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = inputBgColor,
                    unfocusedContainerColor = inputBgColor,
                    disabledContainerColor = inputBgColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- 2. NEW PASSWORD SECTION ---
            Text(
                text = "New Password",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            TextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                placeholder = { Text("Enter new password", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = inputBgColor,
                    unfocusedContainerColor = inputBgColor,
                    disabledContainerColor = inputBgColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- 3. CONFIRM PASSWORD SECTION ---
            Text(
                text = "Confirm New Password",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                placeholder = { Text("Confirm new password", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = inputBgColor,
                    unfocusedContainerColor = inputBgColor,
                    disabledContainerColor = inputBgColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                )
            )

            // --- ERROR MESSAGES ---
            if (validationError != null) {
                Text(validationError!!, color = Color.Red, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
            }

            if (resetState is AuthStatus.Error) {
                Text((resetState as AuthStatus.Error).msg, color = Color.Red, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- 4. RESET BUTTON ---
            Button(
                onClick = {
                    // Logic & Validation
                    validationError = null
                    when {
                        otp.isEmpty() -> validationError = "Please enter the OTP sent to your email."
                        newPassword.isEmpty() -> validationError = "Password cannot be empty."
                        newPassword != confirmPassword -> validationError = "Passwords do not match."
                        else -> authViewModel.resetPassword(otp, newPassword)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(8.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.Black)
                } else {
                    Text(
                        text = "Reset Password",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}