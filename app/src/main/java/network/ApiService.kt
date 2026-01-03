package com.example.myapplication.network

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("predict")
    suspend fun predictDosha(@Body request: PredictionRequest): PredictionResponse
}