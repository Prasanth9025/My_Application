package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.UserSession
import com.example.myapplication.network.HistoryItem
import com.example.myapplication.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    private val _historyList = MutableStateFlow<List<HistoryItem>>(emptyList())
    val historyList: StateFlow<List<HistoryItem>> = _historyList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun fetchHistory() {
        viewModelScope.launch {
            val userId = UserSession.getUserId()
            if (userId == -1) return@launch

            try {
                _isLoading.value = true
                println("DEBUG: Fetching history for user $userId")

                // Fetch from Backend
                val response = RetrofitClient.apiService.getUserHistory(userId)

                // Update State
                _historyList.value = response
                println("DEBUG: Fetched ${response.size} items")

            } catch (e: Exception) {
                println("DEBUG: History Error: ${e.message}")
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}