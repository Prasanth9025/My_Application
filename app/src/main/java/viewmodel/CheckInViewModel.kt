package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.network.PredictionRequest
import com.example.myapplication.network.PredictionResponse
import com.example.myapplication.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Enums
enum class SleepQuality { POOR, MODERATE, GOOD }
enum class StressLevel { LOW, MEDIUM, HIGH }
enum class EnergyLevel { LOW, NORMAL, HIGH }
enum class BowelMovement { REGULAR, DRY_HARD, LOOSE, HEAVY }
enum class Digestion { LIGHT, NORMAL, HEAVY, BLOATED }
enum class Appetite { LOW, NORMAL, STRONG }

data class CheckInState(
    // Sliders (Always have a value)
    val sleepDuration: Float = 7.0f,
    val hydration: Int = 2,

    // Choices (Now Nullable - Starts Empty)
    val sleepQuality: SleepQuality? = null,
    val stressLevel: StressLevel? = null,
    val morningEnergy: EnergyLevel? = null,
    val eveningEnergy: EnergyLevel? = null,
    val bowelMovement: BowelMovement? = null,
    val digestion: Digestion? = null,
    val appetite: Appetite? = null,

    // Lists and Strings
    val symptoms: List<String> = emptyList(),
    val mood: String? = null,
    val physicalActivity: String? = null
)

class CheckInViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CheckInState())
    val uiState: StateFlow<CheckInState> = _uiState.asStateFlow()

    private val _prediction = MutableStateFlow<PredictionResponse?>(null)
    val prediction: StateFlow<PredictionResponse?> = _prediction.asStateFlow()

    // --- Update Functions ---
    fun updateSleepDuration(value: Float) { _uiState.update { it.copy(sleepDuration = value) } }
    fun updateSleepQuality(value: SleepQuality) { _uiState.update { it.copy(sleepQuality = value) } }
    fun updateStressLevel(value: StressLevel) { _uiState.update { it.copy(stressLevel = value) } }
    fun updateMorningEnergy(value: EnergyLevel) { _uiState.update { it.copy(morningEnergy = value) } }
    fun updateEveningEnergy(value: EnergyLevel) { _uiState.update { it.copy(eveningEnergy = value) } }

    fun toggleSymptom(symptom: String) {
        _uiState.update { current ->
            val list = current.symptoms.toMutableList()
            if (list.contains(symptom)) list.remove(symptom) else list.add(symptom)
            current.copy(symptoms = list)
        }
    }

    fun updateBowel(value: BowelMovement) { _uiState.update { it.copy(bowelMovement = value) } }
    fun updateHydration(value: Int) { _uiState.update { it.copy(hydration = value) } }
    fun updateMood(value: String) { _uiState.update { it.copy(mood = value) } }
    fun updateActivity(value: String) { _uiState.update { it.copy(physicalActivity = value) } }
    fun updateDigestion(value: Digestion) { _uiState.update { it.copy(digestion = value) } }
    fun updateAppetite(value: Appetite) { _uiState.update { it.copy(appetite = value) } }

    // --- REAL BACKEND SUBMIT FUNCTION ---
    fun submitData() {
        viewModelScope.launch {
            try {
                println("DEBUG: Starting Real Network Request...")

                // Use '?.' to safely access data. If null, default to 0 (First Option).
                val request = PredictionRequest(
                    sleep_quality = _uiState.value.sleepQuality?.ordinal ?: 1,
                    stress_level = _uiState.value.stressLevel?.ordinal ?: 1,
                    energy_level = _uiState.value.morningEnergy?.ordinal ?: 1,
                    digestion = _uiState.value.digestion?.ordinal ?: 1,
                    stool_type = _uiState.value.bowelMovement?.ordinal ?: 0,
                    skin_condition = 0,
                    gender = 1
                )

                println("DEBUG: Sending Data: $request")

                val response = RetrofitClient.apiService.predictDosha(request)

                println("DEBUG: Success! Server Responded: $response")
                _prediction.value = response

            } catch (e: Exception) {
                println("DEBUG: Network Error Failed!")
                e.printStackTrace()
            }
        }
    }
}