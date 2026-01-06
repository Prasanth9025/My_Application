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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.*
import com.example.myapplication.viewmodel.AuthViewModel
import com.example.myapplication.viewmodel.CheckInViewModel
import com.example.myapplication.data.UserSession

// --- NAVIGATION ITEMS ---
sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    object CheckIn : BottomNavItem("checkin_wizard", "Check-in", Icons.Default.CheckCircle)
    object Guidance : BottomNavItem("guidance", "Guidance", Icons.Default.Face)
    object Insights : BottomNavItem("insights", "Insights", Icons.Default.DateRange)
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserSession.init(applicationContext)

        setContent {
            val navController = rememberNavController()

            // --- VIEW MODELS ---
            val checkInViewModel: CheckInViewModel = viewModel()
            val authViewModel: AuthViewModel = viewModel() // <--- ADDED AUTH VIEWMODEL

            val items = listOf(
                BottomNavItem.Home,
                BottomNavItem.CheckIn,
                BottomNavItem.Guidance,
                BottomNavItem.Insights,
                BottomNavItem.Profile
            )

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
                    startDestination = "splash",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    // --- AUTHENTICATION ---
                    composable("splash") {
                        SplashScreen { nextRoute ->
                            navController.navigate(nextRoute) { popUpTo("splash") { inclusive = true } }
                        }
                    }
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
                        SignUpScreen(onSignUpSuccess = { navController.navigate("login") }, onLoginClick = { navController.navigate("login") })
                    }

                    // --- FORGOT PASSWORD FLOW (UPDATED) ---
                    composable("forgot_password") {
                        ForgotPasswordScreen(
                            onSendCodeClick = { navController.navigate("reset_password") },
                            onBackClick = { navController.popBackStack() },
                            authViewModel = authViewModel // Pass Shared VM
                        )
                    }

                    // --- RESET PASSWORD FLOW (UPDATED) ---
                    // Note: 'verify_otp' is removed because ResetPasswordScreen now handles OTP entry
                    composable("reset_password") {
                        ResetPasswordScreen(
                            onResetClick = {
                                // On Success, go to Login
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onBackClick = { navController.popBackStack() },
                            authViewModel = authViewModel // Pass Shared VM
                        )
                    }

                    // --- HOME ---
                    composable(BottomNavItem.Home.route) {
                        HomeScreen(
                            viewModel = checkInViewModel,
                            onStartCheckIn = { navController.navigate(BottomNavItem.CheckIn.route) },
                            onDoshaClick = { type -> navController.navigate("dosha_detail/$type") },
                            onNotificationClick = { navController.navigate("notifications") },
                            onGuidanceClick = { navController.navigate(BottomNavItem.Guidance.route) }
                        )
                    }

                    // --- CHECK-IN ---
                    composable(BottomNavItem.CheckIn.route) {
                        DailyCheckInScreen(viewModel = checkInViewModel, onNavigateToSummary = { navController.navigate("checkin_summary") })
                    }
                    composable("checkin_summary") {
                        CheckInSummaryScreen(viewModel = checkInViewModel, onBack = { navController.popBackStack() }, onConfirm = { checkInViewModel.submitData(); navController.navigate("checkin_success") })
                    }
                    composable("checkin_success") {
                        val prediction by checkInViewModel.prediction.collectAsState()
                        CheckInSuccessScreen(isReady = prediction != null, onGoToDashboard = { navController.navigate("result") { popUpTo("checkin_success") { inclusive = true } } })
                    }
                    composable("result") {
                        val prediction by checkInViewModel.prediction.collectAsState()
                        if (prediction != null) {
                            ResultScreen(prediction = prediction!!, onGoHome = { navController.navigate(BottomNavItem.Home.route) { popUpTo(BottomNavItem.Home.route) { inclusive = true } } })
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Loading Result...") }
                        }
                    }

                    // --- GUIDANCE ---
                    composable(BottomNavItem.Guidance.route) {
                        val prediction by checkInViewModel.prediction.collectAsState()
                        GuidanceScreen(prediction)
                    }

                    // --- INSIGHTS ---
                    composable(BottomNavItem.Insights.route) {
                        InsightsScreen(onTrendClick = { type -> navController.navigate("dosha_trend/$type") }, onHistoryClick = { navController.navigate("history") })
                    }
                    composable("dosha_trend/{type}") { backStackEntry ->
                        val type = backStackEntry.arguments?.getString("type") ?: "Vata"
                        DoshaTrendScreen(doshaType = type, onBack = { navController.popBackStack() }, viewModel = checkInViewModel)
                    }
                    composable("history") {
                        TrendsHistoryScreen(onBack = { navController.popBackStack() })
                    }

                    // --- PROFILE ---
                    composable(BottomNavItem.Profile.route) {
                        ProfileScreen(
                            checkInViewModel = checkInViewModel, // Correctly passing ViewModel
                            onEditProfile = { navController.navigate("edit_profile") },
                            onSettings = { navController.navigate("settings") },
                            onLogout = { navController.navigate("login") { popUpTo(0) } },
                            onStreakClick = { navController.navigate("consistency") },
                            onAboutClick = { navController.navigate("education") }
                        )
                    }

                    // --- CONSISTENCY ---
                    composable("consistency") {
                        ConsistencyScreen(
                            viewModel = checkInViewModel, // Using shared ViewModel
                            onBack = { navController.popBackStack() }
                        )
                    }

                    // --- EXTRAS ---
                    composable("settings") { SettingsScreen({ navController.popBackStack() }, { navController.navigate("subscription") }, { navController.navigate("language") }, { navController.navigate("privacy") }, { navController.navigate("help_center") }, { navController.navigate("terms_of_service") }) }
                    composable("language") { LanguageScreen { navController.popBackStack() } }
                    composable("privacy") { PrivacyScreen { navController.popBackStack() } }
                    composable("help_center") { HelpCenterScreen { navController.popBackStack() } }
                    composable("terms_of_service") { TermsOfServiceScreen { navController.popBackStack() } }
                    composable("subscription") { SubscriptionScreen({ navController.popBackStack() }, {}) }
                    composable("education") { EducationScreen({ navController.popBackStack() }, { type -> navController.navigate("dosha_learn/$type") }) }
                    composable("dosha_learn/{type}") { backStackEntry -> DoshaLearnScreen(backStackEntry.arguments?.getString("type") ?: "Vata", { navController.popBackStack() }) }
                    composable("edit_profile") { EditProfileScreen({ navController.popBackStack() }, { navController.popBackStack() }) }

                    composable("notifications") { NotificationsScreen { navController.popBackStack() } }

                    // --- DOSHA DETAIL ---
                    composable("dosha_detail/{type}") { backStackEntry ->
                        val type = backStackEntry.arguments?.getString("type") ?: "Vata"
                        DoshaDetailScreen(
                            doshaType = type,
                            onBack = { navController.popBackStack() },
                            viewModel = checkInViewModel
                        )
                    }
                }
            }
        }
    }
}

// --- BOTTOM BAR ---
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
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
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