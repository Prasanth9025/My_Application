package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.UserSession
import com.example.myapplication.network.ForgotPasswordRequest
import com.example.myapplication.network.LoginRequest
import com.example.myapplication.network.RegisterRequest
import com.example.myapplication.network.ResetPasswordRequest
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

    // New states for Forgot Password flow
    private val _forgotPasswordState = MutableStateFlow<AuthStatus>(AuthStatus.Idle)
    val forgotPasswordState: StateFlow<AuthStatus> = _forgotPasswordState

    private val _resetPasswordState = MutableStateFlow<AuthStatus>(AuthStatus.Idle)
    val resetPasswordState: StateFlow<AuthStatus> = _resetPasswordState

    // Temporary storage for email to use in the Reset step
    var tempEmail: String = ""

    // --- LOGIN FUNCTION ---
    fun login(email: String, pass: String) {
        _loginState.value = AuthStatus.Loading
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.loginUser(LoginRequest(email, pass))

                if (response.status.equals("success", ignoreCase = true) && response.userId != null) {
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
                val request = RegisterRequest(name, email, pass, phone)
                val response = RetrofitClient.apiService.registerUser(request)

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

    // --- 1. SEND OTP (FORGOT PASSWORD) ---
    fun sendOtp(email: String) {
        _forgotPasswordState.value = AuthStatus.Loading
        tempEmail = email // Save email for the next step
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.forgotPassword(ForgotPasswordRequest(email))

                if (response.status.equals("success", ignoreCase = true)) {
                    _forgotPasswordState.value = AuthStatus.Success
                } else {
                    _forgotPasswordState.value = AuthStatus.Error(response.message)
                }
            } catch (e: Exception) {
                _forgotPasswordState.value = AuthStatus.Error("Connection Failed: ${e.message}")
            }
        }
    }

    // --- 2. RESET PASSWORD (VERIFY OTP + NEW PASS) ---
    fun resetPassword(otp: String, newPass: String) {
        _resetPasswordState.value = AuthStatus.Loading
        viewModelScope.launch {
            try {
                // We use 'tempEmail' stored from the previous step
                val request = ResetPasswordRequest(tempEmail, otp, newPass)
                val response = RetrofitClient.apiService.resetPassword(request)

                if (response.status.equals("success", ignoreCase = true)) {
                    _resetPasswordState.value = AuthStatus.Success
                } else {
                    _resetPasswordState.value = AuthStatus.Error(response.message)
                }
            } catch (e: Exception) {
                _resetPasswordState.value = AuthStatus.Error("Connection Failed: ${e.message}")
            }
        }
    }

    fun resetLoginState() { _loginState.value = AuthStatus.Idle }
    fun resetRegisterState() { _registerState.value = AuthStatus.Idle }
    fun resetForgotState() { _forgotPasswordState.value = AuthStatus.Idle }
    fun resetResetState() { _resetPasswordState.value = AuthStatus.Idle }
}

// Helper Sealed Class
sealed class AuthStatus {
    object Idle : AuthStatus()
    object Loading : AuthStatus()
    object Success : AuthStatus()
    data class Error(val msg: String) : AuthStatus()
}