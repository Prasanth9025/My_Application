package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.*
import com.example.myapplication.viewmodel.CheckInViewModel

// --- 1. Navigation Items Data Class ---
sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    object CheckIn : BottomNavItem("checkin_wizard", "Check-in", Icons.Default.CheckCircle)
    object Guidance : BottomNavItem("guidance", "Guidance", Icons.Default.Spa)
    object Insights : BottomNavItem("insights", "Insights", Icons.Default.BarChart)
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel: CheckInViewModel = viewModel()

            // Define Bottom Navigation Items
            val items = listOf(
                BottomNavItem.Home,
                BottomNavItem.CheckIn,
                BottomNavItem.Guidance,
                BottomNavItem.Insights,
                BottomNavItem.Profile
            )

            // Logic to show/hide bottom bar
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val showBottomBar = items.any { it.route == currentRoute }

            Scaffold(
                bottomBar = {
                    if (showBottomBar) {
                        BottomNavigationBar(navController = navController, items = items)
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    // CHANGE THIS: Set to "splash" for normal app flow, or BottomNavItem.CheckIn.route to test check-in
                    startDestination = "splash",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    // --- ONBOARDING & AUTHENTICATION ---
                    composable("splash") { SplashScreen { navController.navigate("welcome") { popUpTo("splash") { inclusive = true } } } }
                    composable("welcome") { WelcomeScreen { navController.navigate("wellness") } }
                    composable("wellness") { WellnessScreen { navController.navigate("features") } }
                    composable("features") { FeatureScreen { navController.navigate("login") } }

                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = { navController.navigate(BottomNavItem.Home.route) { popUpTo("login") { inclusive = true } } },
                            onSignUpClick = { navController.navigate("signup") },
                            onForgotPasswordClick = { navController.navigate("forgot_password") }
                        )
                    }

                    composable("signup") {
                        SignUpScreen(
                            onSignUpSuccess={ navController.navigate("login") },
                            onLoginClick={ navController.navigate("login") }
                        )
                    }

                    composable("forgot_password") {
                        ForgotPasswordScreen(
                            onSendCodeClick = { navController.navigate("verify_otp") },
                            onBackClick = { navController.navigate("login") }
                        )
                    }

                    composable("verify_otp") {
                        VerifyOtpScreen(
                            onVerifyClick = { navController.navigate("reset_password") },
                            onCloseClick = { navController.popBackStack() }
                        )
                    }

                    composable("reset_password") {
                        ResetPasswordScreen(
                            onResetClick = { navController.navigate("login") },
                            onBackClick = { navController.navigate("login") }
                        )
                    }

                    // --- HOME TAB ---
                    composable(BottomNavItem.Home.route) {
                        HomeScreen(
                            onStartCheckIn = { navController.navigate(BottomNavItem.CheckIn.route) },
                            onDoshaClick = { type -> navController.navigate("dosha_detail/$type") },
                            onNotificationClick = { navController.navigate("notifications") },
                            onGuidanceClick = { navController.navigate(BottomNavItem.Guidance.route) }
                        )
                    }

                    // --- CHECK-IN FLOW ---
                    composable(BottomNavItem.CheckIn.route) {
                        DailyCheckInScreen(
                            viewModel = viewModel,
                            onNavigateToSummary = {
                                navController.navigate("checkin_summary")
                            }
                        )
                    }
                    composable("checkin_summary") {
                        CheckInSummaryScreen(
                            viewModel,
                            onBack = { navController.popBackStack() },
                            onConfirm = {
                                viewModel.submitData()
                                navController.navigate("checkin_success")
                            }
                        )
                    }
                    composable("checkin_success") {
                        CheckInSuccessScreen {
                            println("DEBUG: Button Clicked! Attempting to go to Result") // <--- Add this
                            navController.navigate("result")
                        }
                    }

                    composable("result") {
                        val prediction by viewModel.prediction.collectAsState()
                        if (prediction != null) {
                            ResultScreen(
                                prediction = prediction!!,
                                onGoHome = {
                                    navController.navigate(BottomNavItem.Home.route) {
                                        popUpTo(BottomNavItem.Home.route) { inclusive = true }
                                    }
                                }
                            )
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Loading Result...") }
                        }
                    }

                    // --- GUIDANCE TAB ---
                    composable(BottomNavItem.Guidance.route) {
                        val prediction by viewModel.prediction.collectAsState()
                        GuidanceScreen(prediction)
                    }

                    // --- INSIGHTS TAB ---
                    composable(BottomNavItem.Insights.route) {
                        InsightsScreen(
                            onTrendClick = { type -> navController.navigate("dosha_trend/$type") },
                            onHistoryClick = { navController.navigate("history") }
                        )
                    }
                    composable("dosha_trend/{type}") { backStackEntry ->
                        val type = backStackEntry.arguments?.getString("type") ?: "Vata"
                        DoshaTrendScreen(
                            doshaType = type,
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("history") {
                        TrendsHistoryScreen(onBack = { navController.popBackStack() })
                    }

                    // --- PROFILE TAB ---
                    composable(BottomNavItem.Profile.route) {
                        val prediction by viewModel.prediction.collectAsState()
                        ProfileScreen(
                            prediction,
                            onEditProfile = { navController.navigate("edit_profile") },
                            onSettings = { navController.navigate("settings") },
                            onLogout = { navController.navigate("login") { popUpTo(0) } },
                            onStreakClick = { navController.navigate("consistency") },
                            onAboutClick = { navController.navigate("education") }
                        )
                    }

                    // --- SETTINGS HIERARCHY ---
                    composable("settings") {
                        SettingsScreen(
                            onBack = { navController.popBackStack() },
                            onSubscriptionClick = { navController.navigate("subscription") },
                            onLanguageClick = { navController.navigate("language") },
                            onPrivacyClick = { navController.navigate("privacy") },
                            onHelpClick = { navController.navigate("help_center") },
                            onTermsClick = { navController.navigate("terms_of_service") }
                        )
                    }

                    composable("language") { LanguageScreen(onBack = { navController.popBackStack() }) }
                    composable("privacy") { PrivacyScreen(onBack = { navController.popBackStack() }) }
                    composable("help_center") { HelpCenterScreen(onBack = { navController.popBackStack() }) }
                    composable("terms_of_service") { TermsOfServiceScreen(onBack = { navController.popBackStack() }) }

                    composable("subscription") {
                        SubscriptionScreen(onBack = { navController.popBackStack() }, onUpgradeClick = {})
                    }

                    composable("education") {
                        EducationScreen(
                            onBack = { navController.popBackStack() },
                            onLearnClick = { type -> navController.navigate("dosha_learn/$type") }
                        )
                    }

                    composable(
                        route = "dosha_learn/{type}",
                        arguments = listOf(navArgument("type") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val type = backStackEntry.arguments?.getString("type") ?: "Vata"
                        DoshaLearnScreen(doshaType = type, onBack = { navController.popBackStack() })
                    }

                    composable("edit_profile") { EditProfileScreen({ navController.popBackStack() }, { navController.popBackStack() }) }
                    composable("consistency") { ConsistencyScreen { navController.popBackStack() } }
                    composable("notifications") { NotificationsScreen { navController.popBackStack() } }

                    composable("dosha_detail/{type}") { backStackEntry ->
                        val type = backStackEntry.arguments?.getString("type") ?: "Vata"
                        DoshaDetailScreen(type) { navController.popBackStack() }
                    }
                }
            }
        }
    }
}

// --- 2. Bottom Navigation Bar Composable ---
@Composable
fun BottomNavigationBar(navController: androidx.navigation.NavController, items: List<BottomNavItem>) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}