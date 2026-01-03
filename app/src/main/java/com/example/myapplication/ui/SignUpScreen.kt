package com.example.myapplication.ui

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignUpScreen(onSignUpSuccess: () -> Unit, onLoginClick: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Colors extracted from your design
    val inputBackgroundColor = Color(0xFFEFF5F0) // Light greenish-grey
    val buttonColor = Color(0xFF00E676) // Bright Green
    val linkColor = Color(0xFF4CAF50) // Darker Green for text

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()), // Allows scrolling if keyboard opens
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Close Button (X)
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onLoginClick() } // Go back to login
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Title
        Text(
            text = "Create your account",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 3. Input Fields
        CustomInput(value = name, onValueChange = { name = it }, placeholder = "Name", color = inputBackgroundColor)
        Spacer(modifier = Modifier.height(16.dp))

        CustomInput(
            value = phone,
            onValueChange = { phone = it },
            placeholder = "Phone Number",
            color = inputBackgroundColor,
            keyboardType = KeyboardType.Phone
        )
        Spacer(modifier = Modifier.height(16.dp))

        CustomInput(
            value = email,
            onValueChange = { email = it },
            placeholder = "Email",
            color = inputBackgroundColor,
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(16.dp))

        CustomInput(
            value = password,
            onValueChange = { password = it },
            placeholder = "Password",
            color = inputBackgroundColor,
            isPassword = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        CustomInput(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = "Confirm Password",
            color = inputBackgroundColor,
            isPassword = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 4. Sign Up Button
        Button(
            onClick = { onSignUpSuccess() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Sign Up", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 5. Bottom Links
        Text("Already have an account?", color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Log In",
            color = linkColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onLoginClick() }
        )
    }
}

// Helper Composable for the styled inputs
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