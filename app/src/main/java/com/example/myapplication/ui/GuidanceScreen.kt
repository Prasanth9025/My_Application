package com.example.myapplication.ui

import androidx.compose.foundation.Image // Import Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale // Import ContentScale
import androidx.compose.ui.res.painterResource // Import painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.network.PredictionResponse
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
// Removed Spa icon import as it's replaced by real images
// import androidx.compose.material.icons.filled.Spa
import com.example.myapplication.R // IMPORTANT: Import R

// --- PRIVATE COLORS ---
private val GuidancePrimaryGreen = Color(0xFF2E7D32)
private val GuidanceLightGreenBg = Color(0xFFF1F8E9)
private val GuidanceTextBlack = Color(0xFF1B1B1B)
private val GuidanceTextGray = Color(0xFF616161)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuidanceScreen(prediction: PredictionResponse?) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Food Plan", "Herbal Recommendations", "Lifestyle Guidance", "Yoga & Breathing")

    // Default to Kapha if no prediction exists
    val dosha = prediction?.dosha ?: "Kapha"

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Guidance", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Personalized Guidance",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = GuidanceTextBlack,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Scrollable Tab Row
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = GuidancePrimaryGreen,
                edgePadding = 24.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = GuidancePrimaryGreen,
                        height = 3.dp
                    )
                },
                divider = { HorizontalDivider(color = Color(0xFFEEEEEE)) }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) GuidancePrimaryGreen else Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    )
                }
            }

            // Scrollable Content Area
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                when (selectedTab) {
                    0 -> FoodPlanContent(dosha)
                    1 -> HerbalContent(dosha)
                    2 -> LifestyleContent(dosha)
                    3 -> YogaContent(dosha)
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

// --- 1. FOOD PLAN TAB ---
@Composable
private fun FoodPlanContent(dosha: String) {
    val (favor, reduce) = when (dosha) {
        "Vata" -> Pair(
            listOf("Warm soups", "Cooked grains", "Nuts & Seeds", "Ghee & Oils"),
            listOf("Cold salads", "Raw vegetables", "Dry crackers", "Ice cold drinks")
        )
        "Pitta" -> Pair(
            listOf("Sweet fruits", "Leafy greens", "Coconut water", "Cucumber"),
            listOf("Spicy curries", "Sour fruits", "Fried foods", "Hot peppers")
        )
        "Kapha" -> Pair(
            listOf("Cooked vegetables", "Legumes", "Spices", "Warm beverages", "Oats", "Wheat", "Rice"),
            listOf("Dairy products", "Sweet fruits", "Cold drinks", "Oily foods")
        )
        else -> Pair(emptyList(), emptyList())
    }

    GuidanceDescription(
        title = "Recommended Food Plan",
        body = "Focus on incorporating foods that balance your $dosha dosha. Favor light, warm, and dry foods. Reduce heavy, oily, and cold foods."
    )

    Spacer(modifier = Modifier.height(24.dp))
    GuidanceSectionHeader("Foods to Favor")
    favor.forEach { GuidanceSimpleListItem(it) }

    Spacer(modifier = Modifier.height(24.dp))
    GuidanceSectionHeader("Foods to Reduce")
    reduce.forEach { GuidanceSimpleListItem(it) }
}

// --- 2. HERBAL RECOMMENDATIONS TAB (UPDATED) ---
@Composable
private fun HerbalContent(dosha: String) {
    GuidanceDescription(
        title = "Recommended Herbs",
        body = "Herbs can powerfully support your journey to balance."
    )
    Spacer(modifier = Modifier.height(24.dp))

    // Card 1: Ashwagandha
    GuidanceHerbalCard(
        name = "Ashwagandha",
        subtitle = "Balances Vata and Kapha doshas, promoting relaxation and mental clarity.",
        benefits = listOf("Reduces stress and anxiety", "Improves sleep quality", "Boosts energy levels"),
        usage = "Dosage: 300-500mg of root extract, once or twice daily. Preparation: Can be taken as a capsule, powder, or tea. Best Time: Consume with warm milk or water, preferably before bedtime.",
        precaution = "Avoid during pregnancy. Consult a healthcare professional if you have thyroid issues or are taking medications.",
        imageRes = R.drawable.ashwagandha // Using the image file
    )

    Spacer(modifier = Modifier.height(24.dp))

    // Card 2: Triphala
    GuidanceHerbalCard(
        name = "Triphala",
        subtitle = "Balances all three doshas, supporting digestion and detoxification.",
        benefits = listOf("Promotes healthy digestion", "Natural antioxidant", "Supports gentle detox"),
        usage = "Take 1/2 to 1 tsp powder with warm water before bed.",
        precaution = "May cause loose stools in high doses. Start with a small amount.",
        imageRes = R.drawable.triphala // Using the image file
    )
}

// --- 3. LIFESTYLE GUIDANCE TAB ---
@Composable
private fun LifestyleContent(dosha: String) {
    val favor = when (dosha) {
        "Kapha" -> listOf("Regular exercise", "Early rising", "Stimulating activities", "Mindfulness", "Social engagement", "Creative expression", "Nature walks")
        else -> listOf("Routine stability", "Self-massage")
    }
    val reduce = when (dosha) {
        "Kapha" -> listOf("Sedentary behavior", "Excessive sleep", "Overeating")
        else -> listOf("Irregular meals")
    }

    GuidanceDescription(
        title = "Recommended Lifestyle Guidance",
        body = "Focus on incorporating lifestyle adjustments that balance your $dosha dosha. Prioritize regular exercise, early rising, and stimulating activities. Reduce sedentary behavior and excessive sleep."
    )

    Spacer(modifier = Modifier.height(24.dp))
    GuidanceSectionHeader("Lifestyle Adjustments to Favor")
    favor.forEach { GuidanceSimpleListItem(it) }

    Spacer(modifier = Modifier.height(24.dp))
    GuidanceSectionHeader("Lifestyle Adjustments to Reduce")
    reduce.forEach { GuidanceSimpleListItem(it) }
}

// --- 4. YOGA & BREATHING TAB ---
@Composable
private fun YogaContent(dosha: String) {
    val yoga = when (dosha) {
        "Kapha" -> listOf("Sun Salutations", "Warrior Pose", "Triangle Pose", "Standing Forward Bend", "Boat Pose", "Bridge Pose", "Cobra Pose")
        else -> listOf("Tree Pose", "Child's Pose")
    }
    val breathing = when (dosha) {
        "Kapha" -> listOf("Kapalabhati", "Bhastrika", "Ujjayi")
        else -> listOf("Nadi Shodhana")
    }

    GuidanceDescription(
        title = "Recommended Yoga & Breathing",
        body = "Focus on incorporating yoga and breathing exercises that balance your $dosha dosha. Favor dynamic, warming practices. Reduce slow, cooling practices."
    )

    Spacer(modifier = Modifier.height(24.dp))
    GuidanceSectionHeader("Yoga to Favor")
    yoga.forEach { GuidanceSimpleListItem(it) }

    Spacer(modifier = Modifier.height(24.dp))
    GuidanceSectionHeader("Breathing to Favor")
    breathing.forEach { GuidanceSimpleListItem(it) }
}

// --- COMPONENTS ---

@Composable
private fun GuidanceDescription(title: String, body: String) {
    Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GuidanceTextBlack)
    Spacer(modifier = Modifier.height(8.dp))
    Text(body, fontSize = 14.sp, color = GuidanceTextGray, lineHeight = 20.sp)
}

@Composable
private fun GuidanceSectionHeader(title: String) {
    Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GuidanceTextBlack)
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun GuidanceSimpleListItem(text: String) {
    Row(modifier = Modifier.padding(vertical = 12.dp)) {
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, fontSize = 15.sp, color = GuidanceTextGray)
    }
    HorizontalDivider(color = Color(0xFFF5F5F5))
}

@Composable
private fun GuidanceHerbalCard(
    name: String,
    subtitle: String,
    benefits: List<String>,
    usage: String,
    precaution: String,
    imageRes: Int // Added parameter for Image Resource ID
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = GuidanceLightGreenBg),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Updated Image Section
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop // Ensures image fills the area properly
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = GuidanceTextBlack)
                Spacer(modifier = Modifier.height(4.dp))
                Text(subtitle, fontSize = 13.sp, color = GuidancePrimaryGreen, lineHeight = 18.sp)

                Spacer(modifier = Modifier.height(16.dp))
                Text("Benefits", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = GuidanceTextBlack)
                Spacer(modifier = Modifier.height(8.dp))

                benefits.forEach { benefit ->
                    Row(
                        modifier = Modifier.padding(bottom = 8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = null,
                            tint = GuidanceTextBlack,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(benefit, fontSize = 14.sp, color = GuidanceTextBlack)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Safe Usage", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = GuidanceTextBlack)
                Text(usage, fontSize = 13.sp, color = GuidanceTextGray, lineHeight = 18.sp)

                Spacer(modifier = Modifier.height(16.dp))
                Text("Precautions", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = GuidanceTextBlack)
                Text(precaution, fontSize = 13.sp, color = GuidanceTextGray, lineHeight = 18.sp)
            }
        }
    }
}
