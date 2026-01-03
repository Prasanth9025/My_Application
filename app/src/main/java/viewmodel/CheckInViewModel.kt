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

// Enums for the choices
enum class SleepQuality { POOR, MODERATE, GOOD }
enum class StressLevel { LOW, MEDIUM, HIGH }
enum class EnergyLevel { LOW, NORMAL, HIGH }
enum class BowelMovement { REGULAR, DRY_HARD, LOOSE, HEAVY }
enum class Digestion { LIGHT, NORMAL, HEAVY, BLOATED }
enum class Appetite { LOW, NORMAL, STRONG }

data class CheckInState(
    // Step 1
    val sleepDuration: Float = 7.0f,
    // Step 2
    val sleepQuality: SleepQuality = SleepQuality.GOOD,
    // Step 3
    val stressLevel: StressLevel = StressLevel.MEDIUM,
    // Step 4 & 5
    val morningEnergy: EnergyLevel = EnergyLevel.NORMAL,
    val eveningEnergy: EnergyLevel = EnergyLevel.NORMAL,
    // Step 6 (Body Sensations)
    val symptoms: List<String> = emptyList(),
    // Step 7
    val bowelMovement: BowelMovement = BowelMovement.REGULAR,
    // Step 8
    val hydration: Int = 2, // Liters
    // Step 9
    val mood: String = "Calm",
    // Step 10
    val physicalActivity: String = "Moderate",
    // Step 11
    val digestion: Digestion = Digestion.NORMAL,
    // Step 12
    val appetite: Appetite = Appetite.NORMAL
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

    // --- Submit ---
    fun submitData() {
        viewModelScope.launch {
            try {
                // Mapping complex state to simple integers for API
                val request = PredictionRequest(
                    sleep_quality = _uiState.value.sleepQuality.ordinal,
                    stress_level = _uiState.value.stressLevel.ordinal,
                    energy_level = _uiState.value.morningEnergy.ordinal,
                    digestion = _uiState.value.digestion.ordinal,
                    stool_type = _uiState.value.bowelMovement.ordinal,
                    skin_condition = 0, // Default if not asked
                    gender = 1
                )
                val response = RetrofitClient.apiService.predictDosha(request)
                _prediction.value = response
            } catch (e: Exception) {
                // Fallback for demo
                _prediction.value = PredictionResponse("Kapha", 30, 20, 50, listOf("Stay active", "Eat warm food"))
            }
        }
    }
}