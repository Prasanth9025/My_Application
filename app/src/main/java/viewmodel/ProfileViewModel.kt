package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.UserSession
import com.example.myapplication.network.RetrofitClient
import com.example.myapplication.network.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    // Default state with empty strings
    private val _userProfile = MutableStateFlow(UserProfile("", "", "", "", "", ""))
    val userProfile: StateFlow<UserProfile> = _userProfile

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchProfileData() {
        val userId = UserSession.getUserId()
        if (userId == 0) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Call the API
                val profile = RetrofitClient.apiService.getUserProfile(userId)
                _userProfile.value = profile

                // Keep Local Session Name Updated
                if (profile.name.isNotEmpty()) {
                    UserSession.saveUser(userId, profile.name)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}