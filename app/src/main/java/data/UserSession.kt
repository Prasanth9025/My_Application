package com.example.myapplication.data

import android.content.Context
import android.content.SharedPreferences

object UserSession {
    private const val PREF_NAME = "user_session"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"

    private lateinit var prefs: SharedPreferences

    // Call this once in your Application class or MainActivity
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // --- SAVE USER ---
    fun saveUser(userId: Int, name: String) {
        prefs.edit().apply {
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, name)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply() // Asynchronous save
        }
    }

    // --- GET USER ---
    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1) // Returns -1 if not found
    }

    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, "User")
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // --- LOGOUT ---
    fun clearSession() {
        prefs.edit().clear().apply()
    }
}