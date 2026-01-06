package com.example.myapplication.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // --- PREDICTION ---
    @POST("predict.php")
    suspend fun predictDosha(@Body request: PredictionRequest): PredictionResponse

    // --- AUTHENTICATION ---
    @POST("login.php")
    suspend fun loginUser(@Body request: LoginRequest): LoginResponse

    @POST("register.php")
    suspend fun registerUser(@Body request: RegisterRequest): RegisterResponse

    // --- HISTORY ---
    // Fetches past predictions (returns a list)
    @GET("get_history.php")
    suspend fun getUserHistory(@Query("user_id") userId: Int): List<HistoryItem>

    // --- PROFILE MANAGEMENT (Updated) ---

    // 1. Get Profile: Fetches name, email, phone, dob, gender, etc.
    // Matches your new get_profile.php script
    @GET("get_profile.php")
    suspend fun getUserProfile(@Query("user_id") userId: Int): UserProfile

    // 2. Update Profile: Sends the edited details to the server
    // Matches your new update_profile.php script
    @POST("update_profile.php")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): UpdateProfileResponse

    // --- DASHBOARD ---
    // Fetches current scores, history (for graphs), and calculated trends
    @GET("get_dashboard.php")
    suspend fun getDashboard(@Query("user_id") userId: Int): DashboardResponse

    @POST("forgot_password.php")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): ForgotPasswordResponse

    @POST("reset_password.php")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): ResetPasswordResponse
}