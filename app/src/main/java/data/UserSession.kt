package com.example.myapplication.data

import android.content.Context
import android.content.SharedPreferences

object UserSession {
    private const val PREF_NAME = "user_prefs"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"

    private lateinit var preferences: SharedPreferences

    // Initialize this in MainActivity
    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Save User Data (Login)
    fun saveUser(id: Int, name: String) {
        preferences.edit().apply {
            putInt(KEY_USER_ID, id)
            putString(KEY_USER_NAME, name)
            apply() // Save to disk
        }
    }

    // Check if Logged In
    fun isLoggedIn(): Boolean {
        return getUserId() != 0
    }

    fun getUserId(): Int {
        return preferences.getInt(KEY_USER_ID, 0) // Default 0 (Not logged in)
    }

    fun getUserName(): String {
        return preferences.getString(KEY_USER_NAME, "User") ?: "User"
    }

    // Clear Data (Logout)
    fun clearSession() {
        preferences.edit().clear().apply()
    }
}