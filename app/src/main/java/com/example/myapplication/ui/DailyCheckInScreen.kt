package com.example.myapplication.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

// --- COLORS (Fixed: Added all missing colors) ---
val RoyalBlue = Color(0xFF2563EB)
val TextGray = Color(0xFF6B7280)       // Fixed: Added TextGray
val SelectedLightBlue = Color(0xFFEFF6FF) // Fixed: Added SelectedLightBlue
val BrightGreenButton = Color(0xFF00E676)
val LightGreenText = Color(0xFF69F0AE)
val GoldYellowButton = Color(0xFFFBC02D)
val DarkOverlay = Color(0xFF212121)

// --- STATE MODELS ---
data class CheckInState(
    var sleepDuration: Float = 7f,
    var sleepQuality: String? = null,
    var stressLevel: String? = null,
    var morningEnergy: String? = null,
    var eveningEnergy: String? = null,
    var symptoms: List<String> = emptyList(),
    var bowelMovement: String? = null,
    var hydration: Int = 2,
    var mood: String? = null,
    var physicalActivity: String? = null,
    var digestion: String? = null,
    var appetite: String? = null
)

@Composable
fun DailyCheckInScreen(
    onNavigateToDashboard: () -> Unit = {}
) {
    var currentView by remember { mutableStateOf("wizard") }
    val checkInState = remember { mutableStateOf(CheckInState()) }

    when (currentView) {
        "wizard" -> CheckInWizard(
            state = checkInState.value,
            onStateChange = { checkInState.value = it },
            onFinish = { currentView = "confirmation" }
        )
        "confirmation" -> ConfirmationView(
            onDashboardClick = { currentView = "summary" }
        )
        "summary" -> SummaryView(
            state = checkInState.value,
            onViewForecast = onNavigateToDashboard
        )
    }
}

// --- 1. WIZARD VIEW ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInWizard(
    state: CheckInState,
    onStateChange: (CheckInState) -> Unit,
    onFinish: () -> Unit
) {
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
                onClick = { if (step < totalSteps) step++ else onFinish() },
                modifier = Modifier.fillMaxWidth().padding(24.dp).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (step == totalSteps) "Submit" else "Next", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).padding(horizontal = 24.dp).fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Text("Step $step of $totalSteps", color = Color.Gray, fontSize = 14.sp)
            Spacer(Modifier.height(16.dp))
            Spacer(Modifier.height(8.dp))
            Text(
                text = getQuestionText(step),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp
            )
            Spacer(Modifier.height(32.dp))

            when (step) {
                1 -> StepSlider(state.sleepDuration, 12f, "Hours") { onStateChange(state.copy(sleepDuration = it)) }
                2 -> StepSimpleOptions(listOf("Good", "Moderate", "Poor"), state.sleepQuality) { onStateChange(state.copy(sleepQuality = it)) }
                3 -> StepSimpleOptions(listOf("Low", "Medium", "High"), state.stressLevel) { onStateChange(state.copy(stressLevel = it)) }
                4 -> StepHeroImage(listOf("Low", "Normal", "High"), state.morningEnergy, Color(0xFFFFCC80)) { onStateChange(state.copy(morningEnergy = it)) }
                5 -> StepSimpleOptions(listOf("Low", "Normal", "High"), state.eveningEnergy) { onStateChange(state.copy(eveningEnergy = it)) }
                6 -> StepGridSensations(state.symptoms) { item ->
                    val newList = if (state.symptoms.contains(item)) state.symptoms - item else state.symptoms + item
                    onStateChange(state.copy(symptoms = newList))
                }
                7 -> StepSimpleOptions(listOf("Regular", "Dry/Hard", "Loose", "Heavy"), state.bowelMovement, isHorizontal = true) { onStateChange(state.copy(bowelMovement = it)) }
                8 -> StepSlider(state.hydration.toFloat(), 5f, "Liters") { onStateChange(state.copy(hydration = it.toInt())) }
                9 -> StepRichList(getMoodOptions(), state.mood) { onStateChange(state.copy(mood = it)) }
                10 -> StepRichList(getActivityOptions(), state.physicalActivity) { onStateChange(state.copy(physicalActivity = it)) }
                11 -> StepSimpleOptions(listOf("Light", "Normal", "Heavy", "Bloated"), state.digestion, isHorizontal = true) { onStateChange(state.copy(digestion = it)) }
                12 -> StepSimpleOptions(listOf("Low", "Normal", "Strong"), state.appetite, isHorizontal = true) { onStateChange(state.copy(appetite = it)) }
            }
            Spacer(Modifier.height(80.dp))
        }
    }
}

// --- 2. CONFIRMATION VIEW (Matches Image Format) ---
@Composable
fun ConfirmationView(onDashboardClick: () -> Unit) {
    Box(Modifier.fillMaxSize().background(DarkOverlay), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Daily Check-in", fontSize = 12.sp, color = Color.Gray)
                Spacer(Modifier.height(8.dp))
                Text("Daily Check-in Complete!", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(Modifier.height(24.dp))

                // Matches the visual style of your image (3 panels with checkmark)
                Row(
                    modifier = Modifier.fillMaxWidth().height(140.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Left Panel (Decoration)
                    Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(8.dp)).background(Color(0xFF78909C)))

                    // Center Panel (Checkmark)
                    Box(
                        modifier = Modifier.weight(2f).fillMaxHeight().clip(RoundedCornerShape(8.dp)).background(Color(0xFFFFF3E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Large Checkmark
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFF81C784), // Soft Green check
                            modifier = Modifier.size(80.dp)
                        )
                        // Simple overlay to simulate the mountain/tree graphic in code
                        Icon(
                            Icons.Default.Landscape,
                            contentDescription = null,
                            tint = Color(0xFF455A64).copy(alpha=0.3f),
                            modifier = Modifier.size(100.dp).align(Alignment.BottomCenter)
                        )
                    }

                    // Right Panel (Decoration)
                    Box(modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(8.dp)).background(Color(0xFF78909C)))
                }

                Spacer(Modifier.height(32.dp))

                // Yellow Button
                Button(
                    onClick = onDashboardClick,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldYellowButton),
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Go to Dashboard", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// --- 3. SUMMARY VIEW (Matches Image Format) ---
@OptIn(ExperimentalMaterial3Api::class) // Fixed: Added OptIn
@Composable
fun SummaryView(state: CheckInState, onViewForecast: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Daily Check-in", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", modifier = Modifier.padding(start = 16.dp))
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Button(
                onClick = onViewForecast,
                colors = ButtonDefaults.buttonColors(containerColor = BrightGreenButton),
                modifier = Modifier.fillMaxWidth().padding(24.dp).height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("View Daily Forecast", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text("Summary for Today", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(Modifier.height(24.dp))

            // Renamed function to avoid conflict
            WizardSummaryItem("Sleep Duration", "${state.sleepDuration.toInt()} hours")
            WizardSummaryItem("Sleep Quality", state.sleepQuality ?: "-")
            WizardSummaryItem("Stress level", state.stressLevel ?: "-")
            WizardSummaryItem("Morning Energy", state.morningEnergy ?: "-")
            WizardSummaryItem("Evening Energy", state.eveningEnergy ?: "-")
            WizardSummaryItem("Body Sensation", state.symptoms.joinToString(", ").ifEmpty { "None" })

            Spacer(Modifier.height(80.dp))
        }
    }
}

// Unique name to prevent conflict
@Composable
fun WizardSummaryItem(label: String, value: String) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = LightGreenText, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        Text(value, fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Normal)
    }
    HorizontalDivider(color = Color(0xFFF5F5F5))
}

// --- HELPER COMPONENTS ---
@Composable
fun StepSlider(value: Float, max: Float, unit: String, onChange: (Float) -> Unit) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
            Text(unit, color = TextGray, fontSize = 16.sp)
            Text("${value.toInt()}", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(16.dp))
        Slider(
            value = value, onValueChange = onChange, valueRange = 0f..max,
            colors = SliderDefaults.colors(thumbColor = Color.Black, activeTrackColor = Color.Black, inactiveTrackColor = Color(0xFFEEEEEE))
        )
    }
}

@Composable
fun StepSimpleOptions(options: List<String>, selected: String?, isHorizontal: Boolean = false, onSelect: (String) -> Unit) {
    if (isHorizontal) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            options.forEach { opt ->
                Box(Modifier.weight(1f)) { OptionButton(opt, selected == opt) { onSelect(opt) } }
            }
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
        ),
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) RoyalBlue else Color(0xFFEEEEEE))
    ) {
        Text(text, fontSize = 16.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium)
    }
}

@Composable
fun StepHeroImage(options: List<String>, selected: String?, placeholderColor: Color, onSelect: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(
            modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(12.dp)).background(placeholderColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.WbSunny, contentDescription = null, tint = Color.White, modifier = Modifier.size(64.dp))
        }
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
                    .border(if (isSelected) 2.dp else 0.dp, if (isSelected) RoyalBlue else Color.Transparent, RoundedCornerShape(16.dp))
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

// --- DATA HELPERS ---
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
