package com.example.myapplication.network

data class PredictionRequest(
    val user_id: Int, // <--- ADD THIS LINE
    val sleep_quality: Int,
    val stress_level: Int,
    val energy_level: Int,
    val digestion: Int,
    val stool_type: Int,
    val skin_condition: Int,
    val gender: Int
)