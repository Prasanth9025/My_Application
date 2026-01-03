package com.example.myapplication.data

// This acts as the "Envelope" that holds all the user's answers
data class CheckInState(
    // Input Fields (Text)
    val sleepHours: String = "",
    val hydration: String = "",

    // Selection Fields (Multiple Choice)
    val sleepQuality: String = "",
    val stressLevel: String = "",
    val morningEnergy: String = "",
    val eveningEnergy: String = "",
    val bodySensation: String = "",
    val bowelMovement: String = "",
    val mood: String = "",
    val physicalActivity: String = "",
    val digestion: String = "",
    val appetite: String = ""
)