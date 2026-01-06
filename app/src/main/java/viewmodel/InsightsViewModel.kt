package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.UserSession
import com.example.myapplication.network.HistoryItem
import com.example.myapplication.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InsightsViewModel : ViewModel() {

    private val _history = MutableStateFlow<List<HistoryItem>>(emptyList())
    val history: StateFlow<List<HistoryItem>> = _history.asStateFlow()

    private val _insightsData = MutableStateFlow(InsightsData())
    val insightsData: StateFlow<InsightsData> = _insightsData.asStateFlow()

    fun fetchInsights() {
        viewModelScope.launch {
            val userId = UserSession.getUserId()
            if (userId == -1) return@launch

            try {
                val response = RetrofitClient.apiService.getUserHistory(userId)
                _history.value = response
                calculateTrends(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun calculateTrends(data: List<HistoryItem>) {
        if (data.isEmpty()) return

        // Take last 7 entries for the chart
        val recent = data.take(7).reversed()
        val latest = data.first() // The most recent check-in

        // --- 1. CALCULATE DOSHA BALANCE ---
        val totalScore = (latest.vataScore + latest.pittaScore + latest.kaphaScore).toFloat()
        val vataPct = if (totalScore > 0) ((latest.vataScore / totalScore) * 100).toInt() else 0
        val pittaPct = if (totalScore > 0) ((latest.pittaScore / totalScore) * 100).toInt() else 0
        val kaphaPct = if (totalScore > 0) ((latest.kaphaScore / totalScore) * 100).toInt() else 0

        val doshaString = "Vata $vataPct%, Pitta $pittaPct%, Kapha $kaphaPct%"

        // --- 2. CALCULATE DOSHA TREND (Vata as reference) ---
        val avgVata = if (data.size > 1) data.drop(1).take(7).map { it.vataScore }.average() else latest.vataScore.toDouble()
        val trendDiff = latest.vataScore - avgVata
        val trendSign = if (trendDiff > 0) "+" else ""
        val trendString = "Last 7 Days $trendSign${trendDiff.toInt()}% (Vata)"

        // --- 3. CALCULATE FACTORS ---
        val avgSleep = recent.map { it.sleepHours }.average().toFloat()
        val avgHydration = recent.map { it.hydration }.average().toFloat()
        val avgStress = recent.map { it.stressLevel }.average().toFloat()

        _insightsData.value = InsightsData(
            // New Dosha Data
            doshaBalanceText = doshaString,
            doshaTrendText = trendString,

            // Factor Data
            avgSleep = "%.1f hours".format(avgSleep),
            avgHydration = "%.1f liters".format(avgHydration),
            avgStress = if(avgStress > 7) "High" else if (avgStress > 4) "Moderate" else "Low",

            sleepChart = recent.map { (it.sleepHours / 12f).coerceIn(0.1f, 1f) },
            hydrationChart = recent.map { (it.hydration / 5f).coerceIn(0.1f, 1f) },
            stressChart = recent.map { (it.stressLevel / 10f).coerceIn(0.1f, 1f) }
        )
    }
}

// Updated Data Model
data class InsightsData(
    val doshaBalanceText: String = "Vata --%, Pitta --%, Kapha --%",
    val doshaTrendText: String = "Last 7 Days --%",
    val avgSleep: String = "--",
    val avgHydration: String = "--",
    val avgStress: String = "--",
    val sleepChart: List<Float> = emptyList(),
    val hydrationChart: List<Float> = emptyList(),
    val stressChart: List<Float> = emptyList()
)