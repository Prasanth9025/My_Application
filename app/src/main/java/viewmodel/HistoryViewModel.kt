package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.UserSession
import com.example.myapplication.network.HistoryItem
import com.example.myapplication.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    private val _historyList = MutableStateFlow<List<HistoryItem>>(emptyList())
    val historyList: StateFlow<List<HistoryItem>> = _historyList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchHistory() {
        val userId = UserSession.getUserId()
        if (userId == 0) return // Not logged in

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getUserHistory(userId)
                _historyList.value = response
            } catch (e: Exception) {
                e.printStackTrace()
                _historyList.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}