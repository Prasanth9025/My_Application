package com.example.myapplication.network

data class PredictionResponse(
    val dosha: String,
    val vata: Int,
    val pitta: Int,
    val kapha: Int,
    val recommendations: List<String> // <--- Ensure this exists!
)