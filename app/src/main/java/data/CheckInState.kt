package com.example.myapplication.data

// --- 1. Define Enums Here ---
enum class SleepQuality { POOR, MODERATE, GOOD }
enum class StressLevel { LOW, MEDIUM, HIGH }
enum class EnergyLevel { LOW, NORMAL, HIGH }
enum class BowelMovement { REGULAR, DRY_HARD, LOOSE, HEAVY }
enum class Digestion { LIGHT, NORMAL, HEAVY, BLOATED }
enum class Appetite { LOW, NORMAL, STRONG }

// --- 2. Update Data Class to use Enums ---
data class CheckInState(
    // Sliders (Always have a value)
    val sleepDuration: Float = 7.0f,
    val hydration: Int = 2,

    // Selection Fields (Use Enums now, not Strings)
    // We use nullable (?) so they can start as "unselected" (null)
    val sleepQuality: SleepQuality? = null,
    val stressLevel: StressLevel? = null,
    val morningEnergy: EnergyLevel? = null,
    val eveningEnergy: EnergyLevel? = null,
    val bowelMovement: BowelMovement? = null,
    val digestion: Digestion? = null,
    val appetite: Appetite? = null,

    // Text/List Fields
    val symptoms: List<String> = emptyList(),
    val mood: String? = null,
    val physicalActivity: String? = null
)