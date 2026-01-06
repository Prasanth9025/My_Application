package com.example.myapplication.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.UserSession
import com.example.myapplication.network.RetrofitClient
import com.example.myapplication.network.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    // Default state with empty strings
    private val _userProfile = MutableStateFlow(UserProfile("", "", "", "", "", ""))
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // --- NEW: State for Profile Image ---
    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri: StateFlow<Uri?> = _profileImageUri.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun fetchProfileData() {
        val userId = UserSession.getUserId()
        // Check for both 0 and -1 depending on your Session implementation
        if (userId == 0 || userId == -1) return

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

    // --- NEW: Function to Handle Image Selection ---
    fun updateProfileImage(uri: Uri) {
        // 1. Update the UI State immediately so the user sees the image
        _profileImageUri.value = uri

        // 2. (Optional) Trigger background upload here
        // uploadImageToServer(uri)
    }
}