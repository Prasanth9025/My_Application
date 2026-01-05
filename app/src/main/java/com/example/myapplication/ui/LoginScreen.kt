package com.example.myapplication.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel // Add this dependency if missing
import com.example.myapplication.R
import com.example.myapplication.viewmodel.AuthStatus
import com.example.myapplication.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = viewModel(), // Inject ViewModel
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Listen to Login Status
    val loginState by authViewModel.loginState.collectAsState()

    // Handle Side Effects (Toast messages / Navigation)
    LaunchedEffect(loginState) {
        when (loginState) {
            is AuthStatus.Success -> {
                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                onLoginSuccess() // Navigate to Home
                authViewModel.resetLoginState()
            }
            is AuthStatus.Error -> {
                Toast.makeText(context, (loginState as AuthStatus.Error).msg, Toast.LENGTH_LONG).show()
                authViewModel.resetLoginState()
            }
            else -> {}
        }
    }

    val beigeInputColor = Color(0xFFF3F0E9)
    val leafGreen = Color(0xFF436B48)

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        // 1. TOP IMAGE (Same as before)
        Box(modifier = Modifier.fillMaxWidth().height(218.dp)) {
            Image(
                painter = painterResource(id = R.drawable.login),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // 2. FORM
        Column(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome back", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = beigeInputColor,
                    unfocusedContainerColor = beigeInputColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text("Forgot password?", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.clickable { onForgotPasswordClick() })
            Spacer(modifier = Modifier.height(32.dp))

            // LOGIN BUTTON
            Button(
                onClick = {
                    if(email.isNotEmpty() && password.isNotEmpty()) {
                        authViewModel.login(email, password) // CALL REAL LOGIN
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = leafGreen),
                shape = RoundedCornerShape(8.dp),
                enabled = loginState !is AuthStatus.Loading // Disable while loading
            ) {
                if (loginState is AuthStatus.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Login", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Don't have an account? ", color = Color.Gray)
                Text("Sign up", color = leafGreen, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onSignUpClick() })
            }
        }
    }
}