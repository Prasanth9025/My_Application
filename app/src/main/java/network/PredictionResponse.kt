package com.example.myapplication.network

import com.google.gson.annotations.SerializedName

data class PredictionResponse(
    // The "dosha" key matches, but it's safer to annotate
    @SerializedName("dosha")
    val dosha: String,

    // CRITICAL FIX: Map "vata_score" (PHP) to "vata" (Kotlin)
    @SerializedName("vata_score")
    val vata: Int,

    @SerializedName("pitta_score")
    val pitta: Int,

    @SerializedName("kapha_score")
    val kapha: Int,

    // Optional: Use a default empty list to prevent crashes if null
    val recommendations: List<String> = emptyList()
)