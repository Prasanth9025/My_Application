package com.example.myapplication.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Import your ViewModel and Status
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.AuthViewModel
import com.example.myapplication.viewmodel.AuthStatus

@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel = viewModel(), // Inject ViewModel
    onSignUpSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current
    // Observe the registration state
    val registerState by authViewModel.registerState.collectAsState()

    // Listen for changes in state (Success or Error)
    LaunchedEffect(registerState) {
        when (registerState) {
            is AuthStatus.Success -> {
                Toast.makeText(context, "Account Created!", Toast.LENGTH_SHORT).show()
                onLoginClick() // Go to Login after success
                authViewModel.resetRegisterState()
            }
            is AuthStatus.Error -> {
                Toast.makeText(context, (registerState as AuthStatus.Error).msg, Toast.LENGTH_LONG).show()
                authViewModel.resetRegisterState()
            }
            else -> {}
        }
    }

    val inputBackgroundColor = Color(0xFFEFF5F0)
    val buttonColor = Color(0xFF00E676)
    val linkColor = Color(0xFF4CAF50)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ... (Close Icon and Title remain the same) ...
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.size(24.dp).clickable { onLoginClick() }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Create your account", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(32.dp))

        // Inputs
        CustomInput(value = name, onValueChange = { name = it }, placeholder = "Name", color = inputBackgroundColor)
        Spacer(modifier = Modifier.height(16.dp))
        CustomInput(value = phone, onValueChange = { phone = it }, placeholder = "Phone Number", color = inputBackgroundColor, keyboardType = KeyboardType.Phone)
        Spacer(modifier = Modifier.height(16.dp))
        CustomInput(value = email, onValueChange = { email = it }, placeholder = "Email", color = inputBackgroundColor, keyboardType = KeyboardType.Email)
        Spacer(modifier = Modifier.height(16.dp))
        CustomInput(value = password, onValueChange = { password = it }, placeholder = "Password", color = inputBackgroundColor, isPassword = true)
        Spacer(modifier = Modifier.height(16.dp))
        CustomInput(value = confirmPassword, onValueChange = { confirmPassword = it }, placeholder = "Confirm Password", color = inputBackgroundColor, isPassword = true)

        Spacer(modifier = Modifier.height(32.dp))

        // Sign Up Button
        Button(
            onClick = {
                if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    if (password == confirmPassword) {
                        // CALL THE BACKEND HERE
                        authViewModel.register(name, email, password,phone) // Note: Update ViewModel to accept phone if needed
                    } else {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            shape = RoundedCornerShape(12.dp),
            enabled = registerState !is AuthStatus.Loading
        ) {
            if (registerState is AuthStatus.Loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Sign Up", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text("Already have an account?", color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Log In", color = linkColor, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onLoginClick() })
    }
}

// ... (Keep CustomInput helper function exactly as it is) ...
@Composable
fun CustomInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    color: Color,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.Gray) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = color,
            unfocusedContainerColor = color,
            disabledContainerColor = color,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        singleLine = true
    )
}