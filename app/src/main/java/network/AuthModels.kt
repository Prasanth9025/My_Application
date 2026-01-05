package com.example.myapplication.network

import com.google.gson.annotations.SerializedName

// --- LOGIN ---
data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val status: String,
    val message: String,
    @SerializedName("user_id") val userId: Int?, // Mapped JSON "user_id" to Kotlin "userId"
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
    @SerializedName("predicted_dosha") val predictedDosha: String,
    @SerializedName("created_at") val createdAt: String
)

// --- UPDATE PROFILE ---
data class UpdateProfileRequest(
    @SerializedName("user_id") val userId: Int,
    val name: String,
    val email: String,
    val phone: String,
    val gender: String,
    val dob: String,
    val country: String,
    val password: String? = null // Good use of default null for optional fields
)

data class UpdateProfileResponse(
    val status: String,
    val message: String
)

// --- USER PROFILE ---
data class UserProfile(
    val name: String,
    val email: String,
    val phone: String?,
    val gender: String?,
    val dob: String?,
    val country: String?
)