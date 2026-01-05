package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.UserSession
import com.example.myapplication.network.RetrofitClient
import com.example.myapplication.network.UpdateProfileRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditProfileViewModel : ViewModel() {

    private val _updateStatus = MutableStateFlow<String?>(null)
    val updateStatus: StateFlow<String?> = _updateStatus

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun updateProfile(name: String,email: String, phone: String, gender: String, dob: String, country: String, password: String?) {
        val userId = UserSession.getUserId()
        if (userId == -1) {
            _updateStatus.value = "Error: User not logged in"
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                // 1. Create the request object
                val request = UpdateProfileRequest(
                    userId = userId,
                    name = name,
                    email = email,
                    phone = phone,
                    gender = gender,
                    dob = dob,
                    country = country,
                    password = password // Optional
                )

                // 2. Call the API
                val response = RetrofitClient.apiService.updateProfile(request)

                if (response.status.equals("success", ignoreCase = true)) {
                    // 3. IMPORTANT: Update local session immediately
                    UserSession.saveUser(userId, name)
                    _updateStatus.value = "Success"
                } else {
                    _updateStatus.value = "Failed: ${response.message}"
                }
            } catch (e: Exception) {
                _updateStatus.value = "Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetStatus() {
        _updateStatus.value = null
    }
}