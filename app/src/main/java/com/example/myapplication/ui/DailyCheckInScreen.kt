package com.example.myapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.grid.*
import com.example.myapplication.viewmodel.*

// --- COLORS ---
val RoyalBlue = Color(0xFF2563EB)
val TextGray = Color(0xFF6B7280)
val SelectedLightBlue = Color(0xFFEFF6FF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyCheckInScreen(
    viewModel: CheckInViewModel,
    onNavigateToSummary: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var step by remember { mutableIntStateOf(1) }
    val totalSteps = 12

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = { Text(getStepTitle(step), fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                    navigationIcon = {
                        if (step > 1) {
                            IconButton(onClick = { step-- }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
                LinearProgressIndicator(
                    progress = { step.toFloat() / totalSteps.toFloat() },
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = Color.Black,
                    trackColor = Color(0xFFEEEEEE),
                    strokeCap = StrokeCap.Round
                )
            }
        },
        bottomBar = {
            Button(
                onClick = { if (step < totalSteps) step++ else onNavigateToSummary() },
                modifier = Modifier.fillMaxWidth().padding(24.dp).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (step == totalSteps) "Finish" else "Next", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).padding(horizontal = 24.dp).fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text("Step $step of $totalSteps", color = Color.Gray, fontSize = 14.sp)
            Spacer(Modifier.height(8.dp))
            Text(getQuestionText(step), fontSize = 24.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp)
            Spacer(Modifier.height(32.dp))

            when (step) {
                1 -> StepSlider(state.sleepDuration, 12f, "Hours") { viewModel.updateSleepDuration(it) }

                // SAFE CALL (?.) prevents crash if value is null
                2 -> StepSimpleOptions(
                    listOf("Good", "Moderate", "Poor"),
                    state.sleepQuality?.name?.toPrettyString()
                ) { str ->
                    val enumVal = when(str) { "Good" -> SleepQuality.GOOD; "Poor" -> SleepQuality.POOR; else -> SleepQuality.MODERATE }
                    viewModel.updateSleepQuality(enumVal)
                }

                3 -> StepSimpleOptions(
                    listOf("Low", "Medium", "High"),
                    state.stressLevel?.name?.toPrettyString()
                ) { str ->
                    val enumVal = when(str) { "Low" -> StressLevel.LOW; "High" -> StressLevel.HIGH; else -> StressLevel.MEDIUM }
                    viewModel.updateStressLevel(enumVal)
                }

                4 -> StepHeroImage(
                    listOf("Low", "Normal", "High"),
                    state.morningEnergy?.name?.toPrettyString(),
                    Color(0xFFFFCC80)
                ) { str ->
                    val enumVal = when(str) { "Low" -> EnergyLevel.LOW; "High" -> EnergyLevel.HIGH; else -> EnergyLevel.NORMAL }
                    viewModel.updateMorningEnergy(enumVal)
                }

                5 -> StepSimpleOptions(
                    listOf("Low", "Normal", "High"),
                    state.eveningEnergy?.name?.toPrettyString()
                ) { str ->
                    val enumVal = when(str) { "Low" -> EnergyLevel.LOW; "High" -> EnergyLevel.HIGH; else -> EnergyLevel.NORMAL }
                    viewModel.updateEveningEnergy(enumVal)
                }

                6 -> StepGridSensations(state.symptoms) { viewModel.toggleSymptom(it) }

                7 -> StepSimpleOptions(
                    listOf("Regular", "Dry/Hard", "Loose", "Heavy"),
                    if (state.bowelMovement != null) mapBowelToString(state.bowelMovement!!) else null,
                    isHorizontal = true
                ) { str -> viewModel.updateBowel(mapStringToBowel(str)) }

                8 -> StepSlider(state.hydration.toFloat(), 5f, "Liters") { viewModel.updateHydration(it.toInt()) }

                9 -> StepRichList(getMoodOptions(), state.mood) { viewModel.updateMood(it) }

                10 -> StepRichList(getActivityOptions(), state.physicalActivity) { viewModel.updateActivity(it) }

                11 -> StepSimpleOptions(
                    listOf("Light", "Normal", "Heavy", "Bloated"),
                    state.digestion?.name?.toPrettyString(),
                    isHorizontal = true
                ) { str ->
                    val enumVal = when(str) { "Light" -> Digestion.LIGHT; "Heavy" -> Digestion.HEAVY; "Bloated" -> Digestion.BLOATED; else -> Digestion.NORMAL }
                    viewModel.updateDigestion(enumVal)
                }

                12 -> StepSimpleOptions(
                    listOf("Low", "Normal", "Strong"),
                    state.appetite?.name?.toPrettyString(),
                    isHorizontal = true
                ) { str ->
                    val enumVal = when(str) { "Low" -> Appetite.LOW; "Strong" -> Appetite.STRONG; else -> Appetite.NORMAL }
                    viewModel.updateAppetite(enumVal)
                }
            }
            Spacer(Modifier.height(80.dp))
        }
    }
}

// --- HELPER EXTENSION ---
// Makes "GOOD" -> "Good", safely handles nulls
fun String.toPrettyString(): String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}

// --- MAPPERS ---
fun mapBowelToString(b: BowelMovement): String = when(b) {
    BowelMovement.REGULAR -> "Regular"
    BowelMovement.DRY_HARD -> "Dry/Hard"
    BowelMovement.LOOSE -> "Loose"
    BowelMovement.HEAVY -> "Heavy"
}

fun mapStringToBowel(s: String): BowelMovement = when(s) {
    "Dry/Hard" -> BowelMovement.DRY_HARD
    "Loose" -> BowelMovement.LOOSE
    "Heavy" -> BowelMovement.HEAVY
    else -> BowelMovement.REGULAR
}

// --- UI COMPONENTS ---
@Composable
fun StepSlider(value: Float, max: Float, unit: String, onChange: (Float) -> Unit) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
            Text(unit, color = TextGray, fontSize = 16.sp)
            Text("${value.toInt()}", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }
        Slider(value = value, onValueChange = onChange, valueRange = 0f..max)
    }
}

@Composable
fun StepSimpleOptions(options: List<String>, selected: String?, isHorizontal: Boolean = false, onSelect: (String) -> Unit) {
    if (isHorizontal) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            options.forEach { opt -> Box(Modifier.weight(1f)) { OptionButton(opt, selected == opt) { onSelect(opt) } } }
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            options.forEach { opt -> OptionButton(opt, selected == opt) { onSelect(opt) } }
        }
    }
}

@Composable
fun OptionButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) SelectedLightBlue else Color.White,
            contentColor = if (isSelected) RoyalBlue else Color.Black
        )
    ) {
        Text(text, fontSize = 16.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium)
    }
}

@Composable
fun StepHeroImage(options: List<String>, selected: String?, placeholderColor: Color, onSelect: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(12.dp)).background(placeholderColor))
        StepSimpleOptions(options, selected, false, onSelect)
    }
}

@Composable
fun StepGridSensations(selectedItems: List<String>, onToggle: (String) -> Unit) {
    val items = listOf(
        "Dryness" to Icons.Default.WaterDrop, "Heat" to Icons.Default.LocalFireDepartment,
        "Heaviness" to Icons.Default.FitnessCenter, "Cold Body" to Icons.Default.AcUnit,
        "Sweet Craving" to Icons.Default.Cake, "Spicy Craving" to Icons.Default.Whatshot
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.height(300.dp)
    ) {
        items(items) { (label, icon) ->
            val isSelected = selectedItems.contains(label)
            OutlinedButton(
                onClick = { onToggle(label) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isSelected) Color.Black else Color.White,
                    contentColor = if (isSelected) Color.White else Color.Black
                ),
                contentPadding = PaddingValues(12.dp),
                modifier = Modifier.height(80.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun StepRichList(options: List<Triple<String, String, Color>>, selected: String?, onSelect: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        options.forEach { (title, subtitle, color) ->
            val isSelected = selected == title
            Row(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) SelectedLightBlue else Color(0xFFFAFAFA))
                    .clickable { onSelect(title) }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(subtitle, fontSize = 12.sp, color = TextGray)
                }
                Box(Modifier.size(60.dp, 40.dp).clip(RoundedCornerShape(8.dp)).background(color))
            }
        }
    }
}

// --- DATA HELPERS (Titles/Text) ---
fun getStepTitle(step: Int) = when (step) {
    1 -> "Sleep hours"; 2 -> "Sleep Quality"; 3 -> "Stress level"; 4 -> "Morning Energy"
    5 -> "Evening Energy"; 6 -> "Body Sensations"; 7 -> "Bowel Movement"; 8 -> "Hydration"
    9 -> "Mood"; 10 -> "Physical Activity"; 11 -> "Digestion"; 12 -> "Appetite Level"
    else -> ""
}

fun getQuestionText(step: Int) = when (step) {
    1 -> "How many hours did you sleep last night?"
    2 -> "How was your sleep quality?"
    3 -> "How stressed are you feeling today?"
    4 -> "How are you feeling this morning?"
    5 -> "How are you feeling this Evening?"
    6 -> "How does your body feel today?"
    7 -> "What was your bowel movement like today?"
    8 -> "How much water did you drink today?"
    9 -> "How are you feeling today?"
    10 -> "How active were you today?"
    11 -> "How was your digestion today?"
    12 -> "How would you describe your appetite today?"
    else -> ""
}

fun getMoodOptions() = listOf(
    Triple("Calm", "Peaceful and centered", Color(0xFFE0F2F1)),
    Triple("Irritable", "Easily annoyed or frustrated", Color(0xFFFFEBEE)),
    Triple("Anxious", "Worried and uneasy", Color(0xFFFFF3E0)),
    Triple("Low", "Feeling down or unmotivated", Color(0xFFECEFF1))
)

fun getActivityOptions() = listOf(
    Triple("None", "No physical activity", Color(0xFFEFEBE9)),
    Triple("Light", "Gentle movement, like a walk", Color(0xFFFFF8E1)),
    Triple("Moderate", "Activities like brisk walking", Color(0xFFE8F5E9)),
    Triple("Intense", "Vigorous activities like running", Color(0xFFFFEBEE))
)