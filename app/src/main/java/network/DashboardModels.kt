package com.example.myapplication.network

import com.google.gson.annotations.SerializedName

data class DashboardResponse(
    val current: DoshaScore?,
    val history: List<DoshaScore>,
    val trends: Trends
)

data class DoshaScore(
    @SerializedName("vata_score") val vataScore: Int = 0,
    @SerializedName("pitta_score") val pittaScore: Int = 0,
    @SerializedName("kapha_score") val kaphaScore: Int = 0,
    @SerializedName("created_at") val createdAt: String = ""
)

data class Trends(
    @SerializedName("vata_change") val vataChange: Int = 0,
    @SerializedName("pitta_change") val pittaChange: Int = 0,
    @SerializedName("kapha_change") val kaphaChange: Int = 0
)