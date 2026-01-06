package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.*
import com.example.myapplication.network.DashboardResponse
import com.example.myapplication.network.PredictionRequest
import com.example.myapplication.network.PredictionResponse
import com.example.myapplication.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckInViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CheckInState())
    val uiState: StateFlow<CheckInState> = _uiState.asStateFlow()

    private val _prediction = MutableStateFlow<PredictionResponse?>(null)
    val prediction: StateFlow<PredictionResponse?> = _prediction.asStateFlow()

    private val _dashboardState = MutableStateFlow<DashboardResponse?>(null)
    val dashboardState: StateFlow<DashboardResponse?> = _dashboardState.asStateFlow()

    // --- Simple Updates ---
    fun updateSleepDuration(value: Float) { _uiState.update { it.copy(sleepDuration = value) } }
    fun updateSleepQuality(value: SleepQuality) { _uiState.update { it.copy(sleepQuality = value) } }
    fun updateStressLevel(value: StressLevel) { _uiState.update { it.copy(stressLevel = value) } }
    fun updateMorningEnergy(value: EnergyLevel) { _uiState.update { it.copy(morningEnergy = value) } }
    fun updateEveningEnergy(value: EnergyLevel) { _uiState.update { it.copy(eveningEnergy = value) } }
    fun updateBowel(value: BowelMovement) { _uiState.update { it.copy(bowelMovement = value) } }
    fun updateHydration(value: Int) { _uiState.update { it.copy(hydration = value) } }
    fun updateMood(value: String) { _uiState.update { it.copy(mood = value) } }
    fun updateActivity(value: String) { _uiState.update { it.copy(physicalActivity = value) } }
    fun updateDigestion(value: Digestion) { _uiState.update { it.copy(digestion = value) } }
    fun updateAppetite(value: Appetite) { _uiState.update { it.copy(appetite = value) } }

    fun toggleSymptom(symptom: String) {
        _uiState.update { current ->
            val list = current.symptoms.toMutableList()
            if (list.contains(symptom)) list.remove(symptom) else list.add(symptom)
            current.copy(symptoms = list)
        }
    }

    // --- FETCH DASHBOARD ---
    fun fetchDashboard() {
        viewModelScope.launch {
            try {
                val userId = UserSession.getUserId()
                if (userId != -1) {
                    val response = RetrofitClient.apiService.getDashboard(userId)
                    _dashboardState.value = response
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // --- SUBMIT DATA (UPDATED) ---
    fun submitData() {
        viewModelScope.launch {
            try {
                val currentState = _uiState.value
                val userId = UserSession.getUserId()

                // Enum Mappings
                val sleepScore = when (currentState.sleepQuality) { SleepQuality.POOR -> 3; SleepQuality.MODERATE -> 5; SleepQuality.GOOD -> 8; null -> 5 }
                val stressScore = when (currentState.stressLevel) { StressLevel.LOW -> 2; StressLevel.MEDIUM -> 5; StressLevel.HIGH -> 9; null -> 5 }
                val energyScore = when (currentState.morningEnergy) { EnergyLevel.LOW -> 3; EnergyLevel.NORMAL -> 6; EnergyLevel.HIGH -> 9; null -> 5 }
                val digestionScore = when (currentState.digestion) { Digestion.LIGHT -> 3; Digestion.NORMAL -> 6; Digestion.HEAVY -> 2; Digestion.BLOATED -> 1; null -> 5 }
                val bowelScore = when (currentState.bowelMovement) { BowelMovement.REGULAR -> 6; BowelMovement.DRY_HARD -> 3; BowelMovement.LOOSE -> 8; BowelMovement.HEAVY -> 2; null -> 5 }

                // Create Request with ALL fields
                val request = PredictionRequest(
                    user_id = userId,
                    sleep_quality = sleepScore,
                    stress_level = stressScore,
                    energy_level = energyScore,
                    digestion = digestionScore,
                    stool_type = bowelScore,
                    skin_condition = 5,
                    gender = 1,

                    // Sending UI Data
                    sleep_hours = currentState.sleepDuration,
                    hydration = currentState.hydration.toFloat()
                )

                val response = RetrofitClient.apiService.predictDosha(request)
                _prediction.value = response
                fetchDashboard() // Refresh home screen
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}