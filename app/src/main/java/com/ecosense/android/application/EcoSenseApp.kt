package com.ecosense.android.application

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import logcat.AndroidLogcatLogger
import logcat.LogPriority

@HiltAndroidApp
class EcoSenseApp : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        AndroidLogcatLogger.installOnDebuggableApp(
            application = this,
            minPriority = LogPriority.VERBOSE
        )
    }
}