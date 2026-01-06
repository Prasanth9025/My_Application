package com.example.myapplication.network

import com.google.gson.annotations.SerializedName

// --- LOGIN ---
data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val status: String, val message: String, @SerializedName("user_id") val userId: Int?, val name: String?)
//forgot
data class ForgotPasswordRequest(val email: String)
data class ForgotPasswordResponse(val status: String, val message: String)

data class ResetPasswordRequest(val email: String, val otp: String, @SerializedName("new_password") val newPassword: String)
data class ResetPasswordResponse(val status: String, val message: String)

// --- REGISTER ---
data class RegisterRequest(val name: String, val email: String, val password: String, val phone: String)
data class RegisterResponse(val status: String, val message: String)

// --- HISTORY & INSIGHTS (UPDATED) ---
data class HistoryItem(
    val id: Int,
    @SerializedName("predicted_dosha") val predictedDosha: String,

    // NEW: Scores for Graphs
    @SerializedName("vata_score") val vataScore: Int = 0,
    @SerializedName("pitta_score") val pittaScore: Int = 0,
    @SerializedName("kapha_score") val kaphaScore: Int = 0,

    // NEW: Factors for Insights
    @SerializedName("sleep_hours") val sleepHours: Float = 0f,
    @SerializedName("hydration") val hydration: Float = 0f,
    @SerializedName("stress_level") val stressLevel: Int = 0,

    @SerializedName("created_at") val createdAt: String
)

// --- USER PROFILE ---
data class UpdateProfileRequest(@SerializedName("user_id") val userId: Int, val name: String, val email: String, val phone: String, val gender: String, val dob: String, val country: String, val password: String? = null)
data class UpdateProfileResponse(val status: String, val message: String)
data class UserProfile(val name: String, val email: String, val phone: String?, val gender: String?, val dob: String?, val country: String?)