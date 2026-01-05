package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.UserSession // Ensure this imports your updated UserSession
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

                if (response.status == "success" && response.user_id != null) {

                    // --- FIX: USE saveUser() TO PERSIST LOGIN ---
                    // This saves the ID to the phone's storage so the app remembers the user.
                    UserSession.saveUser(response.user_id, response.name ?: "")

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

                if (response.status == "success") {
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