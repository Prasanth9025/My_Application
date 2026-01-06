package com.example.myapplication.network

import com.google.gson.annotations.SerializedName

data class PredictionRequest(
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("sleep_quality") val sleep_quality: Int,
    @SerializedName("stress_level") val stress_level: Int,
    @SerializedName("energy_level") val energy_level: Int,
    @SerializedName("digestion") val digestion: Int,
    @SerializedName("stool_type") val stool_type: Int,
    @SerializedName("skin_condition") val skin_condition: Int,
    @SerializedName("gender") val gender: Int,

    // NEW FIELDS (Must be here!)
    @SerializedName("sleep_hours") val sleep_hours: Float,
    @SerializedName("hydration") val hydration: Float
)