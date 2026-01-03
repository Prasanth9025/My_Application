package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VerifyOtpScreen(
    onVerifyClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    var otpCode by remember { mutableStateOf("") }

    // Colors matching your design
    val screenBackgroundColor = Color(0xFFF4F9F6) // Light Mint background
    val otpBoxColor = Color.White                 // White input boxes
    val buttonColor = Color(0xFF00E676)           // Bright Green button
    val subtitleColor = Color(0xFF66BB6A)         // Muted Green text

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBackgroundColor)
            .padding(24.dp)
    ) {
        // 1. Top Bar (Close Icon)
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Title & Subtitle
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Enter the code we just sent you",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Please enter the 6-digit code sent to your email address",
                fontSize = 14.sp,
                color = subtitleColor,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // 3. 6-Digit OTP Input
        BasicTextField(
            value = otpCode,
            onValueChange = { if (it.length <= 6 && it.all { char -> char.isDigit() }) otpCode = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            decorationBox = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(6) { index ->
                        val char = if (index < otpCode.length) otpCode[index].toString() else ""
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(0.85f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(otpBoxColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        )

        // 4. Spacer to push Button & Resend Code to the bottom
        Spacer(modifier = Modifier.weight(1f))

        // 5. Verify Button
        Button(
            onClick = onVerifyClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Verify",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 6. Resend Code Link
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            TextButton(
                onClick = { /* Resend logic */ },
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
            ) {
                Text(
                    text = "Resend Code",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }

        // Bottom padding for safety
        Spacer(modifier = Modifier.height(16.dp))
    }
}