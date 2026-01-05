package com.example.myapplication.network

// --- LOGIN ---
data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val status: String,
    val message: String,
    val user_id: Int?, // Nullable because it's only present on success
    val name: String?
)

// --- REGISTER ---
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val phone: String
)

data class RegisterResponse(
    val status: String,
    val message: String
)

// --- HISTORY ---
data class HistoryItem(
    val id: Int,
    val predicted_dosha: String,
    val created_at: String
)