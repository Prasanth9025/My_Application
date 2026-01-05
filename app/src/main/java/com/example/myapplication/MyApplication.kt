package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.UserSession

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // This wakes up the UserSession when the app icon is clicked
        UserSession.init(this)
    }
}