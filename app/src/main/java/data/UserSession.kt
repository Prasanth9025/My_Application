package com.example.myapplication.data

object UserSession {
    var userId: Int = 0 // Default 0 means not logged in
    var userName: String = ""

    fun isLoggedIn(): Boolean {
        return userId != 0
    }

    fun clearSession() {
        userId = 0
        userName = ""
    }
}