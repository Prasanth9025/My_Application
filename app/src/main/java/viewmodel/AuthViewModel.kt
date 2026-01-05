package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.UserSession
import com.example.myapplication.network.LoginRequest
import com.example.myapplication.network.RegisterRequest
import com.example.myapplication.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    // --- STATES ---
    private val _loginState = MutableStateFlow<AuthStatus>(AuthStatus.Idle)
    val loginState: StateFlow<AuthStatus> = _loginState

    private val _registerState = MutableStateFlow<AuthStatus>(AuthStatus.Idle)
    val registerState: StateFlow<AuthStatus> = _registerState

    // --- LOGIN FUNCTION ---
    fun login(email: String, pass: String) {
        _loginState.value = AuthStatus.Loading
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.loginUser(LoginRequest(email, pass))

                // FIX 1: Use 'userId' (CamelCase)
                // FIX 2: Use ignoreCase for safer status checking
                if (response.status.equals("success", ignoreCase = true) && response.userId != null) {

                    // Save the ID and Name
                    UserSession.saveUser(response.userId, response.name ?: "")

                    _loginState.value = AuthStatus.Success
                } else {
                    _loginState.value = AuthStatus.Error(response.message)
                }
            } catch (e: Exception) {
                _loginState.value = AuthStatus.Error("Connection Failed: ${e.message}")
            }
        }
    }

    // --- REGISTER FUNCTION ---
    fun register(name: String, email: String, pass: String, phone: String) {
        _registerState.value = AuthStatus.Loading
        viewModelScope.launch {
            try {
                val request = RegisterRequest(
                    name = name,
                    email = email,
                    password = pass,
                    phone = phone
                )

                val response = RetrofitClient.apiService.registerUser(request)

                // FIX: Safer status check
                if (response.status.equals("success", ignoreCase = true)) {
                    _registerState.value = AuthStatus.Success
                } else {
                    _registerState.value = AuthStatus.Error(response.message)
                }
            } catch (e: Exception) {
                _registerState.value = AuthStatus.Error("Connection Failed: ${e.message}")
            }
        }
    }

    fun resetLoginState() { _loginState.value = AuthStatus.Idle }
    fun resetRegisterState() { _registerState.value = AuthStatus.Idle }
}

// Helper Sealed Class
sealed class AuthStatus {
    object Idle : AuthStatus()
    object Loading : AuthStatus()
    object Success : AuthStatus()
    data class Error(val msg: String) : AuthStatus()
}