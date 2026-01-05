package com.example.myapplication.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // Existing Prediction Endpoint
    @POST("predict.php")
    suspend fun predictDosha(@Body request: PredictionRequest): PredictionResponse

    // --- NEW ENDPOINTS ---

    @POST("login.php")
    suspend fun loginUser(@Body request: LoginRequest): LoginResponse

    @POST("register.php")
    suspend fun registerUser(@Body request: RegisterRequest): RegisterResponse

    // Fetch history using GET (e.g., get_history.php?user_id=1)
    @GET("get_history.php")
    suspend fun getUserHistory(@Query("user_id") userId: Int): List<HistoryItem>
}